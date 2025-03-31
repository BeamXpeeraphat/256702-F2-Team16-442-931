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
            dispose(); // ปิด dialog และกลับไปที่เกม
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

        // จัดการเมื่อปิด Dialog ไม่ว่าจะด้วยปุ่มกากบาทหรือ Resume
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainLevel.resumeGame(); // เริ่มเกมต่อเมื่อปิดด้วยกากบาท
                System.out.println("Setting dialog closed via X, resuming game");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                mainLevel.resumeGame(); // เริ่มเกมต่อเมื่อปิดด้วยวิธีใดก็ตาม
                System.out.println("Setting dialog closed, resuming game");
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
}