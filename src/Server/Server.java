package Server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Client.Client;
import Minesweeper.Game;
import Utils.MagicNumbers;

// A simple UDP server for Minesweeper99
public class Server {

    private DatagramSocket socket;

    private ExecutorService processingPool = Executors.newFixedThreadPool(4);
    private BlockingQueue<DatagramPacket> messageQueue = new LinkedBlockingQueue<>();
    private CopyOnWriteArrayList<ServerClient> clients = new CopyOnWriteArrayList<>();
    private ConcurrentHashMap<InetAddress, Integer> clientMap = new ConcurrentHashMap<>();

    public Server() throws SocketException {
        socket = new DatagramSocket(12345);

        // Initialize receiver thread
        Thread receiver = new Thread(() -> {
            while (true) {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    messageQueue.put(packet);
                    // If connection is from a new IP, add to clients list
                    boolean knownClient = clientMap.containsKey(packet.getAddress());
                    if (!knownClient) {
                        clients.add(new ServerClient(packet.getAddress(), packet.getPort(), clients.size() + 1));
                        clientMap.put(packet.getAddress(), packet.getPort());
                        System.out.println("New client connected: " + packet.getAddress() + ":" + packet.getPort());
                    }
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

    public void processPacket(DatagramPacket packet) throws Exception {
        Integer clientId = clientMap.get(packet.getAddress());
        ServerClient client = clients.get(clientId);

        // Check if the client has a valid game
        if (client.game == null) {
            // If not, create a new game for the client
            client.game = new Game(MagicNumbers.DEFAULT_WIDTH, MagicNumbers.DEFAULT_HEIGHT, MagicNumbers.DEFAULT_BOMB_COUNT);
            sendMessage(client.game.toByteArray(), client);
            return;
        }

        // If the client has a game, process the move
        byte[] data = packet.getData();
        int x = data[0];
        int y = data[1];
        boolean flag = data[2] == 1;
        client.game.play(x, y, flag);

        // Send updated board state back to the client
        sendMessage(client.game.toByteArray(), client);

        // Process the packet (e.g., parse message, update game state, etc.)
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Received: " + message + " from " + packet.getAddress() + ":" + packet.getPort());

        // Example: Echo the message back to the sender
        try {
            DatagramPacket response = new DatagramPacket(packet.getData(), packet.getLength(), packet.getAddress(), packet.getPort());
            socket.send(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
