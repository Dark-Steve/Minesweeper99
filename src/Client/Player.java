package Client;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import Minesweeper.PlayerBoard;
import Utils.MagicNumbers;

// Player class extending Client with player-specific functionality
public class Player extends Client {
    private Thread receiver;
    private Thread sender;
    private Scanner scanner;

    private PlayerBoard board;

    public Player(String serverAddress, int serverPort, int id) throws Exception {
        super(serverAddress, serverPort, id);
        scanner = new Scanner(System.in);
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

    public void run() throws Exception {
        int interval = 16; // Approx. 60 UPS

        while (true) {
            long startTime = System.currentTimeMillis();

            // Process incoming messages
            while (!receivedQueue.isEmpty()) {
                try {
                    byte[] message = receivedQueue.take();
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
            board = new PlayerBoard(data);
        } else {
            board.update(data);
        }
    }
}
