package Minesweeper;

public class Game {
    private ServerBoard board;

    public Game(int width, int height, int bombCount) {
        board = new ServerBoard(width, height, bombCount);
    }

    public void reveal(int x, int y) {
        // Game logic to handle a move at (x, y)
        board.getTile(x, y).reveal();
    }

    public void flag(int x, int y) {   
        // Game logic to handle flagging a tile at (x, y)
        board.getTile(x, y).toggleFlag();
    }

    public byte[] toByteArray(long clientId) {
        return board.toByteArray(clientId);
    }
}
