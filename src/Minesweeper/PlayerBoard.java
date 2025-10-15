package Minesweeper;

public class PlayerBoard extends Board {
    private byte[][] cells;

    public PlayerBoard(byte[] data) {
        super(data[0], data[1], data[2]);
        cells = new byte[width][height];
        for (int i = 0; i < data.length - 5; i++) {
            int x = i / height;
            int y = i % height;
            cells[x][y] = data[i + 5];
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
        for (int i = 0; i < data.length; i++) {
            int x = i / height;
            int y = i % height;
            cells[x][y] = data[i];
        }
    }

    public byte[] toByteArray() {
        byte[] data = new byte[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x * height + y;
                data[index] = cells[x][y];
            }
        }
        return data;
    }

    public byte getTile(int x, int y) {
        return cells[x][y];
    }
}