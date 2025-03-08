package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MotorcyclePanel extends JPanel {
    private JPanel rightPanel;
    private JLayeredPane layeredPane;
    private Inventory inventory;
    private JLabel coinsLabel; // เพิ่ม JLabel สำหรับแสดง coins

    public MotorcyclePanel(MainGameWindow mainGameWindow) {
        this.inventory = new Inventory(mainGameWindow);
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
            "coin.png", "blockcoin.png"
        };

        ImageIcon[] images = new ImageIcon[6];
        int[][] sizes = {
            {150, 160}, {155, 160}, {190, 200}, {200, 250},
            {50, 40}, {230, 170}
        };

        for (int i = 0; i < 6; i++) {
            images[i] = loadAndScaleImage(imageNames[i], sizes[i][0], sizes[i][1]);
        }

        int[][] positions = {
            {150, 100}, {800, 100}, {150, 400}, {800, 300},
            {1090, 42}, {950, -20}
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
                    public void mouseClicked(MouseEvent e) {
                        String motorcycleName = imageNames[index];
                        if (index == 0) {
                            JOptionPane.showMessageDialog(null, 
                                motorcycleName + " It's your starter car, ready to use.!");
                        } else if (inventory.isMotorcycleOwned(motorcycleName)) {
                            JOptionPane.showMessageDialog(null, "You already have this " + motorcycleName + ".");
                        } else {
                            int currentCoins = inventory.getCoins();
                            int response = JOptionPane.showConfirmDialog(null, 
                                "You have " + currentCoins + " Coin\n" + 
                                "Do you want to buy " + motorcycleName + " for " + prices[index] + " coins?", 
                                "Buy Motorcycle", JOptionPane.YES_NO_OPTION);
                            if (response == JOptionPane.YES_OPTION) {
                                if (inventory.spendCoins(prices[index])) {
                                    inventory.addMotorcycle(motorcycleName);
                                    int remainingCoins = inventory.getCoins();
                                    updateCoinsDisplay(); // อัพเดทข้อความ coins
                                    JOptionPane.showMessageDialog(null, 
                                        "Buy " + motorcycleName + " complete!\nBalance: " + remainingCoins + " Coin",
                                        "Purchase Confirmation", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Not enough coins!");
                                }
                            }
                        }
                    }
                });
                rightPanel.add(imageLabel);
            }
        }

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 500, 500);

        if (images[5] != null) {
            JLabel blockcoinLabel = createImageLabel(images[5]);
            blockcoinLabel.setBounds(positions[5][0], positions[5][1], 
                                   images[5].getIconWidth(), images[5].getIconHeight());
            layeredPane.add(blockcoinLabel, Integer.valueOf(0));
            
            // เพิ่ม JLabel สำหรับแสดงจำนวน coins
            coinsLabel = new JLabel(String.valueOf(inventory.getCoins()));
            coinsLabel.setFont(new Font("Arial", Font.BOLD, 25));
            coinsLabel.setForeground(Color.WHITE);
            coinsLabel.setBounds(positions[5][0] + 80 , positions[5][1] + 70 , 100, 30); // วางข้าง blockcoin.png
            layeredPane.add(coinsLabel, Integer.valueOf(1));
        }
        if (images[4] != null) {
            JLabel coinLabel = createImageLabel(images[4]);
            coinLabel.setBounds(positions[4][0], positions[4][1], 
                              images[4].getIconWidth(), images[4].getIconHeight());
            layeredPane.add(coinLabel, Integer.valueOf(1));
        }

        rightPanel.add(layeredPane);
        add(rightPanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                rightPanel.setBounds(275, 60, getWidth() - 325, getHeight() - 120);
                layeredPane.setBounds(0, 0, rightPanel.getWidth(), rightPanel.getHeight());
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

    private ImageIcon loadAndScaleImage(String imageName, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + imageName));
            if (icon.getImage() == null) {
                System.err.println("Failed to load " + imageName + " - Image is null");
                return null;
            }
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
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