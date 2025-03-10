package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        // MouseListener สำหรับเข้า/ออกหน้าต่าง
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(CursorManager.getNormalCursor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });

        // MouseMotionListener สำหรับปุ่มและ JLabel
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Component component = SwingUtilities.getDeepestComponentAt(mainPanel, e.getX(), e.getY());
                if (component instanceof JButton || component instanceof JLabel) {
                    component.setCursor(CursorManager.getClickCursor());
                } else {
                    setCursor(CursorManager.getNormalCursor());
                }
            }
        });

        setButtonCursors(mainPanel);

        // WindowListener สำหรับบันทึก inventory
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

    private void setButtonCursors(Component component) {
        if (component instanceof JButton) {
            ((JButton) component).setCursor(CursorManager.getClickCursor());
        } else if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setButtonCursors(child);
            }
        }
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        setButtonCursors(mainPanel);
    }

    public ShopGame getShopGame() {
        return shopGame;
    }
}