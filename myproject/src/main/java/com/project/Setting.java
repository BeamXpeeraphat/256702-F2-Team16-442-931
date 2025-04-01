package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Setting extends JDialog {
    private MainLevel mainLevel;

    public Setting(MainLevel mainLevel) {
        super(mainLevel.mainGameWindow.getFrame(), "Settings", true);
        this.mainLevel = mainLevel;
        setLayout(new GridLayout(4, 1, 10, 10));
        setSize(300, 400);
        setLocationRelativeTo(mainLevel.mainGameWindow.getFrame());

        JButton motorcycleButton = new JButton("Shop");
        motorcycleButton.addActionListener(e -> showShopGame());
        add(motorcycleButton);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> {
            mainLevel.resumeGame();
            dispose();
        });
        add(resumeButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            mainLevel.restartLevel();
            dispose();
        });
        add(restartButton);

        JButton returnButton = new JButton("Return to Level Selection");
        returnButton.addActionListener(e -> {
            mainLevel.returnToLevelGame();
            dispose();
        });
        add(returnButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainLevel.resumeGame();
                System.out.println("Setting dialog closed via X, resuming game");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Setting dialog closed");
            }
        });
    }

    private void showShopGame() {
        JDialog shopDialog = new JDialog(this, "Shop", true);
        ShopGame shopGame = new ShopGame(mainLevel.mainGameWindow, this);
        shopDialog.setLayout(new BorderLayout());
        shopDialog.add(shopGame, BorderLayout.CENTER);
        shopDialog.setSize(1300, 780);
        shopDialog.setLocationRelativeTo(this);
        shopDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainLevel.updateMotorcycleImage();
                System.out.println("Shop dialog closed");
            }
        });
        shopDialog.setVisible(true);
    }

    // เมธอดใหม่สำหรับแสดง Message Dialog
    public static void showGameStartedMessage(MainLevel mainLevel) {
        JDialog messageDialog = new JDialog(mainLevel.mainGameWindow.getFrame(), "Game Status", true);
        messageDialog.setLayout(new BorderLayout(10, 10));
        messageDialog.setSize(300, 150);
        messageDialog.setLocationRelativeTo(mainLevel.mainGameWindow.getFrame());
        messageDialog.setCursor(CursorManager.getNormalCursor()); // ตั้งค่า cursor สำหรับ dialog
    
        JLabel messageLabel = new JLabel("Resume the game", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageDialog.add(messageLabel, BorderLayout.CENTER);
    
        JButton okButton = new JButton("OK");
        okButton.setCursor(CursorManager.getClickCursor()); // ตั้งค่า cursor สำหรับปุ่ม
        okButton.addActionListener(e -> {
            mainLevel.resumeGame();
            messageDialog.dispose();
            System.out.println("Game resumed via OK button");
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        messageDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        messageDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainLevel.resumeGame();
                System.out.println("Message dialog closed via X, resuming game");
            }
    
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Message dialog closed");
            }
        });
    
        messageDialog.setVisible(true);
    }
}