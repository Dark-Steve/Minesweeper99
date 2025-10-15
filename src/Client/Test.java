package Client;

import Minesweeper.PlayerBoard;
import Utils.MagicNumbers;

// Imma just use this for whatever
public class Test {
    PlayerBoard testBoard;
    public static void main(String[] args) {
        Test t = new Test();
        byte[] boardData = {
            5, 5, 5, // width, height, bombCount
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x10, 0x10, 0x10, 0x00,
            0x00, 0x10, 0x30, 0x10, 0x00,
            0x00, 0x10, 0x15, 0x16, 0x40,
            0x00, 0x00, 0x00, 0x00, 0x00
        };
        t.testBoard = new PlayerBoard(boardData);
        t.displayBoard();
    }

    private void displayTile(byte tile) {
        if (tile == MagicNumbers.TILE_BOMB) {
            System.out.print("[*]"); // Bomb
        } else if (tile == MagicNumbers.TILE_FLAG) {
            System.out.print("[F]"); // Flag
        } else if (tile != 0) {
            System.out.print("[" + (tile & MagicNumbers.TILE_NUMBER_MASK) + "]"); // Number
        } else {
            System.out.print("[ ]"); // Empty
        }
    }

    public void displayBoard() {
        if (testBoard != null) {
            for (int y = 0; y < testBoard.getHeight(); y++) {
                for (int x = 0; x < testBoard.getWidth(); x++) {
                    displayTile(testBoard.getTile(x, y));
                }
                System.out.println();
            }
        } else {
            System.out.println("Board not initialized.");
        }
    }
}
