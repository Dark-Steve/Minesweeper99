package Minesweeper;

import Utils.MagicNumbers;

public class PlayerBoard extends Board {
    protected byte[][] cells;

    public PlayerBoard(byte[] data) {
        super(data[0], data[1], data[2]);
        cells = new byte[width][height];
        for (int i = 0; i < data.length - MagicNumbers.BOARD_HEADER_SIZE; i++) {
            int x = i / height;
            int y = i % height;
            cells[x][y] = data[i + MagicNumbers.BOARD_HEADER_SIZE];
        }
    }

    public PlayerBoard(int width, int height, int bombCount, byte[] data) {
        super(width, height, bombCount);
        cells = new byte[width][height];
        for (int i = 0; i < data.length; i++) {
            int x = i / height;
            int y = i % height;
            cells[x][y] = data[i];
        }
    }

    public void update(byte[] data) {
        for (int i = MagicNumbers.BOARD_HEADER_SIZE; i < data.length; i++) {
            int x = i / height;
            int y = i % height;
            cells[x][y] = data[i];
        }
    }

    public byte[] toByteArray() {
        byte[] data = new byte[width * height + MagicNumbers.BOARD_HEADER_SIZE];
        data[MagicNumbers.WIDTH_INDEX] = (byte) width;
        data[MagicNumbers.HEIGHT_INDEX] = (byte) height;
        data[MagicNumbers.BOMB_COUNT_INDEX] = (byte) bombCount;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x * height + y;
                data[index + MagicNumbers.BOARD_HEADER_SIZE] = cells[x][y];
            }
        }
        return data;
    }

    public byte getTile(int x, int y) {
        return cells[x][y];
    }
}