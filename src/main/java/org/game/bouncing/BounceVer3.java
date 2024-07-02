package org.game.bouncing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class BounceVer3 extends JPanel implements ActionListener, KeyListener {
    private int ballX = 100; // Initial X position of the ball
    private int ballY = 100; // Initial Y position of the ball
    private int ballDiameter = 50;
    private int groundLevel = 500; // Y position of the ground
    private int ballVelocityY = 0;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private int score = 100; // Initial score
    private boolean gameRunning = true;

    private ArrayList<RedEnemy> enemies;
    private Timer timer;
    private Random random;

    public BounceVer3() {
        int width = 1200;
        setPreferredSize(new Dimension(width, 700));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        enemies = new ArrayList<>();
        random = new Random();
        createEnemies(width); // Create 5 enemies initially

        timer = new Timer(20, this); // Update every 20 ms
        timer.start();
    }

    private void createEnemies(int width) {
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
            enemies.add(new RedEnemy(x, y, 50));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameRunning) {
            // Draw ground
            g.setColor(Color.GREEN);
            g.fillRect(0, groundLevel, getWidth(), getHeight() - groundLevel);

            // Draw ball
            g.setColor(new Color(139, 69, 19)); // Brown color
            g.fillOval(ballX, ballY, ballDiameter, ballDiameter);

            // Draw enemies
            for (RedEnemy enemy : enemies) {
                enemy.draw(g);
            }

            // Draw score
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            // Game over screen
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
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
            checkCollisions();

            // Repaint the panel
            repaint();
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < enemies.size(); i++) {
            RedEnemy enemy = enemies.get(i);
            if (enemy.isHit(ballX, ballY, ballDiameter)) {
                score -= 10; // Lose points if hit
                enemies.remove(i);
                i--; // Adjust index after removal
            } else if (enemy.isPassedOver(ballX, ballY, ballDiameter)) {
                score += 10; // Gain points if passed over
//                enemies.remove(i);
//                i--; // Adjust index after removal
            }

            // Check for game over condition
            if (score < 0) {
                gameRunning = false;
                break;
            }
        }
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
        BounceVer3 gamePanel = new BounceVer3();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class RedEnemy {
    private int x;
    private int y;
    private int size;
    private boolean passedOver = false;

    public RedEnemy(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        int[] xPoints = { x, x + size / 2, x + size, x + size, x + size / 2, x };
        int[] yPoints = { y + size / 2, y, y + size / 2, y + size, y + size + size / 2, y + size };
        g.fillPolygon(xPoints, yPoints, 6);
    }

    public boolean isHit(int ballX, int ballY, int ballDiameter) {
        int ballCenterX = ballX + ballDiameter / 2;
        int ballCenterY = ballY + ballDiameter / 2;
        return (ballCenterX > x && ballCenterX < x + size) && (ballCenterY > y && ballCenterY < y + size);
    }

    public boolean isPassedOver(int ballX, int ballY, int ballDiameter) {
        if (ballY + ballDiameter < y && ballX > x + size) {
            if (!passedOver) {
                passedOver = true;
                return true;
            }
        }
        return false;
    }
}
