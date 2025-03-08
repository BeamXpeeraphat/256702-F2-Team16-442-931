package com.project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;

public class Inventory extends JPanel {
    private int coins;
    private ArrayList<String> ownedMotorcycles;
    private ImageIcon coinIcon;
    private JPanel rightPanel;
    private static final String SAVE_FILE = "inventory.txt";

    public Inventory(MainGameWindow mainGameWindow) {
        setLayout(null);
        setOpaque(false);

        coins = 1000; // ค่าเริ่มต้น
        ownedMotorcycles = new ArrayList<>();
        ownedMotorcycles.add("motorcycle1.png"); // รถเริ่มต้น

        rightPanel = new JPanel();
        rightPanel.setBounds(275, 60, 500, 500);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150));
        rightPanel.setLayout(null);

        loadFromFile(); // โหลดข้อมูลจากไฟล์

        coinIcon = loadImage("coin.png", 30, 30);

        JLabel titleLabel = new JLabel("<html><b><span style=\"font-size: 24px;\">Inventory Section</span></b><br>"
                + "Your coins and owned items are here.</html>");
        titleLabel.setBounds(10, 10, 480, 50);
        rightPanel.add(titleLabel);

        add(rightPanel);
        updateInventoryDisplay();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                rightPanel.setBounds(275, 60, getWidth() - 325, getHeight() - 120);
                repaint();
            }
        });

        revalidate();
        repaint();
    }

    public void addMotorcycle(String motorcycleName) {
        if (!ownedMotorcycles.contains(motorcycleName)) {
            ownedMotorcycles.add(motorcycleName);
            updateInventoryDisplay();
            saveToFile();
        }
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            updateInventoryDisplay();
            saveToFile();
            return true;
        }
        return false;
    }

    public boolean isMotorcycleOwned(String motorcycleName) {
        return ownedMotorcycles.contains(motorcycleName);
    }

    private void updateInventoryDisplay() {
        // ลบส่วนประกอบเก่า (ยกเว้น title)
        for (Component comp : rightPanel.getComponents()) {
            if (!(comp instanceof JLabel && ((JLabel) comp).getText().contains("Inventory Section"))) {
                rightPanel.remove(comp);
            }
        }

        // แสดงยอดเงิน
        JLabel coinLabel = new JLabel("Coins: " + coins, coinIcon, JLabel.LEFT);
        coinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coinLabel.setBounds(10, 70, 200, 30);
        rightPanel.add(coinLabel);

        // หัวข้อรายการมอเตอร์ไซค์
        JLabel itemsLabel = new JLabel("Owned Motorcycles:");
        itemsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        itemsLabel.setBounds(10, 110, 200, 30);
        rightPanel.add(itemsLabel);

        // แสดงรายการมอเตอร์ไซค์
        int y = 150;
        for (String motorcycle : ownedMotorcycles) {
            ImageIcon icon = loadImage(motorcycle, 50, 50);
            if (icon != null) {
                JLabel itemLabel = new JLabel(motorcycle, icon, JLabel.LEFT);
                itemLabel.setBounds(20, y, 200, 50);
                rightPanel.add(itemLabel);
                y += 60;
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private ImageIcon loadImage(String imageName, int width, int height) {
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

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write("Coins: " + coins);
            writer.newLine();
            writer.write("Motorcycles:");
            writer.newLine();
            for (String motorcycle : ownedMotorcycles) {
                writer.write(motorcycle);
                writer.newLine();
            }
            System.out.println("Inventory saved to " + SAVE_FILE);
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found, using default values.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            ownedMotorcycles = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Coins: ")) {
                    coins = Integer.parseInt(line.substring(7));
                } else if (line.equals("Motorcycles:")) {
                    continue;
                } else if (!line.trim().isEmpty()) {
                    ownedMotorcycles.add(line);
                }
            }
            System.out.println("Inventory loaded from " + SAVE_FILE);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
            coins = 1000;
            ownedMotorcycles = new ArrayList<>();
            ownedMotorcycles.add("motorcycle1.png");
        }
    }

    public void resetToDefault() {
        coins = 1000;
        ownedMotorcycles.clear();
        ownedMotorcycles.add("motorcycle1.png");
        updateInventoryDisplay();
        saveToFile();
    }

    public int getCoins() {
        return coins;
    }
}