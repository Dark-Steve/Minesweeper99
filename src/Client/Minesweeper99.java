package Client;

import java.net.InetAddress;
import java.util.Random;

import Utils.MagicNumbers;

// Main class to start the Minesweeper99 client application
// Displays main menu and server browser
// Allows user to host or join a game
// Starts a Player instance or server instance based on user choice
public class Minesweeper99 {

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        int port = random.nextInt(10000, 60000);
        VisualPlayer player = new VisualPlayer(MagicNumbers.DEFAULT_SERVER_ADDRESS, MagicNumbers.DEFAULT_SERVER_PORT, InetAddress.getLocalHost().hashCode() * port);
        player.connect();
        player.run();
    }
}