package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public abstract class MainLevel extends JPanel {
    protected MainGameWindow mainGameWindow;
    protected Inventory inventory;
    protected JLabel coinLabel;
    protected JLabel timeLabel;
    private Image backgroundImage;
    protected Timer gameTimer;
    protected int gameTime = 0;
    protected int levelCoins = 0;
    protected JLabel motorcycleLabel;
    protected int motorcycleX = 50;
    protected int motorcycleY = 700;
    protected int motorcycleSpeed = 5;
    protected int jumpVelocity = -15;
    protected int gravity = 1;
    protected int motorcycleVY = 0;
    protected boolean isJumping = false;
    protected boolean wPressed = false;
    protected boolean aPressed = false;
    protected boolean dPressed = false;
    protected boolean isGameActive = false;
    protected ArrayList<JLabel> coins;
    protected JLabel goalLabel;
    protected boolean hasWon = false;

    public MainLevel(MainGameWindow mainGameWindow) {
        this.mainGameWindow = mainGameWindow;
        this.inventory = mainGameWindow.getInventory();
        this.inventory.setMainLevel(this);
        setLayout(null);
        coins = new ArrayList<>();
        loadBackgroundImage();
        initializeUI();
        initializeMotorcycle();
        setupControls();
        resetLevelStats();
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
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        statusPanel.setOpaque(false);
        statusPanel.setBounds(0, 10, getWidth(), 50);
        coinLabel = new JLabel("Coins: " + levelCoins);
        timeLabel = new JLabel("Time: 00:00");
        coinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(coinLabel);
        statusPanel.add(timeLabel);
        add(statusPanel);
        System.out.println("Initialized UI: coinLabel = " + coinLabel.getText() + ", timeLabel = " + timeLabel.getText());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                statusPanel.setBounds(0, 10, getWidth(), 50);
            }
        });

        revalidate();
        repaint();
    }

    protected void initializeMotorcycle() {
        String selectedMotorcycle = inventory.getSelectedMotorcycle();
        if (selectedMotorcycle == null) {
            System.err.println("No selected motorcycle available in Inventory! Using default motorcycle.");
            selectedMotorcycle = "motorcycle1.png";
        }
        ImageIcon motorcycleIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + selectedMotorcycle));
        if (motorcycleIcon.getImage() == null) {
            System.err.println("Failed to load motorcycle image: " + selectedMotorcycle + ". Check resource path.");
            motorcycleLabel = new JLabel("Motorcycle");
            motorcycleLabel.setBounds(motorcycleX, motorcycleY, 100, 100);
        } else {
            Image scaledImage = motorcycleIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            motorcycleLabel = new JLabel(new ImageIcon(scaledImage));
            motorcycleLabel.setBounds(motorcycleX, motorcycleY, 100, 100);
            System.out.println("Initialized motorcycle: " + selectedMotorcycle);
        }
        add(motorcycleLabel);
    }

    protected void setupControls() {
        setFocusable(true);
        requestFocusInWindow();
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("MainLevel gained focus");
                if (isGameActive && (gameTimer == null || !gameTimer.isRunning())) {
                    startTimer();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("MainLevel lost focus");
                stopTimer();
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

    protected void regainFocus() {
        SwingUtilities.invokeLater(() -> {
            setFocusable(true);
            requestFocusInWindow();
            mainGameWindow.getFrame().requestFocus();
            if (isFocusOwner()) {
                System.out.println("Focus successfully regained by MainLevel");
            } else {
                System.out.println("Failed to regain focus for MainLevel, forcing focus...");
                mainGameWindow.getFrame().toFront();
                mainGameWindow.getFrame().requestFocus();
                requestFocusInWindow();
            }
        });
    }

    public void updateMotorcycle() {
        if (!isGameActive) return;
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
            if (motorcycleY >= 700) {
                motorcycleY = 700;
                motorcycleVY = 0;
                isJumping = false;
            }
        }
        if (motorcycleLabel != null) {
            motorcycleLabel.setLocation(motorcycleX, motorcycleY);
            checkCoinCollision();
            checkGoalCollision();
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
        if (!isGameActive) {
            System.out.println("Cannot start timer: Game is not active");
            return;
        }
        stopTimer();
        gameTimer = new Timer(1000, e -> {
            gameTime++;
            int minutes = gameTime / 60;
            int seconds = gameTime % 60;
            timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
            timeLabel.revalidate();
            timeLabel.repaint();
            System.out.println("Timer tick: " + timeLabel.getText());
        });
        gameTimer.start();
        System.out.println("Timer started");
    }

    protected void stopTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
            System.out.println("Timer stopped");
        }
    }

    protected void showSettings() {
        stopTimer();
        isGameActive = false;
        Setting settingDialog = new Setting(this);
        settingDialog.setVisible(true);
    }

    protected void resumeGame() {
        System.out.println("Resuming game...");
        isGameActive = true;
        startTimer();
        regainFocus();
    }

    protected void restartLevel() {
        updateInventoryCoins();
        resetLevelStats();
        stopTimer();
        motorcycleX = 50;
        motorcycleY = 700;
        motorcycleVY = 0;
        isJumping = false;
        wPressed = false;
        aPressed = false;
        dPressed = false;
        if (motorcycleLabel != null) motorcycleLabel.setLocation(motorcycleX, motorcycleY);
        coins.clear();
        isGameActive = true;
        startTimer();
        startLevel();
        regainFocus(); // เพิ่มการคืนโฟกัส
        repaint();
    }

    protected void showMotorcycleSelection() {
        JOptionPane.showMessageDialog(this, "Motorcycle selection not implemented yet!");
    }

    protected void returnToLevelGame() {
        updateInventoryCoins();
        stopTimer();
        isGameActive = false;
        if (hasWon) {
            restartLevel();
            hasWon = false;
        }
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
        if (selectedMotorcycle == null) {
            System.err.println("No selected motorcycle available in Inventory! Using default motorcycle.");
            selectedMotorcycle = "motorcycle1.png";
        }
        ImageIcon motorcycleIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + selectedMotorcycle));
        if (motorcycleIcon.getImage() == null) {
            System.err.println("Failed to load motorcycle image: " + selectedMotorcycle);
            motorcycleLabel = new JLabel("Motorcycle");
            motorcycleLabel.setBounds(motorcycleX, motorcycleY, 100, 100);
        } else {
            Image scaledImage = motorcycleIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            motorcycleLabel = new JLabel(new ImageIcon(scaledImage));
            motorcycleLabel.setBounds(motorcycleX, motorcycleY, 100, 100);
            System.out.println("Motorcycle updated to: " + selectedMotorcycle);
        }
        add(motorcycleLabel);
        revalidate();
        repaint();
    }

    protected void resetLevelStats() {
        gameTime = 0;
        levelCoins = 0;
        timeLabel.setText("Time: 00:00");
        coinLabel.setText("Coins: " + levelCoins);
        repaint();
    }

    public void addCoins(int amount) {
        levelCoins += amount;
        coinLabel.setText("Coins: " + levelCoins);
        repaint();
        System.out.println("Added " + amount + " coins, total now: " + levelCoins);
    }

    protected void updateInventoryCoins() {
        if (levelCoins > 0) {
            inventory.addCoins(levelCoins);
            System.out.println("Added " + levelCoins + " coins to Inventory. Total now: " + inventory.getCoins());
        }
    }

    public int getLevelCoins() {
        return levelCoins;
    }

    public int getGameTime() {
        return gameTime;
    }

    protected void initializeCoins(int[] xPositions, int yPosition) {
        ImageIcon coinIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/coin.png"));
        if (coinIcon.getImage() == null) {
            System.err.println("Failed to load coin.png");
            return;
        }
        Image scaledCoin = coinIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        coinIcon = new ImageIcon(scaledCoin);

        for (int x : xPositions) {
            JLabel coinLabel = new JLabel(coinIcon);
            coinLabel.setBounds(x, yPosition, 30, 30);
            coins.add(coinLabel);
            add(coinLabel);
        }
        revalidate();
        repaint();
    }

    protected void checkCoinCollision() {
        if (motorcycleLabel == null) return;
        Rectangle motorcycleBounds = new Rectangle(motorcycleX, motorcycleY, 100, 100);
        for (int i = coins.size() - 1; i >= 0; i--) {
            JLabel coin = coins.get(i);
            Rectangle coinBounds = coin.getBounds();
            if (motorcycleBounds.intersects(coinBounds)) {
                coins.remove(i);
                remove(coin);
                addCoins(10);
                System.out.println("Coin collected! Total coins: " + levelCoins);
                revalidate();
                repaint();
            }
        }
    }

    protected void checkGoalCollision() {
        if (goalLabel == null || motorcycleLabel == null) return;
        Rectangle motorcycleBounds = new Rectangle(motorcycleX, motorcycleY, 100, 100);
        Rectangle goalBounds = goalLabel.getBounds();
        int goalMidPoint = goalBounds.x + (goalBounds.width / 2);
        if (motorcycleBounds.intersects(goalBounds) && motorcycleX + 100 >= goalMidPoint) {
            isGameActive = false;
            stopTimer();
            hasWon = true;
            showWinDialog();
        }
    }

    protected void showWinDialog() {
        JDialog winDialog = new JDialog(mainGameWindow.getFrame(), "You Win!", true);
        winDialog.setLayout(new GridLayout(4, 1, 10, 10));
        winDialog.setSize(300, 200);
        winDialog.setLocationRelativeTo(this);

        JLabel winLabel = new JLabel("You Win!", SwingConstants.CENTER);
        winLabel.setFont(new Font("Arial", Font.BOLD, 20));
        winDialog.add(winLabel);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            returnToLevelGame();
            winDialog.dispose();
        });
        winDialog.add(backButton);

        JButton replayButton = new JButton("Play Again");
        replayButton.addActionListener(e -> {
            restartLevel();
            winDialog.dispose();
        });
        winDialog.add(replayButton);

        JButton nextLevelButton = new JButton("Next Level");
        nextLevelButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(winDialog, "Level 2 Coming Soon!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        });
        winDialog.add(nextLevelButton);

        winDialog.setVisible(true);
    }

    public abstract void startLevel();
}