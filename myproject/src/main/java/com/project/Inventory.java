package com.project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Inventory extends JPanel {
    private int coins;
    private ArrayList<String> ownedMotorcycles;
    private ImageIcon coinIcon;
    private JPanel rightPanel;
    private static final String SAVE_FILE = "inventory.txt";
    private static final int MOTORCYCLE_WIDTH = 150;
    private static final int START_X = 20;
    private static final int START_Y = 150;
    private static final int X_GAP = 400; // ระยะห่างของมอไซค์ในแนวแกน X
    private static final int Y_GAP = 30;  // ระยะห่างของมอไซค์ในแนวแกน Y
    private String selectedMotorcycle;
    private JLabel coinLabel;
    private MainLevel mainLevel;

    public Inventory(MainGameWindow mainGameWindow) {
        this.mainLevel = null; // เพิ่มการกำหนดค่าเริ่มต้นให้ mainLevel
        setLayout(null);
        setOpaque(false);

        coins = 1000;
        ownedMotorcycles = new ArrayList<>();
        ownedMotorcycles.add("motorcycle1.png");

        loadFromFile();

        if (selectedMotorcycle == null && ownedMotorcycles.contains("motorcycle1.png")) {
            selectedMotorcycle = "motorcycle1.png";
        }

        coinIcon = loadCoinImage("coin.png");

        initializeRightPanel(); // ตั้งค่าเริ่มต้นของ rightPanel
        add(rightPanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                updatePanelSize(); // อัปเดตขนาดเมื่อมีการปรับขนาด
                repaint();
            }
        });

        revalidate();
        repaint();
    }

    private void initializeRightPanel() {
        // ถ้ามี rightPanel เดิมอยู่ ให้ลบออกก่อน
        if (rightPanel != null) {
            remove(rightPanel);
        }

        rightPanel = new JPanel();
        int panelWidth = Math.max(getWidth() - 325, 700); // คงขนาดขั้นต่ำ 700
        int panelHeight = Math.max(getHeight() - 120, 600); // คงขนาดขั้นต่ำ 600
        rightPanel.setBounds(275, 60, panelWidth, panelHeight);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150)); // โปร่งใส (alpha = 150)
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("<html><b><span style=\"font-size: 24px;\">Inventory Section</span></b><br>"
                + "Your coins and owned items are here.</html>");
        titleLabel.setBounds(10, 10, 480, 50);
        rightPanel.add(titleLabel);

        coinLabel = new JLabel("Coins: " + coins, coinIcon, JLabel.LEFT);
        coinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coinLabel.setBounds(10, 70, 200, 30);
        rightPanel.add(coinLabel);

        JLabel itemsLabel = new JLabel("Owned Motorcycles:");
        itemsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        itemsLabel.setBounds(10, 110, 200, 30);
        rightPanel.add(itemsLabel);

        int index = 0;
        int y = START_Y;
        int maxHeightRow1 = 0;

        // คำนวณความสูงสูงสุดของแถวแรก
        for (String motorcycle : ownedMotorcycles) {
            ImageIcon icon = loadImage(motorcycle);
            if (icon != null && index < 2) {
                maxHeightRow1 = Math.max(maxHeightRow1, icon.getIconHeight());
            }
            index++;
            if (index >= 4) break;
        }

        index = 0;
        for (String motorcycle : ownedMotorcycles) {
            String displayImage = motorcycle.equals(selectedMotorcycle) ? motorcycle.replace(".png", "select.png") : motorcycle;
            ImageIcon icon = loadImage(displayImage);
            if (icon == null) {
                icon = loadImage(motorcycle);
            }
            if (icon != null) {
                String displayName = motorcycle.replace(".png", "");
                JLabel itemLabel = new JLabel(displayName, icon, JLabel.LEFT);
                int row = index / 2;
                int col = index % 2;
                int x = START_X + col * X_GAP;
                int height = icon.getIconHeight();
                if (row == 0) {
                    itemLabel.setBounds(x, y, MOTORCYCLE_WIDTH + 200, height);
                } else {
                    itemLabel.setBounds(x, y + maxHeightRow1 + Y_GAP, MOTORCYCLE_WIDTH + 200, height);
                }

                final String motorcycleName = motorcycle;
                itemLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectedMotorcycle = motorcycleName;
                        refreshPanel(); // รีเฟรชทั้ง panel เมื่อเลือกมอเตอร์ไซค์
                        saveToFile();
                        if (mainLevel != null) {
                            mainLevel.notifyMotorcycleChanged();
                            System.out.println("Notified MainLevel: Selected motorcycle changed to " + motorcycleName);
                        } else {
                            System.err.println("MainLevel is null, cannot notify!");
                        }
                    }
                });

                rightPanel.add(itemLabel);
                index++;
                if (index >= 4) break;
            }
        }
    }

    public void addMotorcycle(String motorcycleName) {
        if (!ownedMotorcycles.contains(motorcycleName)) {
            ownedMotorcycles.add(motorcycleName);
            refreshPanel(); // รีเฟรชทั้ง panel เมื่อเพิ่มมอเตอร์ไซค์
            saveToFile();
            System.out.println("Added motorcycle: " + motorcycleName);
        }
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            refreshPanel(); // รีเฟรชทั้ง panel เมื่อใช้เหรียญ
            saveToFile();
            return true;
        }
        return false;
    }

    public boolean isMotorcycleOwned(String motorcycleName) {
        return ownedMotorcycles.contains(motorcycleName);
    }

    private void refreshPanel() {
        initializeRightPanel(); // สร้าง rightPanel ใหม่
        add(rightPanel);
        updatePanelSize(); // อัปเดตขนาดให้สอดคล้องกับขนาดปัจจุบัน
        revalidate();
        repaint();
    }

    private void updatePanelSize() {
        if (rightPanel != null) {
            int panelWidth = Math.max(getWidth() - 325, 700);
            int panelHeight = Math.max(getHeight() - 120, 600);
            rightPanel.setBounds(275, 60, panelWidth, panelHeight);
        }
    }

    private ImageIcon loadImage(String imageName) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + imageName));
            if (icon.getImage() == null) {
                System.err.println("Failed to load " + imageName + " - Image is null");
                return null;
            }
            int originalWidth = icon.getIconWidth();
            int originalHeight = icon.getIconHeight();
            int newWidth = MOTORCYCLE_WIDTH;
            int newHeight = (int) ((double) originalHeight * newWidth / originalWidth);
            Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error loading " + imageName + ": " + e.getMessage());
            return null;
        }
    }

    private ImageIcon loadCoinImage(String imageName) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("com/project/" + imageName));
            if (icon.getImage() == null) {
                System.err.println("Failed to load " + imageName + " - Image is null");
                return null;
            }
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
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
            writer.write("SelectedMotorcycle: " + (selectedMotorcycle != null ? selectedMotorcycle : ""));
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
                } else if (line.startsWith("SelectedMotorcycle: ")) {
                    String selected = line.substring(19).trim();
                    selectedMotorcycle = selected.isEmpty() ? null : selected;
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
            selectedMotorcycle = "motorcycle1.png";
        }
    }

    public void resetToDefault() {
        coins = 1000;
        ownedMotorcycles.clear();
        ownedMotorcycles.add("motorcycle1.png");
        selectedMotorcycle = "motorcycle1.png";
        refreshPanel(); // รีเฟรชทั้ง panel
        saveToFile();
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;
        refreshPanel(); // รีเฟรชทั้ง panel
        saveToFile();
    }

    public String getSelectedMotorcycle() {
        return selectedMotorcycle;
    }

    public void setMainLevel(MainLevel mainLevel) {
        this.mainLevel = mainLevel;
        System.out.println("MainLevel set in Inventory: " + (mainLevel != null ? "Success" : "Null"));
    }

    public void setSelectedMotorcycle(String motorcycleName) {
        if (ownedMotorcycles.contains(motorcycleName)) {
            selectedMotorcycle = motorcycleName;
            refreshPanel(); // รีเฟรชทั้ง panel
            saveToFile();
            if (mainLevel != null) {
                mainLevel.notifyMotorcycleChanged();
                System.out.println("Notified MainLevel from setSelectedMotorcycle: " + motorcycleName);
            } else {
                System.err.println("MainLevel is null in setSelectedMotorcycle!");
            }
        }
    }
}