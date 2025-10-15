package Minesweeper;

import java.util.Random;

public class Board {
    protected int bombCount;
    protected int width;
    protected int height;

    public Board(int width, int height, int bombCount) {
        this.bombCount = bombCount;
        this.width = width;
        this.height = height;
    }

    public int getBombCount() {
        return bombCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
