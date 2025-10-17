package Minesweeper;

import Utils.MagicNumbers;

// A class representing a single tile in the Minesweeper game
public class Tile extends Cell {
    private boolean isRevealed;
    private boolean isFlagged;
    private boolean isMine;
    private int adjacentMines;

    public Tile() {
        isRevealed = false;
        isFlagged = false;
        isMine = false;
        adjacentMines = 0;
    }

    public byte toByte() {
        byte value = 0;
        if (isRevealed) {
            value |= 0x10; // Revealed flag
            if (isMine) {
                value |= MagicNumbers.TILE_BOMB; // Mine flag
            } else {
                value |= (byte) adjacentMines; // Adjacent mine count
            }
        } else if (isFlagged) {
            value |= MagicNumbers.TILE_FLAG; // Flagged flag
        }
        return value;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isMine() {
        return isMine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setAdjacentMines(int count) {
        adjacentMines = count;
    }

    public void reveal() {
        isRevealed = true;
    }

    public void flag() {
        isFlagged = true;
    }

    public void unflag() {
        isFlagged = false;
    }

    public void toggleFlag() {
        if (isFlagged) {
            unflag();
        } else {
            flag();
        }
    }
}
