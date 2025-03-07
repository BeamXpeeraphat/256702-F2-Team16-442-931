package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameLauncher extends JFrame {
    private JButton startButton;
    private ImageIcon defaultIcon;
    private ImageIcon hoverIcon;

    public GameLauncher() {
        setTitle("Game Launcher");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // ตั้งค่าเคอร์เซอร์เลื่อนเมาส์ให้ทั้งหน้าต่าง
        setCursor(CursorManager.getNormalCursor());

        // โหลดและปรับสเกลรูปภาพพื้นหลัง
        JLabel backgroundLabel = new JLabel();
        Image backgroundImg = new ImageIcon(getClass().getResource("/com/project/backgroundoflauncher.png")).getImage();
        backgroundImg = backgroundImg.getScaledInstance(1000, 650, Image.SCALE_SMOOTH);
        ImageIcon backgroundImage = new ImageIcon(backgroundImg);

        if (backgroundImage.getImageLoadStatus() == MediaTracker.COMPLETE) {
            backgroundLabel.setIcon(backgroundImage);
        } else {
            System.out.println("Error: Cannot load /com/project/backgroundoflauncher.png");
            backgroundLabel.setBackground(Color.RED);
            backgroundLabel.setOpaque(true);
        }
        backgroundLabel.setBounds(0, 0, 1000, 650);
        add(backgroundLabel);

        // สร้างปุ่ม Start
        startButton = new JButton();
        startButton.setBounds(650, 480, 285, 120);

        defaultIcon = new ImageIcon(getClass().getResource("/com/project/startoflauncher.png"));
        hoverIcon = new ImageIcon(getClass().getResource("/com/project/startoflauncher2.png"));

        if (defaultIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            startButton.setIcon(defaultIcon);
        } else {
            System.out.println("Error: Cannot load /com/project/startoflauncher.png");
            startButton.setText("Start Game (Image Failed)");
        }

        startButton.setContentAreaFilled(false);
        startButton.setBorder(null);
        startButton.setFocusPainted(false);

        // ตั้งค่า MouseListener เฉพาะปุ่ม Start
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    startButton.setIcon(hoverIcon);
                }
                startButton.setCursor(CursorManager.getClickCursor()); // เคอร์เซอร์คลิกได้สำหรับปุ่ม
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(defaultIcon);
                // ไม่ต้องตั้งเคอร์เซอร์ที่นี่ ให้ใช้ของ JFrame (เลื่อนเมาส์)
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startButton.setCursor(CursorManager.getClickCursor()); // เคอร์เซอร์คลิกได้เมื่อกด
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startButton.setCursor(CursorManager.getClickCursor()); // ยังคงคลิกได้เมื่อปล่อย
            }
        });

        // เพิ่ม MouseListener ให้พื้นหลังเพื่อรักษาเคอร์เซอร์เลื่อนเมาส์
        backgroundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(CursorManager.getNormalCursor()); // คงเคอร์เซอร์เลื่อนเมาส์ในพื้นหลัง
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor()); // ใช้เคอร์เซอร์ระบบเมื่อออกจากหน้าต่าง
            }
        });

        startButton.addActionListener(e -> startGame());

        add(startButton);
        setComponentZOrder(startButton, 0);
        setComponentZOrder(backgroundLabel, 1);

        setVisible(true);
    }

    private void startGame() {
        dispose(); // ปิด GameLauncher
        SwingUtilities.invokeLater(() -> new MainGameWindow()); // เปิดหน้าต่างใหม่แบบ fullscreen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameLauncher::new);
    }
}