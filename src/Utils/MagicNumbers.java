package Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MagicNumbers {
    // Magic numbers used in Minesweeper99

    // Indexing constants
    public static final byte CLIENT_ID_INDEX = 0; // Index of client ID in board data
    public static final byte CLIENT_ID_BYTE_ARRAY_SIZE = 8; // Size of the client ID byte arrays
    public static final byte SERVER_MESSAGE_TYPE_INDEX = 0 + CLIENT_ID_BYTE_ARRAY_SIZE; // Index of value indicating message type from the server
    public static final byte WIDTH_INDEX = 1 + CLIENT_ID_BYTE_ARRAY_SIZE; // Index of width in board data
    public static final byte HEIGHT_INDEX = 2 + CLIENT_ID_BYTE_ARRAY_SIZE; // Index of height in board data
    public static final byte BOMB_COUNT_INDEX = 3 + CLIENT_ID_BYTE_ARRAY_SIZE; // Index of bomb count in board data
    public static final byte CLIENT_TYPE_INDEX = 0 + CLIENT_ID_BYTE_ARRAY_SIZE; // Index of the message type in client messages


    // Board defaults
    public static final byte DEFAULT_WIDTH = 5; // Default board width
    public static final byte DEFAULT_HEIGHT = 5; // Default board height
    public static final byte DEFAULT_BOMB_COUNT = 3; // Default number of bombs

    // Tile representation constants
    public static final byte TILE_HIDDEN = 0x00; // Represents a hidden tile
    public static final byte TILE_NUMBER_MASK = 0x0F; // Mask to extract number of adjacent bombs
    public static final byte TILE_REVEALED = 0x10; // Represents a revealed tile
    public static final byte TILE_BOMB = 0x20; // Represents a bomb tile
    public static final byte TILE_FLAG = 0x30; // Represents a flagged tile
    public static final byte TILE_QUESTION = 0x40; // Represents a questioned tile
    public static final byte TILE_DEPRESSED_QUESTION = 0x50; // Represents a depressed question tile
    public static final byte TILE_EXPLODED_BOMB = 0x60; // Represents an exploded bomb tile
    public static final byte TILE_WRONG_FLAG = 0x70; // Represents a wrong flag tile

    // Client message constants
    public static final byte CLIENT_MESSAGE_HEADER_SIZE = 1 + CLIENT_ID_BYTE_ARRAY_SIZE; // Size of the client message header in bytes
    // Message types
    public static final byte MSG_CONNECT = 0x00; // Message to connect to the server
    public static final byte MSG_REVEAL = 0x01; // Message to reveal a tile
    public static final byte MSG_FLAG = 0x02; // Message to flag a tile
    public static final byte MSG_HEARTBEAT = 0x7F; // Heartbeat message

    // Server message constants
    public static final byte FULL_GAME_STATE_INDICATOR = 0x7E; // Value indicating that the data is full game state
    public static final byte FULL_GAME_STATE_HEADER_SIZE = 2 + CLIENT_ID_BYTE_ARRAY_SIZE; // Size of the full game state header in bytes
    public static final byte FULL_GAME_BOARD_COUNT_INDEX = 1 + CLIENT_ID_BYTE_ARRAY_SIZE; // Index of the number of boards in full game state message

    // Board representation constants
    public static final byte BOARD_HEADER_SIZE = 12; // Size of the board header in bytes
    public static final byte BOARD_INDICATOR = 0x3F; // Value indicating that the data is board data
    // Client ID is stored as a long so 8 bytes must be reserved

    // Tile image resources
    public static final BufferedImage SPRITE_SHEET;
    static {
    try {
        SPRITE_SHEET = ImageIO.read(new File("resources/spritesheet.png"));
    } catch (IOException e) {
        throw new ExceptionInInitializerError("Failed to load sprite sheet: " + e.getMessage());
    }
}

    // Network communication constants
    public static final int HEARTBEAT_INTERVAL_MS = 5000; // Interval for sending heartbeat messages
    public static final int MAX_MESSAGE_SIZE = 2048; // Maximum size of a network message in bytes
    public static final int DEFAULT_SERVER_PORT = 12345; // Default server port
    public static final String DEFAULT_SERVER_ADDRESS = "localhost"; // Default server address
}
