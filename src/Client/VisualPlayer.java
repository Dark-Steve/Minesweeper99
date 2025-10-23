package Client;

import java.awt.*;
import javax.swing.*;

import Utils.MagicNumbers;
import Utils.Util;

// Representation of a player with GUI capabilities
public class VisualPlayer extends Player {
    // protected Thread receiver;
    // protected Scanner scanner;

    // protected PlayerBoard board;

    // protected HashMap<Long, PlayerBoard> otherBoards;

    private DrawingPanel drawingPanel;
    private JFrame frame;

    private class DrawingPanel extends JPanel {
        private Image offscreenImage;
        private Graphics offscreenGraphics;

        @Override
        public void repaint() {

        }

        public void update(Graphics g) {
            if (offscreenImage == null || getWidth() != offscreenImage.getWidth(null)
                    || getHeight() != offscreenImage.getHeight(null)) {
                return;
            }

            // Draw the offscreen image onto the screen
            g.drawImage(offscreenImage, 0, 0, null);
        }

        public void clearOffScreen() {
            if (offscreenImage == null) {
                offscreenImage = createImage(getWidth(), getHeight());
                offscreenGraphics = offscreenImage.getGraphics();
            }
            offscreenGraphics.setColor(Color.BLACK);
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
        }

        public void paintTileOffScreen(VisualTile tile, int x, int y, int tileSize) {

            // Draw a tile onto the offscreen graphics at position (x, y)
            byte tileData = tile.getTileData();

            if (tileData == MagicNumbers.TILE_BOMB) {
                offscreenGraphics.drawImage(Util.getBombImage(), x, y, this); // Bomb
            } else if (tileData == MagicNumbers.TILE_FLAG) {
                offscreenGraphics.drawImage(Util.getFlagImage(), x, y, this); // Flag
            } else if (tileData == MagicNumbers.TILE_QUESTION) {
                offscreenGraphics.drawImage(Util.getQuestionImage(), x, y, this); // Question
            } else if (tileData == MagicNumbers.TILE_DEPRESSED_QUESTION) {
                offscreenGraphics.drawImage(Util.getDepressedQuestionImage(), x, y, this); // Depressed Question
            } else if (tileData == MagicNumbers.TILE_EXPLODED_BOMB) {
                offscreenGraphics.drawImage(Util.getExplodedBombImage(), x, y, this); // Exploded Bomb
            } else if (tileData == MagicNumbers.TILE_WRONG_FLAG) {
                offscreenGraphics.drawImage(Util.getWrongFlagImage(), x, y, this); // Wrong Flag
            } else if (tileData != MagicNumbers.TILE_HIDDEN) {
                offscreenGraphics.drawImage(Util.getNumberImage(tileData & MagicNumbers.TILE_NUMBER_MASK), x, y, this); // Number
            } else {
                offscreenGraphics.drawImage(Util.getHiddenImage(), x, y, this); // Hidden
            }
        }
    }

    public VisualPlayer(String serverAddress, int serverPort, int id) throws Exception {
        super(serverAddress, serverPort, id);
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(800, 600));
        frame = new JFrame("Minesweeper99 - Player " + id);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(drawingPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void drawTile(VisualTile tile, int x, int y, int tileSize) {
        drawingPanel.paintTileOffScreen(tile, x, y, tileSize);
    }

    public Dimension getSize() {
        return drawingPanel.getSize();
    }

    @Override
    PlayerBoard createNewBoard(byte[] data) {
        // TODO Auto-generated method stub
        return new VisualBoard(data);
    }

    public void displayGame() {
        Graphics g = drawingPanel.getGraphics();
        drawingPanel.clearOffScreen();

        VisualBoard visualBoard = (VisualBoard) board;
        visualBoard.displayMainBoard(this);

        int boardNumber = 0;
        for (PlayerBoard otherBoard : otherBoards.values()) {
            VisualBoard vb = (VisualBoard) otherBoard;
            vb.displayBackgroundBoard(boardNumber, this);
            boardNumber++;
        }

        drawingPanel.update(g);
    }

    @Override
    public void displayBoard() {
        // TODO Auto-generated method stub
        displayGame();
    }
}
