package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MotorcyclePanel extends JPanel {
    private JPanel rightPanel;
    private JLayeredPane layeredPane;
    private Inventory inventory;
    private JLabel coinsLabel;
    private static final int[][] SIZES = { {150, 160}, {155, 160}, {190, 200}, {200, 250} };
    private static final int[][] POSITIONS = { {150, 100}, {800, 100}, {150, 400}, {800, 300} };
    private static final int[] PRICES = {0, 250, 300, 350};
    private static final String[] IMAGE_NAMES = { "motorcycle1.png", "motorcycle2.png", "motorcycle3.png", "motorcycle4.png" };

    public MotorcyclePanel(MainGameWindow mainGameWindow, Inventory inventory) {
        this.inventory = inventory;
        setLayout(null);
        setOpaque(false);

        initializeRightPanel();
        add(rightPanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                updatePanelSize();
                repaint();
            }
        });

        revalidate();
        repaint();
    }

    private void initializeRightPanel() {
        if (rightPanel != null) {
            remove(rightPanel);
        }

        rightPanel = new JPanel();
        int panelWidth = Math.max(getWidth() - 325, 500);
        int panelHeight = Math.max(getHeight() - 120, 500);
        rightPanel.setBounds(275, 60, panelWidth, panelHeight);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150));
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("<html><b><span style=\"font-size: 24px;\">Motorcycle Section</span></b><br>"
                + "Details about motorcycles go here.</html>");
        titleLabel.setBounds(10, 10, 480, 50);
        rightPanel.add(titleLabel);

        for (int i = 0; i < 4; i++) {
            addMotorcycle(i);
        }

        ImageIcon tabCoinsIcon = loadAndScaleImage("TabCoins.png", 230, -1);
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(988, 10, 300, 200);

        if (tabCoinsIcon != null) {
            JLabel tabCoinsLabel = createImageLabel(tabCoinsIcon);
            tabCoinsLabel.setBounds(0, 0, tabCoinsIcon.getIconWidth(), tabCoinsIcon.getIconHeight());
            layeredPane.add(tabCoinsLabel, Integer.valueOf(0));
            
            coinsLabel = new JLabel(String.valueOf(inventory.getCoins()));
            coinsLabel.setFont(new Font("Arial", Font.BOLD, 25));
            coinsLabel.setForeground(Color.WHITE);
            coinsLabel.setBounds(62, 31, 100, 30);
            layeredPane.add(coinsLabel, Integer.valueOf(1));
        }

        rightPanel.add(layeredPane);
    }

    private void addMotorcycle(int index) {
        String imageName = IMAGE_NAMES[index];
        String displayImage = inventory.isMotorcycleOwned(imageName) ? imageName.replace(".png", "select.png") : imageName;
        ImageIcon icon = loadAndScaleImage(displayImage, SIZES[index][0], SIZES[index][1]);
        
        if (icon != null) {
            JLabel imageLabel = createImageLabel(icon);
            imageLabel.setBounds(POSITIONS[index][0], POSITIONS[index][1], icon.getIconWidth(), icon.getIconHeight());

            String motorcycleName = imageName.replace(".png", "");
            String displayText = "<html><center>" + motorcycleName + "<br>" + PRICES[index] + " Coins</center></html>";
            JLabel namePriceLabel = new JLabel(displayText);
            namePriceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            namePriceLabel.setHorizontalAlignment(JLabel.CENTER);
            int labelX = POSITIONS[index][0];
            int labelY = POSITIONS[index][1] + icon.getIconHeight() + 5;
            namePriceLabel.setBounds(labelX, labelY, icon.getIconWidth(), 50);

            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    System.out.println("Mouse entered " + imageName);
                    imageLabel.setCursor(CursorManager.getClickCursor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    System.out.println("Mouse exited " + imageName);
                    imageLabel.setCursor(CursorManager.getNormalCursor());
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Clicked on " + imageName);
                    String motorcycleName = imageName;
                    if (index == 0) {
                        CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                            motorcycleName.replace(".png", "") + " It's your starter car, ready to use!", 
                            "Starter Car", JOptionPane.INFORMATION_MESSAGE);
                    } else if (inventory.isMotorcycleOwned(motorcycleName)) {
                        CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                            "You already have this " + motorcycleName.replace(".png", "") + ".", 
                            "Already Owned", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int currentCoins = inventory.getCoins();
                        int response = CustomMessageDialog.showConfirmDialog(MotorcyclePanel.this, 
                            "You have " + currentCoins + " Coin\n" + 
                            "Do you want to buy " + motorcycleName.replace(".png", "") + " for " + PRICES[index] + " coins?", 
                            "Buy Motorcycle", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.YES_OPTION) {
                            if (inventory.spendCoins(PRICES[index])) {
                                inventory.addMotorcycle(motorcycleName);
                                int remainingCoins = inventory.getCoins();
                                refreshPanel(); // รีเฟรช MotorcyclePanel
                                CustomMessageDialog.showMessageDialog(MotorcyclePanel.this, 
                                    "Buy " + motorcycleName.replace(".png", "") + " complete!\nBalance: " + remainingCoins + " Coin",
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
            rightPanel.add(namePriceLabel);
        }
    }

    private void refreshPanel() {
        initializeRightPanel();
        add(rightPanel);
        updatePanelSize();
        revalidate();
        repaint();
    }

    private void updatePanelSize() {
        if (rightPanel != null) {
            int panelWidth = Math.max(getWidth() - 325, 500);
            int panelHeight = Math.max(getHeight() - 120, 500);
            rightPanel.setBounds(275, 60, panelWidth, panelHeight);
            
            if (layeredPane != null) {
                layeredPane.setBounds(panelWidth - 312, 10, 300, 200);
            }
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