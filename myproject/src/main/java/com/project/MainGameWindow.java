package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

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

        // เพิ่มหน้า HomePage และ LevelGame
        HomePage homePage = new HomePage(this);
        LevelGame levelGame = new LevelGame(this);

        mainPanel.add(homePage, "HomePage");
        mainPanel.add(levelGame, "LevelGame");

        add(mainPanel);

        // เริ่มที่ HomePage
        cardLayout.show(mainPanel, "HomePage");

        // เปลี่ยนเคอร์เซอร์เมื่อเมาส์เข้า/ออกจากหน้าต่าง
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
        cardLayout.show(mainPanel, panelName);
    }
}