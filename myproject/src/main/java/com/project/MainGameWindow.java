package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel backgroundLabel; // ตัวแปรสำหรับจัดการพื้นหลัง

    public MainGameWindow() {
        setTitle("Adventure Rider Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        // ตั้งค่าเคอร์เซอร์ปกติเมื่อเริ่มต้น
        setCursor(CursorManager.getNormalCursor());

        // ตั้งค่า CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // สร้าง JLabel สำหรับพื้นหลัง
        backgroundLabel = new JLabel();
        ImageIcon homeBackground = new ImageIcon(getClass().getClassLoader().getResource("com/project/backgroundofhomepage.jpg"));
        if (homeBackground.getImage() == null) {
            System.err.println("Error: backgroundofhomepage.jpg not found!");
            backgroundLabel.setText("Background image not found!");
        } else {
            backgroundLabel.setIcon(homeBackground);
        }
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight()); // ตั้งค่าขนาดให้เต็มหน้าจอ

        // เพิ่มหน้า HomePage, LevelGame และ ShopGame
        HomePage homePage = new HomePage(this);
        LevelGame levelGame = new LevelGame(this);
        ShopGame shopGame = new ShopGame(this);

        backgroundLabel.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(homePage, "HomePage");
        mainPanel.add(levelGame, "LevelGame");
        mainPanel.add(shopGame, "ShopGame");

        add(backgroundLabel); // เพิ่ม backgroundLabel ลงใน JFrame

        // เริ่มที่ HomePage
        cardLayout.show(mainPanel, "HomePage");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(CursorManager.getNormalCursor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setCursor(CursorManager.getClickCursor());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(CursorManager.getNormalCursor());
            }
        });

        setVisible(true);
    }

    public void showPanel(String panelName) {
        if (panelName.equals("ShopGame")) {
            ImageIcon shopBackground = new ImageIcon(getClass().getClassLoader().getResource("com/project/backgroundofshop.jpg"));
            if (shopBackground.getImage() == null) {
                System.err.println("Error: backgroundofshop.jpg not found or failed to load! Check resources/com/project/ folder.");
                backgroundLabel.setText("Background image not found!");
            } else {
                System.out.println("Successfully loaded backgroundofshop.jpg");
                backgroundLabel.setIcon(shopBackground);
            }
        } else if (panelName.equals("HomePage")) {
            ImageIcon homeBackground = new ImageIcon(getClass().getClassLoader().getResource("com/project/backgroundofhomepage.jpg"));
            if (homeBackground.getImage() == null) {
                System.err.println("Error: backgroundofhomepage.jpg not found!");
                backgroundLabel.setText("Background image not found!");
            } else {
                backgroundLabel.setIcon(homeBackground);
            }
        }
        cardLayout.show(mainPanel, panelName);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight()); // อัปเดตขนาดทุกครั้งที่เปลี่ยนหน้า
    }

    // เมธอดสำหรับตั้งค่าพื้นหลัง (ถ้าต้องการให้ยืดหยุ่นมากขึ้น)
    public void setBackgroundLabel(JLabel label) {
        this.backgroundLabel = label;
    }
}