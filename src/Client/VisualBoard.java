package Client;

import java.awt.Dimension;

public class VisualBoard extends PlayerBoard {
    public int mainTileSize = 16; // Tile length in pixels
    public int backgroundTileSize = 8; // Tile length in pixels
    public VisualBoard(byte[] data) {
        super(data);
    }

    public void displayMainBoard(VisualPlayer player) {
        // Display the main game board in a visual format
        Dimension d = player.getSize();
        int xOffset = (d.width - (this.width * mainTileSize)) / 2;
        int yOffset = (d.height - (this.height * mainTileSize)) / 2;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                byte tile = getTile(x, y);
                // Render the tile visually based on its state
                VisualTile visualTile = new VisualTile(tile);
                visualTile.render((x * mainTileSize) + (xOffset), (y * mainTileSize) + (yOffset), player, mainTileSize);
            }
        }
    }

    public void displayBackgroundBoard(int boardNumber, Player player) {
        // Display the background board for other players in a visual format
    }
}
