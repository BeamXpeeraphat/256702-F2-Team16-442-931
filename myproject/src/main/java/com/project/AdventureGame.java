package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class AdventureGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX = 50;
    private int playerY = 250;
    private int speed = 5;
    private int score = 0;
    private ArrayList<Rectangle> obstacles;
    private Random rand;

    public AdventureGame() {
        timer = new Timer(20, this);
        obstacles = new ArrayList<>();
        rand = new Random();
        this.setFocusable(true);
        this.addKeyListener(this);
        generateObstacles();
        timer.start();
    }

    private void generateObstacles() {
        for (int i = 0; i < 5; i++) {
            int x = 300 + i * 200;
            int y = rand.nextInt(400);
            obstacles.add(new Rectangle(x, y, 50, 50));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, 50, 50);

        g.setColor(Color.RED);
        for (Rectangle obs : obstacles) {
            g.fillRect(obs.x, obs.y, obs.width, obs.height);
        }
        
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Rectangle obs : obstacles) {
            obs.x -= speed;
            if (obs.x + obs.width < 0) {
                obs.x = 600;
                obs.y = rand.nextInt(400);
                score++;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && playerY > 0) {
            playerY -= 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && playerY < getHeight() - 50) {
            playerY += 20;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Adventure Game");
        AdventureGame game = new AdventureGame();
        frame.add(game);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}