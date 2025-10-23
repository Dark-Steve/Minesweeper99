package Client;

public class VisualTile  {
    private byte tileData;

    public VisualTile(byte tileData) {
        // Initialize the visual tile based on tile data
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
