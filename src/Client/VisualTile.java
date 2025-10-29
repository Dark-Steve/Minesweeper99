package Client;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JButton;

public class VisualTile {
    private byte tileData;

    public VisualTile(byte tileData) {
        this.tileData = tileData;
    }

    public byte getTileData() {
        return tileData;
    }

    public void render(int x, int y, VisualPlayer player, int tileSize) {
        // Render the tile visually at position (x, y)
        player.drawTile(this, x, y, tileSize);
    }
}
