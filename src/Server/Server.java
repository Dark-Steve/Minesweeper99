package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Minesweeper.Game;
import Utils.MagicNumbers;
import Utils.Util;

// A simple UDP server for Minesweeper99
public class Server {

    private DatagramSocket socket;

    private ExecutorService processingPool = Executors.newFixedThreadPool(4);
    private BlockingQueue<DatagramPacket> messageQueue = new LinkedBlockingQueue<>();
    private ConcurrentHashMap<Long, ServerClient> clientMap = new ConcurrentHashMap<>();

    public Server() throws SocketException {
        socket = new DatagramSocket(12345);

        // Initialize receiver thread
        Thread receiver = new Thread(() -> {
            while (true) {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    
                    // Obtain client ID from packet (address.hashCode() * port as a simple example)
                    // If connection is from a new IP, add to clients list
                    boolean knownClient = packet.getData()[MagicNumbers.CLIENT_TYPE_INDEX] != MagicNumbers.MSG_CONNECT;

                    if (!knownClient) {
                        byte[] clientIdBytes = new byte[8];
                        System.arraycopy(packet.getData(), MagicNumbers.CLIENT_ID_INDEX, clientIdBytes, 0, 8);
                        long clientId = Util.bytesToLong(clientIdBytes);
                        ServerClient newClient = new ServerClient(packet.getAddress(), packet.getPort(), clientId);
                        clientMap.put(clientId, newClient);
                        System.out.println("New client connected: " + packet.getAddress() + ":" + packet.getPort());
                    }

                    // Add packet to message queue
                    // ALWAYS LAST STEP
                    messageQueue.put(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();

        // Initialize processing threads
        for (int i = 0; i < 4; i++) {
            processingPool.submit(() -> {
                while (true) {
                    try {
                        System.out.println("Waiting for message...");
                        DatagramPacket packet = messageQueue.take();
                        processPacket(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void sendMessage(byte[] message, ServerClient client) throws Exception {
        DatagramPacket packet = new DatagramPacket(message, message.length, client.address, client.port);
        socket.send(packet);
    }

    private byte[] serializeGameState() {
        // First pass: calculate total size
        int totalSize = 0;
        List<byte[]> gameStates = new ArrayList<>(clientMap.size());

        for (ServerClient client : clientMap.values()) {
            if (client.game != null) {
                byte[] state = client.game.toByteArray(client.id);
                gameStates.add(state);
                totalSize += state.length;
            }
        }

        // Second pass: copy to result array
        byte[] serialized = new byte[totalSize + MagicNumbers.FULL_GAME_STATE_HEADER_SIZE];
        
        // Set header values
        serialized[MagicNumbers.SERVER_MESSAGE_TYPE_INDEX] = MagicNumbers.FULL_GAME_STATE_INDICATOR;
        serialized[MagicNumbers.FULL_GAME_BOARD_COUNT_INDEX] = (byte) gameStates.size();

        int currentIndex = MagicNumbers.FULL_GAME_STATE_HEADER_SIZE; // Leave space for header
        for (byte[] state : gameStates) {
            System.arraycopy(state, 0, serialized, currentIndex, state.length);
            currentIndex += state.length;
        }

        return serialized;
    }

    public void processPacket(DatagramPacket packet) throws Exception {
        long clientId = packet.getAddress().hashCode() * packet.getPort();
        ServerClient client = clientMap.get(clientId);

        // Check if the client has a valid game
        if (client.game == null) {
            // If not, create a new game for the client
            client.game = new Game(MagicNumbers.DEFAULT_WIDTH, MagicNumbers.DEFAULT_HEIGHT,
                    MagicNumbers.DEFAULT_BOMB_COUNT);
            byte[] response = client.game.toByteArray(clientId);
            sendMessage(response, client);
            return;
        }

        // If the client has a game, process the move
        byte[] data = packet.getData();
        // Get message type from header
        byte messageType = data[MagicNumbers.CLIENT_TYPE_INDEX];
        if (messageType == MagicNumbers.HEART_BEAT) {
            // TODO: implement client timeout timer
        } else if (messageType == MagicNumbers.MSG_REVEAL) {
            // Process reveal message
            if (data.length < MagicNumbers.CLIENT_MESSAGE_HEADER_SIZE + 2) {
                System.out.println("Invalid reveal message length");
                return;
            }
            int x = data[MagicNumbers.CLIENT_MESSAGE_HEADER_SIZE];
            int y = data[MagicNumbers.CLIENT_MESSAGE_HEADER_SIZE + 1];
            client.game.reveal(x, y);
        } else if (messageType == MagicNumbers.MSG_FLAG) {
            // Process flag message
            if (data.length < MagicNumbers.CLIENT_MESSAGE_HEADER_SIZE + 2) {
                System.out.println("Invalid flag message length");
                return;
            }
            int x = data[MagicNumbers.CLIENT_MESSAGE_HEADER_SIZE];
            int y = data[MagicNumbers.CLIENT_MESSAGE_HEADER_SIZE + 1];
            client.game.flag(x, y);
        } else {
            System.out.println("Unknown message type: " + messageType);
            return;
        }

        byte[] gameState = serializeGameState();
        // Send updated board state back to the client
        sendMessage(gameState, client);

        // Echo message back to client for testing
        sendMessage(packet.getData(), client);
    }
}
