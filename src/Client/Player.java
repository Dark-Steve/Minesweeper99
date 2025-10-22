package Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import Minesweeper.PlayerBoard;
import Utils.MagicNumbers;
import Utils.Util;

// Player class extending Client with player-specific functionality
public class Player extends Client {
    private Thread receiver;
    private Scanner scanner;

    private PlayerBoard board;

    private HashMap<Long, PlayerBoard> otherBoards;

    public Player(String serverAddress, int serverPort, int id) throws Exception {
        super(serverAddress, serverPort, id);
        scanner = new Scanner(System.in);
        otherBoards = new HashMap<>();
    }

    public void startReceiver() {
        // Start a thread to listen for incoming messages
        receiver = new Thread(() -> {
            while (true) {
                try {
                    receiveMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();
    }

    public void getInput() throws Exception {
        if (System.in.available() > 0) {
            String input = scanner.nextLine();
            try {
                // Determine action based on input
                if (input.startsWith("reveal")) {
                    byte[] inputBytes = new byte[3];
                    inputBytes[0] = MagicNumbers.MSG_REVEAL;
                    String[] parts = input.split(" ");
                    inputBytes[1] = (byte) Integer.parseInt(parts[1]);
                    inputBytes[2] = (byte) Integer.parseInt(parts[2]);
                    sendMessage(inputBytes);
                    return;
                } else if (input.startsWith("flag")) {
                    byte[] inputBytes = new byte[3];
                    inputBytes[0] = MagicNumbers.MSG_FLAG;
                    String[] parts = input.split(" ");
                    inputBytes[1] = (byte) Integer.parseInt(parts[1]);
                    inputBytes[2] = (byte) Integer.parseInt(parts[2]);
                    sendMessage(inputBytes);
                    return;
                } else {
                   byte[] inputBytes = input.getBytes();
                   sendMessage(inputBytes);
                   return;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGameState(byte[] data) {
        // Loop through the data starting after the header
        int index = MagicNumbers.FULL_GAME_STATE_HEADER_SIZE;
        int gameCount = data[MagicNumbers.FULL_GAME_BOARD_COUNT_INDEX];
        int processedGames = 0;
        System.out.println(clientId);
        while (true) {
            // Check if we've reached the end of the data
            if (processedGames >= gameCount) break;
            // Read board dimensions
            int width = data[index + MagicNumbers.WIDTH_INDEX];
            int height = data[index + MagicNumbers.HEIGHT_INDEX];
            int bombCount = data[index + MagicNumbers.BOMB_COUNT_INDEX];
            // Read client ID
            byte[] clientIdBytes = new byte[MagicNumbers.CLIENT_ID_BYTE_ARRAY_SIZE];
            System.arraycopy(data, index + MagicNumbers.CLIENT_ID_INDEX, clientIdBytes, 0, MagicNumbers.CLIENT_ID_BYTE_ARRAY_SIZE
            );
            long clientId = Util.bytesToLong(clientIdBytes);
            // Calculate board size
            int boardSize = (width * height) + MagicNumbers.BOARD_HEADER_SIZE;
            // Extract board data
            byte[] boardData = new byte[boardSize];
            System.arraycopy(data, index, boardData, 0, boardSize);
            // If this board is for the current player, update the local board
            if (clientId == this.clientId) {
                if (board == null) {
                    System.out.println("How tf did this even happen");
                }
                System.out.println("\nReceived board for client ID: " + clientId + " (current player)\n");
                updateBoard(boardData);
            } else { // Otherwise, update or add the other player's board
                System.out.println("Received board for client ID: " + clientId + " (not current player)");
                // Store the other player's board
                PlayerBoard otherBoard = new PlayerBoard(boardData);
                otherBoards.put(clientId, otherBoard);
            }
            // Move to the next board in the data
            processedGames++;
            index += boardSize;
        }
    }

    private void handleMessage(byte[] message) {
        // Handle incoming messages from the server
        byte messageType = message[MagicNumbers.SERVER_MESSAGE_TYPE_INDEX];
        if (messageType == MagicNumbers.FULL_GAME_STATE_INDICATOR) {
            // Full game state update
            updateGameState(message);
        } else if (messageType == MagicNumbers.BOARD_INDICATOR) {
            // Single board update (mainly for testing purposes)
            updateBoard(message);
        } else {
            System.out.println(new String(message));
            System.out.println("Received unknown message type: " + messageType);
        }
    }

    public void run() throws Exception {
        int interval = 16; // Approx. 60 UPS

        while (true) {
            long startTime = System.currentTimeMillis();

            // Process incoming messages
            while (!receivedQueue.isEmpty()) {
                try {
                    byte[] message = receivedQueue.take();
                    handleMessage(message);
                    displayBoard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Get user input and send to server
            getInput();

            // Sleep to maintain the update rate
            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = interval - elapsedTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayTile(byte tile) {
        if (tile == MagicNumbers.TILE_BOMB) {
            System.out.print("[*]"); // Bomb
        } else if (tile == MagicNumbers.TILE_FLAG) {
            System.out.print("[F]"); // Flag
        } else if (tile != MagicNumbers.TILE_EMPTY) {
            System.out.print("[" + (tile & MagicNumbers.TILE_NUMBER_MASK) + "]"); // Number
        } else {
            System.out.print("[ ]"); // Empty
        }
    }

    public void displayBoard() {
        if (board != null) {
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    displayTile(board.getTile(x, y));
                }
                System.out.println();
            }
        } else {
            System.out.println("Board not initialized.");
        }
    }

    @Override
    public void connect() throws Exception {
        super.connect();
        startReceiver();
        byte[] msg = {MagicNumbers.MSG_CONNECT};
        sendMessage(msg);
    }

    // Method to update the player's board state
    public void updateBoard(byte[] data) {
        board = new PlayerBoard(data);
    }
}
