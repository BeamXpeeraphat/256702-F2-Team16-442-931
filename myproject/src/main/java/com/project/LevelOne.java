package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LevelOne extends MainLevel {
    private JButton settingButton;

    public LevelOne(MainGameWindow mainGameWindow) {
        super(mainGameWindow);
        motorcycleY = 700;
        setFocusable(true);
        initializeLevelOneUI();
        startLevel(); // เริ่มเลเวลครั้งแรก
        regainFocus();
    }

    @Override
    public void startLevel() {
        System.out.println("Level 1 started!");
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && comp != coinLabel && comp != timeLabel && comp != motorcycleLabel && comp != goalLabel) {
                remove(comp);
            }
        }
        coins.clear();

        int[] coinXPositions = {200, 400, 600, 800, 1000, 1200};
        initializeCoins(coinXPositions, 650);

        if (goalLabel != null) {
            remove(goalLabel);
        }
        ImageIcon goalIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/Goal.png"));
        if (goalIcon.getImage() == null) {
            System.err.println("Failed to load Goal.png");
            goalLabel = new JLabel("Goal");
            goalLabel.setBounds(1400, 650, 100, 100);
        } else {
            Image scaledGoal = goalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            goalLabel = new JLabel(new ImageIcon(scaledGoal));
            goalLabel.setBounds(1400, 650, 100, 100);
        }
        add(goalLabel);

        isGameActive = true;
        startTimer();
        regainFocus();
        revalidate();
        repaint();
    }

    private void initializeLevelOneUI() {
        ImageIcon settingIcon = new ImageIcon(getClass().getClassLoader().getResource("com/project/setting.png"));
        if (settingIcon.getImage() == null) {
            System.err.println("Failed to load setting.png, using text button");
            settingButton = new JButton("Settings");
        } else {
            settingIcon = new ImageIcon(settingIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            settingButton = new JButton(settingIcon);
        }
        int buttonX = 1460;
        int buttonY = 25;
        settingButton.setBounds(buttonX, buttonY, 50, 50);
        settingButton.setFocusable(false);
        settingButton.addActionListener(e -> {
            showSettings();
            regainFocus();
        });
        add(settingButton);

        revalidate();
        repaint();
    }

    @Override
    public void resumeGame() {
        super.resumeGame();
        regainFocus();
    }

    @Override
    protected void regainFocus() {
        SwingUtilities.invokeLater(() -> {
            setFocusable(true);
            requestFocusInWindow();
            mainGameWindow.getFrame().requestFocus();
            if (isFocusOwner()) {
                System.out.println("LevelOne regained focus");
            } else {
                System.out.println("LevelOne failed to regain focus, forcing focus...");
                mainGameWindow.getFrame().toFront();
                mainGameWindow.getFrame().requestFocus();
                requestFocusInWindow();
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // ไม่เรียก restartLevel() ที่นี่ เพื่อรักษาสถานะเดิม
        regainFocus();
    }
}