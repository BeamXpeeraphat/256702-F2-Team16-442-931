package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class MainLevel extends JPanel {
    protected MainGameWindow mainGameWindow;
    protected Inventory inventory;
    protected JLabel coinLabel;
    protected JLabel timeLabel;
    private Image backgroundImage;
    protected Timer gameTimer;
    protected int gameTime = 0;
    protected JLabel motorcycleLabel;
    protected int motorcycleX = 50;
    protected int motorcycleY = 700;
    protected int motorcycleSpeed = 5;
    protected int jumpVelocity = -15;
    protected int gravity = 1;
    protected int motorcycleVY = 0;
    protected boolean isJumping = false;
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    public MainLevel(MainGameWindow mainGameWindow) {
        this.mainGameWindow = mainGameWindow;
        this.inventory = mainGameWindow.getInventory();
        this.inventory.setMainLevel(this);
        setLayout(null);
        loadBackgroundImage();
        initializeUI();
        initializeMotorcycle();
        setupControls();
        startTimer();
    }

    private void loadBackgroundImage() {
        ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/backgroundoflevelone.jpg"));
        if (bgIcon.getImage() == null) {
            System.err.println("Failed to load backgroundoflevelone.jpg, using white background");
            backgroundImage = null;
        } else {
            backgroundImage = bgIcon.getImage();
            System.out.println("Loaded background: backgroundoflevelone.jpg");
        }
    }

    protected void initializeUI() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBounds(0, 0, getWidth(), 50);
        statusPanel.setOpaque(false);
        coinLabel = new JLabel("Coins: " + (inventory != null ? inventory.getCoins() : 0));
        timeLabel = new JLabel("Time: 00:00");
        coinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(coinLabel);
        statusPanel.add(timeLabel);
        add(statusPanel);

        revalidate();
        repaint();
    }

    protected void initializeMotorcycle() {
        String selectedMotorcycle = inventory.getSelectedMotorcycle();
        if (selectedMotorcycle != null) {
            ImageIcon motorcycleIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + selectedMotorcycle));
            if (motorcycleIcon.getImage() != null) {
                Image scaledImage = motorcycleIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                motorcycleLabel = new JLabel(new ImageIcon(scaledImage));
                motorcycleLabel.setBounds(motorcycleX, motorcycleY, 100, 100);
                add(motorcycleLabel);
                System.out.println("Initialized motorcycle: " + selectedMotorcycle);
            } else {
                System.err.println("Failed to load motorcycle image: " + selectedMotorcycle + " in MainLevel");
            }
        } else {
            System.err.println("No selected motorcycle available in Inventory!");
        }
    }

    protected void setupControls() {
        setFocusable(true);
        requestFocusInWindow();
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("MainLevel gained focus");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("MainLevel lost focus");
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isFocusOwner()) {
                    System.out.println("Key pressed but MainLevel is not focused! Attempting to regain focus...");
                    regainFocus();
                    return;
                }
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W && !isJumping) {
                    wPressed = true;
                }
                if (key == KeyEvent.VK_A) aPressed = true;
                if (key == KeyEvent.VK_D) dPressed = true;
                updateMotorcycle();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!isFocusOwner()) return;
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) wPressed = false;
                if (key == KeyEvent.VK_A) aPressed = false;
                if (key == KeyEvent.VK_D) dPressed = false;
                updateMotorcycle();
            }
        });

        Timer movementTimer = new Timer(20, e -> {
            updateMotorcycle();
            repaint();
        });
        movementTimer.start();
    }

    private void regainFocus() {
        SwingUtilities.invokeLater(() -> {
            setFocusable(true);
            requestFocusInWindow();
            mainGameWindow.getFrame().requestFocus();
            if (isFocusOwner()) {
                System.out.println("Focus successfully regained by MainLevel");
            } else {
                System.out.println("Failed to regain focus for MainLevel");
            }
        });
    }

    public void updateMotorcycle() {
        if (aPressed && motorcycleX > 0) {
            motorcycleX -= motorcycleSpeed;
        }
        if (dPressed && motorcycleX < getWidth() - 100) {
            motorcycleX += motorcycleSpeed;
        }
        if (wPressed && !isJumping) {
            motorcycleVY = jumpVelocity;
            isJumping = true;
            wPressed = false;
        }
        if (isJumping) {
            motorcycleY += motorcycleVY;
            motorcycleVY += gravity;
            if (motorcycleY >= 700) { // เปลี่ยนพื้นดินจาก 500 เป็น 700
                motorcycleY = 700;
                motorcycleVY = 0;
                isJumping = false;
            }
        }
        if (motorcycleLabel != null) {
            motorcycleLabel.setLocation(motorcycleX, motorcycleY);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    protected void startTimer() {
        gameTimer = new Timer(1000, e -> {
            gameTime++;
            int minutes = gameTime / 60;
            int seconds = gameTime % 60;
            timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        });
        gameTimer.start();
    }

    protected void stopTimer() {
        if (gameTimer != null) gameTimer.stop();
    }

    protected void showSettings() {
        stopTimer();
        Setting settingDialog = new Setting(this);
        settingDialog.setVisible(true);
    }

    protected void resumeGame() {
        startTimer();
        regainFocus();
    }

    protected void restartLevel() {
        stopTimer();
        gameTime = 0;
        timeLabel.setText("Time: 00:00");
        motorcycleX = 50;
        motorcycleY = 500;
        motorcycleVY = 0;
        isJumping = false;
        wPressed = false;
        aPressed = false;
        dPressed = false;
        if (motorcycleLabel != null) motorcycleLabel.setLocation(motorcycleX, motorcycleY);
        startTimer();
        startLevel();
    }

    protected void showMotorcycleSelection() {
        JOptionPane.showMessageDialog(this, "Motorcycle selection not implemented yet!");
    }

    protected void returnToLevelGame() {
        stopTimer();
        mainGameWindow.showPanel("LevelGame");
    }

    public void notifyMotorcycleChanged() {
        System.out.println("MainLevel notified of motorcycle change, updating...");
        SwingUtilities.invokeLater(this::updateMotorcycleImage);
    }

    public void updateMotorcycleImage() {
        if (motorcycleLabel != null) {
            remove(motorcycleLabel);
        }
        String selectedMotorcycle = inventory.getSelectedMotorcycle();
        if (selectedMotorcycle != null) {
            ImageIcon motorcycleIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + selectedMotorcycle));
            if (motorcycleIcon.getImage() != null) {
                Image scaledImage = motorcycleIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                motorcycleLabel = new JLabel(new ImageIcon(scaledImage));
                motorcycleLabel.setBounds(motorcycleX, motorcycleY, 100, 100);
                add(motorcycleLabel);
                revalidate();
                repaint();
                System.out.println("Motorcycle updated to: " + selectedMotorcycle);
            } else {
                System.err.println("Failed to load motorcycle image: " + selectedMotorcycle);
            }
        } else {
            System.err.println("No selected motorcycle to update!");
        }
    }

    public abstract void startLevel();
}