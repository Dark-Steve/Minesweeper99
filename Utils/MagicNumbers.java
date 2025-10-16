package Utils;

public class MagicNumbers {
    // Magic numbers used in Minesweeper99

    // Board defaults
    public static final int DEFAULT_WIDTH = 10; // Default board width
    public static final int DEFAULT_HEIGHT = 10; // Default board height
    public static final int DEFAULT_BOMB_COUNT = 10; // Default number of bombs

    // Tile representation constants
    public static final byte TILE_REVEALED = 0x10; // Represents a revealed tile
    public static final byte TILE_BOMB = 0x30; // Represents a bomb tile
    public static final byte TILE_FLAG = 0x40; // Represents a flagged tile
    public static final byte TILE_EMPTY = 0x00; // Represents an empty tile
    public static final byte TILE_NUMBER_MASK = 0x0F; // Mask to extract number of adjacent bombs

    // Board representation constants
    public static final int BOARD_HEADER_SIZE = 3; // Size of the board header in bytes
    public static final int WIDTH_INDEX = 0; // Index of width in board data
    public static final int HEIGHT_INDEX = 1; // Index of height in board data
    public static final int BOMB_COUNT_INDEX = 2; // Index of bomb count in board data

    // Network communication constants
    public static final int HEARTBEAT_INTERVAL_MS = 5000; // Interval for sending heartbeat messages
    public static final int MAX_MESSAGE_SIZE = 1024; // Maximum size of a network message in bytes
    public static final int SERVER_PORT = 9876; // Default server port
    public static final String SERVER_ADDRESS = "localhost"; // Default server address
}
