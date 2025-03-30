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
    private Inventory inventory; // ตัวแปรสำหรับ Inventory เดียว

    public MainGameWindow() {
        setTitle("Adventure Rider Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        setCursor(CursorManager.getNormalCursor());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        try {
            inventory = new Inventory(this); // สร้าง Inventory ครั้งเดียว
            HomePage homePage = new HomePage(this);
            LevelGame levelGame = new LevelGame(this);
            shopGame = new ShopGame(this, null);
            LevelOne levelOne = new LevelOne(this);

            mainPanel.add(homePage, "HomePage");
            mainPanel.add(levelGame, "LevelGame");
            mainPanel.add(shopGame, "ShopGame");
            mainPanel.add(levelOne, "LevelOne");

            add(mainPanel);
            cardLayout.show(mainPanel, "HomePage");
        } catch (Exception e) {
            System.err.println("Error initializing panels: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to initialize game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (inventory != null) {
                    inventory.saveToFile();
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
        try {
            cardLayout.show(mainPanel, panelName);
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp.isVisible() && comp instanceof JPanel) {
                    comp.requestFocusInWindow();
                    System.out.println("Focus requested for panel: " + panelName);
                    break;
                }
            }
            System.out.println("Switched to panel: " + panelName);
            mainPanel.revalidate();
            mainPanel.repaint();
        } catch (Exception e) {
            System.err.println("Error showing panel " + panelName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ShopGame getShopGame() {
        return shopGame;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public JFrame getFrame() {
        return this;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGameWindow window = new MainGameWindow();
            window.setVisible(true);
        });
    }
}