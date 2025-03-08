package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ShopGame shopGame;

    public MainGameWindow() {
        setTitle("Adventure Rider Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        setCursor(CursorManager.getNormalCursor());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        HomePage homePage = new HomePage(this);
        LevelGame levelGame = new LevelGame(this);
        shopGame = new ShopGame(this);

        mainPanel.add(homePage, "HomePage");
        mainPanel.add(levelGame, "LevelGame");
        mainPanel.add(shopGame, "ShopGame");

        add(mainPanel);

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (shopGame != null && shopGame.getInventory() != null) {
                    shopGame.getInventory().saveToFile();
                    System.out.println("Saved inventory on exit.");
                }
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // เพิ่ม getter สำหรับ shopGame
    public ShopGame getShopGame() {
        return shopGame;
    }
}