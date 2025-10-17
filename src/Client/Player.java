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
    private Thread sender;
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
                sendMessage(input.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGameState(byte[] data) {
        // Loop through the data starting after the header
        int index = MagicNumbers.FULL_GAME_STATE_HEADER_SIZE;
        while (true) {
            // Check if we've reached the end of the data
            if (index >= data.length) break;
            // Read board dimensions
            int width = data[index + MagicNumbers.WIDTH_INDEX];
            int height = data[index + MagicNumbers.HEIGHT_INDEX];
            int bombCount = data[index + MagicNumbers.BOMB_COUNT_INDEX];
            // Read client ID
            byte[] clientIdBytes = new byte[8];
            System.arraycopy(data, index + MagicNumbers.CLIENT_ID_INDEX, clientIdBytes, 0, 8);
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
                board.update(boardData);
            } else { // Otherwise, update or add the other player's board
                System.out.println("Received board for client ID: " + clientId + " (not current player)");
                // Store the other player's board
                PlayerBoard otherBoard = new PlayerBoard(boardData);
                otherBoards.put(clientId, otherBoard);
            }
            // Move to the next board in the data
            index += boardSize;
        }
    }

    private void handleMessage(byte[] message) {
        // Handle incoming messages from the server
        byte messageType = message[MagicNumbers.SERVER_MESSAGE_TYPE_INDEX];
        if (messageType == MagicNumbers.FULL_GAME_STATE_INDICATOR) {
            // Full game state update
            updateGameState(message);
        } else {
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
                    updateBoard(message);
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
            System.out.println("[*]"); // Bomb
        } else if (tile == MagicNumbers.TILE_FLAG) {
            System.out.println("[F]"); // Flag
        } else if (tile != MagicNumbers.TILE_EMPTY) {
            System.out.println("[" + (tile & MagicNumbers.TILE_NUMBER_MASK) + "]"); // Number
        } else {
            System.out.println("[ ]"); // Empty
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
        sender.start();
    }

    // Method to update the player's board state
    public void updateBoard(byte[] data) {
        if (board == null) {
            throw new IllegalStateException("Board not initialized.");
        } else {
            board.update(data);
        }
    }
}
