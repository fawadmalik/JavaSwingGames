package org.game.bouncing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BouncingBallGame extends JPanel implements ActionListener, KeyListener {
    private int ballX = 100; // Initial X position of the ball
    private int ballY = 100; // Initial Y position of the ball
    private int ballDiameter = 50;
    private int groundLevel = 500; // Y position of the ground (200 pixels from bottom)
    private int ballVelocityY = 0;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private Timer timer;

    public BouncingBallGame() {
        setPreferredSize(new Dimension(1200, 700));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(20, this); // Update every 20 ms
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw ground
        g.setColor(Color.GREEN);
        g.fillRect(0, groundLevel, getWidth(), getHeight() - groundLevel);

        // Draw ball
        g.setColor(new Color(139, 69, 19)); // Brown color
        g.fillOval(ballX, ballY, ballDiameter, ballDiameter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Apply gravity
        ballVelocityY += 1;

        // Move ball
        ballY += ballVelocityY;

        // Check for bounce
        if (ballY + ballDiameter >= groundLevel) {
            ballY = groundLevel - ballDiameter;
            ballVelocityY = -20; // Bounce back up with initial velocity
        }

        // Move ball left or right
        if (moveLeft && ballX > 0) {
            ballX -= 5;
        }
        if (moveRight && ballX + ballDiameter < getWidth()) {
            ballX += 5;
        }

        // Repaint the panel
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }

        if (key == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }

        if (key == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bouncing Ball Game");
        BounceVer2 gamePanel = new BounceVer2();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
