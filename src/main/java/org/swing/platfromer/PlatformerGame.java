package org.swing.platfromer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PlatformerGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int playerX, playerY;
    private int playerVelocityX, playerVelocityY;
    private boolean isJumping;
    private boolean isOnGround;

    private final int gravity = 1; // Gravity force
    private final int jumpStrength = -20; // Initial jump velocity
    private final int maxFallSpeed = 10; // Terminal velocity

    private ArrayList<Platform> platforms;

    public PlatformerGame() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        // Initialize player position
        playerX = 400;
        playerY = 500;
        playerVelocityX = 0;
        playerVelocityY = 0;
        isJumping = false;
        isOnGround = true;

        // Initialize platforms
        platforms = new ArrayList<>();
        platforms.add(new Platform(300, 400, 200, 20));
        platforms.add(new Platform(100, 300, 150, 20));
        platforms.add(new Platform(500, 200, 200, 20));

        // Timer to repaint the screen every 16ms (~60 FPS)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, 50, 50); // Player as a simple square

        // Draw ground (a simple line at y=550)
        g.setColor(Color.WHITE);
        g.drawLine(0, 550, 800, 550);

        // Draw platforms
        g.setColor(Color.GREEN);
        for (Platform platform : platforms) {
            g.fillRect(platform.x, platform.y, platform.width, platform.height);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update player position
        playerX += playerVelocityX;
        playerY += playerVelocityY;

        // Apply gravity
        if (!isOnGround) {
            playerVelocityY += gravity;
            if (playerVelocityY > maxFallSpeed) {
                playerVelocityY = maxFallSpeed;
            }
        }

        // Check for collision with the ground
        if (playerY >= 500) { // Assuming ground is at y = 500
            playerY = 500;
            playerVelocityY = 0;
            isJumping = false;
            isOnGround = true;
        } else {
            isOnGround = false;
        }

        // Check for collision with platforms
        for (Platform platform : platforms) {
            if (playerX + 50 > platform.x && playerX < platform.x + platform.width &&
                    playerY + 50 > platform.y && playerY + 50 <= platform.y + playerVelocityY) {
                playerY = platform.y - 50;
                playerVelocityY = 0;
                isJumping = false;
                isOnGround = true;
                break;
            }
        }

        // Repaint the screen
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                playerVelocityX = -5;
                break;
            case KeyEvent.VK_RIGHT:
                playerVelocityX = 5;
                break;
            case KeyEvent.VK_UP:
                if (!isJumping && isOnGround) {
                    playerVelocityY = jumpStrength;
                    isJumping = true;
                    isOnGround = false;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                playerVelocityX = 0;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Platformer Game");
        PlatformerGame game = new PlatformerGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
