package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LevelOne extends MainLevel {
    private JButton settingButton;

    public LevelOne(MainGameWindow mainGameWindow) {
        super(mainGameWindow);
        setFocusable(true); // ทำให้แน่ใจว่าแผงสามารถรับเหตุการณ์คีย์ได้
        initializeLevelOneUI();
        startLevel();
        regainFocus(); // เรียกเพื่อให้แน่ใจว่าเริ่มต้นด้วยโฟกัสที่ LevelOne
    }

    @Override
    public void startLevel() {
        System.out.println("Level 1 started!");
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
        int buttonX = 1460; // ปรับตำแหน่งตามต้องการ
        int buttonY = 25;
        settingButton.setBounds(buttonX, buttonY, 50, 50);
        settingButton.setFocusable(false); // ป้องกันปุ่มขโมยโฟกัส
        settingButton.addActionListener(e -> {
            showSettings();
            regainFocus(); // คืนโฟกัสให้ LevelOne หลังจากกดปุ่ม
        });
        add(settingButton);
    }

    // Override resumeGame เพื่อให้แน่ใจว่าโฟกัสกลับมา
    @Override
    protected void resumeGame() {
        super.resumeGame();
        regainFocus(); // บังคับโฟกัสกลับมาที่ LevelOne
    }

    private void regainFocus() {
        SwingUtilities.invokeLater(() -> {
            setFocusable(true);
            requestFocusInWindow();
            if (isFocusOwner()) {
                System.out.println("LevelOne regained focus");
            } else {
                System.out.println("LevelOne failed to regain focus, forcing focus...");
                mainGameWindow.getFrame().requestFocus();
                requestFocusInWindow();
            }
        });
    }
}