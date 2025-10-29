package Client;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JButton;

public class VisualTile extends JButton {
    private byte tileData;

    public VisualTile(byte tileData, int x, int y, int size, boolean background) {
        // Initialize the visual tile based on tile data
        this.tileData = tileData;
        if (!background) {
            this.setBounds(x, y, size, size);
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
        }
        else {
            this.setBounds(-1, -1, 0, 0);
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
        }
    }

    public byte getTileData() {
        return tileData;
    }

    public void render(int x, int y, VisualPlayer player, int tileSize) {
        // Render the tile visually at position (x, y)
        player.drawTile(this, x, y, tileSize);
    }
}
