package org.swing.platfromer;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("2D Platformer Game");
        setSize(1600, 900); // Screen width is 1600, height can be 900 for a 16:9 ratio
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        GamePanel panel = new GamePanel();
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::new);
    }
}

class GamePanel extends JPanel {
    public GamePanel() {
        setBackground(Color.CYAN); // Background color
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player character (e.g., a rectangle)
        g.setColor(Color.RED);
        g.fillRect(100, 700, 50, 50); // Position (x, y) and size (width, height)

        // Draw a platform (e.g., a rectangle)
        g.setColor(Color.GREEN);
        g.fillRect(300, 800, 300, 20); // Position (x, y) and size (width, height)
    }
}
