package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Utils.MagicNumbers;
import Utils.Util;

// Representation of a player with GUI capabilities
public class VisualPlayer extends Player implements ActionListener{
    // protected Thread receiver;
    // protected Scanner scanner;

    // protected PlayerBoard board;

    // protected HashMap<Long, PlayerBoard> otherBoards;

    JButton[][]visualTiles;


    private DrawingPanel drawingPanel;
    private JFrame frame;

    private class DrawingPanel extends JPanel {
        private Image offscreenImage;
        private Graphics offscreenGraphics;

        @Override
        public void repaint() {
            update(offscreenGraphics);
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
            if (offscreenImage == null || getWidth() != offscreenImage.getWidth(null)
                    || getHeight() != offscreenImage.getHeight(null)) {
                offscreenImage = createImage(getWidth(), getHeight());
                offscreenGraphics = offscreenImage.getGraphics();
            }
            offscreenGraphics.setColor(Color.BLACK);
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
        }

        public void paintTileOffScreen(VisualTile tile, int x, int y, int tileSize) {

            // Draw a tile onto the offscreen graphics at position (x, y)
            byte tileData = tile.getTileData();

            
        }
    }

    public void add(JButton button) {
        drawingPanel.add(button);
        button.addActionListener(this);
        button.repaint();
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
        frame.add(tile);
        tile.addActionListener(this);
    }

    public Dimension getSize() {
        return drawingPanel.getSize();
    }

    @Override
    PlayerBoard createNewBoard(byte[] data) {
        // TODO Auto-generated method stub
        if (visualTiles != null) {
            return new VisualBoard(data, visualTiles);
        }
        return new VisualBoard(data);
    }

    public void displayGame() {
        Graphics g = drawingPanel.getGraphics();
        drawingPanel.clearOffScreen();

        VisualBoard visualBoard = (VisualBoard) board;
        visualBoard.updateMainBoard(this);
        visualTiles = visualBoard.visualTiles;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
