package Minesweeper;

public class Game {
    private ServerBoard board;

    public Game(int width, int height, int bombCount) {
        board = new ServerBoard(width, height, bombCount);
    }

    public void play(int x, int y) {
        // Game logic to handle a move at (x, y)
        if (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
            board.getTile(x, y).reveal();
        }
    }

    public byte[] getBoardState() {
        return board.toByteArray();
    }
}
