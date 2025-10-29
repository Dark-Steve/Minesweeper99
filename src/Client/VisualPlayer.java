package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Utils.MagicNumbers;
import Utils.Util;

// Representation of a player with GUI capabilities
public class VisualPlayer extends Player implements ActionListener {
    // protected Thread receiver;
    // protected Scanner scanner;

    // protected PlayerBoard board;

    // protected HashMap<Long, PlayerBoard> otherBoards;

    JButton[][] visualTiles;

    private DrawingPanel drawingPanel;
    private JFrame frame;

    /**
     * DrawingPanel now acts as a plain JPanel that hosts the interactive buttons
     * directly. We rely on Swing's normal double-buffering and component painting
     * so the buttons remain fully interactive (receive mouse events, focus, etc.)
     * and avoid the flicker caused by mixing offscreen-painted components with
     * components added to the hierarchy.
     */
    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            // We'll position tiles absolutely (setBounds) so use null layout here.
            setLayout(null);
            setBackground(Color.BLACK);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // If you want any custom background drawing, do it here.
            // Currently the panel background color fills the area.
        }
    }

    /**
     * Add a button to the visual area so it is interactive.
     * The caller is responsible for setting the button bounds (location/size)
     * before or after calling add. We make sure the action listener is attached
     * and request revalidation/repaint.
     */
    public void add(JButton button) {
        // Ensure UI changes happen on the EDT
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> add(button));
            return;
        }

        // Keep the button interactive: add to the panel's component hierarchy.
        button.addActionListener(this);
        drawingPanel.add(button);
        drawingPanel.revalidate();
        drawingPanel.repaint();
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
        // Ensure UI changes happen on the EDT
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> drawTile(tile, x, y, tileSize));
            return;
        }
        ImageIcon imageIcon = new ImageIcon(Util.getImage(tile.getTileData())); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(tileSize, tileSize, java.awt.Image.SCALE_FAST); // scale it the smooth
                                                                                                 // way
        imageIcon = new ImageIcon(newimg); // transform it back
        drawingPanel.add(
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

    /**
     * Display the whole game by letting boards populate the visual area.
     * We remove all components first so boards can add fresh buttons (tiles)
     * via VisualPlayer.add(...). This keeps buttons in the normal component
     * hierarchy so they remain interactive.
     */
    public void displayGame() {
        // Ensure we run on EDT
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::displayGame);
            return;
        }

        // Remove any existing components (old tiles) so boards can re-add them.
        drawingPanel.removeAll();

        VisualBoard visualBoard = (VisualBoard) board;
        // Let the board repopulate the UI by calling VisualPlayer.add(...) as needed.
        visualBoard.updateMainBoard(this);
        visualTiles = visualBoard.visualTiles;
        int boardNumber = 0;
        for (PlayerBoard otherBoard : otherBoards.values()) {
            VisualBoard vb = (VisualBoard) otherBoard;
            vb.displayBackgroundBoard(boardNumber, this);
            boardNumber++;
        }
    }

    @Override
    public void displayBoard() {
        // Forward to the game display routine
        displayGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: handle button actions here. This is called for each interactive
        // tile/button.
        // Example:
        // Object src = e.getSource();
        // if (src instanceof VisualTile) { ... }
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}