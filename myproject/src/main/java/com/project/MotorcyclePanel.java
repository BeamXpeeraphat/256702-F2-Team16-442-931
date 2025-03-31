package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MotorcyclePanel extends JPanel {
    private JPanel rightPanel;
    private JLayeredPane layeredPane;
    private Inventory inventory; // ใช้ Inventory ที่ส่งเข้ามา
    private JLabel coinsLabel;

    public MotorcyclePanel(MainGameWindow mainGameWindow, Inventory inventory) {
        this.inventory = inventory; // รับ Inventory จากพารามิเตอร์
        setLayout(null);
        setOpaque(false);

        rightPanel = new JPanel();
        rightPanel.setBounds(275, 60, 500, 500);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150));
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("<html><b><span style=\"font-size: 24px;\">Motorcycle Section</span></b><br>"
                + "Details about motorcycles go here.</html>");
        titleLabel.setBounds(10, 10, 480, 50);
        rightPanel.add(titleLabel);

        String[] imageNames = {
            "motorcycle1.png", "motorcycle2.png", "motorcycle3.png", "motorcycle4.png",
            "TabCoins.png"
        };

        ImageIcon[] images = new ImageIcon[5];
        int[][] sizes = {
            {150, 160}, {155, 160}, {190, 200}, {200, 250},
            {230, -1} // กำหนดความกว้าง 230, ความสูง -1 เพื่อให้คำนวณตามสัดส่วน
        };

        for (int i = 0; i < 5; i++) {
            images[i] = loadAndScaleImage(imageNames[i], sizes[i][0], sizes[i][1]);
        }

        int[][] positions = {
            {150, 100}, {800, 100}, {150, 400}, {800, 300},
            {950, -20}
        };

        int[] prices = {0, 250, 300, 350};

        for (int i = 0; i < 4; i++) {
            if (images[i] != null) {
                JLabel imageLabel = createImageLabel(images[i]);
                imageLabel.setBounds(positions[i][0], positions[i][1], 
                                     images[i].getIconWidth(), images[i].getIconHeight());
                
                final int index = i;
                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        System.out.println("Mouse entered " + imageNames[index]);
                        imageLabel.setCursor(CursorManager.getClickCursor());
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        System.out.println("Mouse exited " + imageNames[index]);
                        imageLabel.setCursor(CursorManager.getNormalCursor());
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Clicked on " + imageNames[index]);
                        String motorcycleName = imageNames[index];
                        if (index == 0) {
                            CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                                motorcycleName + " It's your starter car, ready to use.!", 
                                "Starter Car", JOptionPane.INFORMATION_MESSAGE);
                        } else if (inventory.isMotorcycleOwned(motorcycleName)) {
                            CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                                "You already have this " + motorcycleName + ".", 
                                "Already Owned", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            int currentCoins = inventory.getCoins();
                            int response = CustomMessageDialog.showConfirmDialog(MotorcyclePanel.this, 
                                "You have " + currentCoins + " Coin\n" + 
                                "Do you want to buy " + motorcycleName + " for " + prices[index] + " coins?", 
                                "Buy Motorcycle", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (response == JOptionPane.YES_OPTION) {
                                if (inventory.spendCoins(prices[index])) {
                                    inventory.addMotorcycle(motorcycleName);
                                    int remainingCoins = inventory.getCoins();
                                    updateCoinsDisplay();
                                    inventory.updateInventoryDisplay(); // อัปเดต UI ของ Inventory
                                    CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                                        "Buy " + motorcycleName + " complete!\nBalance: " + remainingCoins + " Coin",
                                        "Purchase Confirmation", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                                        "Not enough coins!", 
                                        "Insufficient Coins", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                });
                rightPanel.add(imageLabel);
            }
        }

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(950, -20, 300, 200);

        if (images[4] != null) {
            JLabel tabCoinsLabel = createImageLabel(images[4]);
            tabCoinsLabel.setBounds(0, 0, images[4].getIconWidth(), images[4].getIconHeight());
            layeredPane.add(tabCoinsLabel, Integer.valueOf(0));
            
            coinsLabel = new JLabel(String.valueOf(inventory.getCoins()));
            coinsLabel.setFont(new Font("Arial", Font.BOLD, 25));
            coinsLabel.setForeground(Color.WHITE);
            coinsLabel.setBounds(62, 31, 100, 30);
            layeredPane.add(coinsLabel, Integer.valueOf(1));
        }

        rightPanel.add(layeredPane);
        add(rightPanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                rightPanel.setBounds(275, 60, getWidth() - 325, getHeight() - 120);
                layeredPane.setBounds(988, 10, 300, 200);
                repaint();
            }
        });

        revalidate();
        repaint();
    }

    private void updateCoinsDisplay() {
        if (coinsLabel != null) {
            coinsLabel.setText(String.valueOf(inventory.getCoins()));
        }
    }

    private ImageIcon loadAndScaleImage(String imageName, int targetWidth, int targetHeight) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + imageName));
            if (icon.getImage() == null) {
                System.err.println("Failed to load " + imageName + " - Image is null");
                return null;
            }
            int originalWidth = icon.getIconWidth();
            int originalHeight = icon.getIconHeight();
            int newWidth, newHeight;

            if (targetHeight == -1) {
                newWidth = targetWidth;
                newHeight = (int) ((double) originalHeight * newWidth / originalWidth);
            } else if (targetWidth == -1) {
                newHeight = targetHeight;
                newWidth = (int) ((double) originalWidth * newHeight / originalHeight);
            } else {
                newWidth = targetWidth;
                newHeight = targetHeight;
            }

            Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error loading " + imageName + ": " + e.getMessage());
            return null;
        }
    }

    private JLabel createImageLabel(ImageIcon icon) {
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }

    public Inventory getInventory() {
        return inventory;
    }
}