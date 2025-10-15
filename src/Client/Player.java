package Client;

import Minesweeper.PlayerBoard;

// Player class extending Client with player-specific functionality
public class Player extends Client {
    Thread receiver;
    Thread sender;

    PlayerBoard board;

    public Player(String serverAddress, int serverPort, int id) throws Exception {
        super(serverAddress, serverPort, id);

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

        sender = new Thread(() -> {
            while (true) {
                try {
                    // Example: send a heartbeat or status update every 5 seconds
                    sendMessage("Player heartbeat".getBytes());
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void connect() throws Exception {
        super.connect();
        receiver.start();
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
