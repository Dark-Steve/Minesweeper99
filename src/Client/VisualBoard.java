package Client;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import Utils.Util;

public class VisualBoard extends PlayerBoard {
    public int mainTileSize = 32; // Tile length in pixels
    public int backgroundTileSize = 16; // Tile length in pixels

    public JButton[][] visualTiles;

    public VisualBoard(byte[] data) {
        super(data);
        visualTiles = new JButton[width][height];
    }

    public VisualBoard(byte[] data, JButton[][] buttons) {
        super(data);
        visualTiles = new JButton[width][height];
    }


    public void updateMainBoard(VisualPlayer player) {
        // Display the main game board in a visual format
        Dimension d = player.getSize();
        int xOffset = (d.width - (this.width * mainTileSize)) / 2;
        int yOffset = (d.height - (this.height * mainTileSize)) / 2;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                byte tile = getTile(x, y);
                // Render the tile visually based on its state
                ImageIcon imageIcon = new ImageIcon(Util.getImage(tile)); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(mainTileSize, mainTileSize, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back

                // Tile isn't initialized yet
                if (visualTiles[x][y] == null ) {
                    // Create new button and add to player array
                    JButton visualTile = new JButton(imageIcon);
                    visualTiles[x][y] = visualTile;
                    int trueX = x * mainTileSize + xOffset;
                    int trueY = y * mainTileSize + yOffset;
                    visualTile.setBounds(trueX, trueY, mainTileSize, mainTileSize);
                    player.add(visualTile);
                } 
                // Tile needs to be updated
                else if (visualTiles[x][y].getIcon() != imageIcon) {
                    // Update existing button icon
                    JButton visualTile = visualTiles[x][y];
                    visualTile.setIcon(imageIcon);
                    visualTile.repaint();
                }
                // Else tile is the same, do nothing
            }
        }
    }

    public void displayBackgroundBoard(int boardNumber, VisualPlayer player) {
        // Display the background board for other players in a visual format
        Dimension d = player.getSize();
        int boardsPerRow = d.width / (this.width * backgroundTileSize);
        int xOffset = (boardNumber % boardsPerRow) * this.width * backgroundTileSize;
        int yOffset = (boardNumber / boardsPerRow) * this.height * backgroundTileSize;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                byte tile = getTile(x, y);
                // Render the tile visually based on its state
                int trueX = (x * backgroundTileSize) + (xOffset);
                int trueY = (y * backgroundTileSize) + (yOffset);
                VisualTile visualTile = new VisualTile(tile);
                visualTile.render(trueX, trueY, (VisualPlayer) player, backgroundTileSize);
            }
        }
    }
}
