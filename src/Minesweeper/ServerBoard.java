package Minesweeper;

import java.util.Random;

import Utils.MagicNumbers;

// A simple representation of a Minesweeper board
public class ServerBoard extends Board {
    private Tile[][] cells;

    public ServerBoard(int width, int height, int bombCount) {
        super(width, height, bombCount);
        cells = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Tile();
            }
        }
        initialize();
    }

    private void initialize() {
        // Randomly place bombs
        Random rand = new Random();
        int placedBombs = 0;
        while (placedBombs < bombCount) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (!cells[x][y].isMine()) {
                cells[x][y].setMine(true);
                placedBombs++;
                // Update adjacent cells' mine counts
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height && !cells[nx][ny].isMine()) {
                            cells[nx][ny].setAdjacentMines(cells[nx][ny].getAdjacentMines() + 1);
                        }
                    }
                }
            }
        }
    }

    public byte[] toByteArray() {
        byte[] data = new byte[(width * height) + MagicNumbers.BOARD_HEADER_SIZE];
        data[MagicNumbers.WIDTH_INDEX] = (byte) width;
        data[MagicNumbers.HEIGHT_INDEX] = (byte) height;
        data[MagicNumbers.BOMB_COUNT_INDEX] = (byte) bombCount;
        // data[3] and data[4] can be reserved for future use
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x * height + y;
                Tile tile = cells[x][y];
                byte value = 0;
                if (tile.isRevealed()) {
                    value |= 0x10; // Revealed flag
                    if (tile.isMine()) {
                        value |= 0x20; // Mine flag
                    } else {
                        value |= (byte) tile.getAdjacentMines(); // Adjacent mine count
                    }
                } else if (tile.isFlagged()) {
                    value |= 0x40; // Flagged flag
                }
                data[index] = value;
            }
        }
        return data;
    }

    public Tile getTile(int x, int y) {
        return cells[x][y];
    }

    public void revealCell(int x, int y) {
        cells[x][y].reveal();
    }

    public void flagCell(int x, int y) {
        cells[x][y].flag();
    }
}
