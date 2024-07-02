package org.game.bouncing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class BounceVer2 extends JPanel implements ActionListener, KeyListener {
    private int ballX = 100; // Initial X position of the ball
    private int ballY = 100; // Initial Y position of the ball
    private int ballDiameter = 50;
    private int groundLevel = 500; // Y position of the ground (200 pixels from bottom)
    private int ballVelocityY = 0;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private int score = 0;
    private boolean gameOver = false;

    private Timer timer;
    private ArrayList<Enemy> enemies;
    private Random random;

    public BounceVer2() {
        int width = 1200;
        setPreferredSize(new Dimension(width, 700));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(20, this); // Update every 20 ms
        timer.start();
        enemies = new ArrayList<>();
        random = new Random();
        generateEnemies(width);
    }

    private void generateEnemies(int width) {
        int numberOfEnemies = random.nextInt(5) + 5;
        // Random number of enemies between 5 and 10
        int lastx = 150;
        for (int i = 0; i < numberOfEnemies; i++) {
            int enemyWidth = 50;
            int x;
            do {
//                int width = getWidth();
//                System.out.println("getWidth " + width);
                x = random.nextInt( width - 100) + lastx + 150;
                System.out.println("x=["+x+"]");
            } while (x > (150 + lastx)); // Ensure no overlap with ball start position
            lastx = x;
            int y = groundLevel - 50; // Place enemies above the ground
            enemies.add(new Enemy(x, y, 50, 50));
        }
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

        // Draw enemies
        g.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            drawHexagon(g, enemy.x, enemy.y, enemy.width, enemy.height);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);

        // Draw game over message
        if (gameOver) {
            g.drawString("Game Over", getWidth() / 2 - 50, getHeight() / 2);
        }
    }

    private void drawHexagon(Graphics g, int x, int y, int width, int height) {
        int[] xPoints = {
                x + width / 2, x + width, x + width, x + width / 2, x, x
        };
        int[] yPoints = {
                y, y + height / 4, y + 3 * height / 4, y + height, y + 3 * height / 4, y + height / 4
        };
        g.fillPolygon(xPoints, yPoints, 6);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            timer.stop();
            return;
        }

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

        // Check for collisions with enemies
        for (Enemy enemy : enemies) {
            if (ballX < enemy.x + enemy.width &&
                    ballX + ballDiameter > enemy.x &&
                    ballY < enemy.y + enemy.height &&
                    ballY + ballDiameter > enemy.y) {
                score -= 10;
                if (score < 0) {
                    gameOver = true;
                }
            } else if (ballY + ballDiameter >= groundLevel && ballY + ballDiameter - ballVelocityY < enemy.y) {
                score += 10;
            }
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

    private class Enemy {
        int x, y, width, height;

        Enemy(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
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
