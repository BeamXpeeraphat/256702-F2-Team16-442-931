package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameLauncher extends JFrame {
    private JButton startButton;

    public GameLauncher() {
        // ตั้งค่าหน้าต่าง
        setTitle("Game Launcher");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false); // ปิดการปรับขนาด

        // โหลดและเพิ่มรูปภาพพื้นหลัง
        JLabel backgroundLabel = new JLabel();
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/com/project/backgroundoflauncher.png"));
        if (backgroundImage.getImageLoadStatus() == MediaTracker.COMPLETE) {
            backgroundLabel.setIcon(backgroundImage);
        } else {
            System.out.println("Error: Cannot load /com/project/imageoflauncher.png");
            backgroundLabel.setBackground(Color.RED);
            backgroundLabel.setOpaque(true);
        }
        backgroundLabel.setBounds(0, 0, 1000, 650);
        add(backgroundLabel);

        // สร้างปุ่ม Start Game
        startButton = new JButton();
        startButton.setBounds(650, 480, 285, 120);

        // โหลดรูปภาพสำหรับปุ่ม
        ImageIcon buttonIcon = new ImageIcon(getClass().getResource("/com/project/startoflauncher.png"));
        if (buttonIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            startButton.setIcon(buttonIcon);
        } else {
            System.out.println("Error: Cannot load /com/project/startoflauncher.png");
            startButton.setText("Start Game (Image Failed)");
        }

        // ปรับแต่งปุ่ม
        startButton.setContentAreaFilled(false);
        startButton.setBorder(null);
        startButton.setFocusPainted(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // เพิ่มปุ่มหลังพื้นหลัง
        add(startButton);

        // ตรวจสอบลำดับชั้น
        setComponentZOrder(startButton, 0); // ปุ่มอยู่ด้านหน้า
        setComponentZOrder(backgroundLabel, 1); // พื้นหลังอยู่ด้านหลัง

        // แสดงหน้าต่าง
        setVisible(true);
    }

    private void startGame() {
        dispose(); // ปิดหน้าต่าง GameLauncher ทันที
        SwingUtilities.invokeLater(new Runnable() { // เรียกใน EDT
            @Override
            public void run() {
                HomePage homePage = new HomePage();
                homePage.showWindow(); // เรียกเมธอดแสดงหน้าต่างทันที
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameLauncher::new);
    }
}