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
    private static final int X_GAP = 250;
    private static final int Y_GAP = 10;
    private String selectedMotorcycle;
    private JLabel coinLabel;
    private ArrayList<JLabel> motorcycleLabels;
    private MainLevel mainLevel;

    public Inventory(MainGameWindow mainGameWindow) {
        setLayout(null);
        setOpaque(false);

        coins = 1000;
        ownedMotorcycles = new ArrayList<>();
        ownedMotorcycles.add("motorcycle1.png");
        motorcycleLabels = new ArrayList<>();

        rightPanel = new JPanel();
        rightPanel.setBounds(275, 60, 700, 600);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        loadFromFile();

        if (selectedMotorcycle == null && ownedMotorcycles.contains("motorcycle1.png")) {
            selectedMotorcycle = "motorcycle1.png";
        }

        coinIcon = loadCoinImage("coin.png");

        JLabel titleLabel = new JLabel("<html><b><span style=\"font-size: 24px;\">Inventory Section</span></b><br>"
                + "Your coins and owned items are here.</html>");
        titleLabel.setBounds(10, 10, 480, 50);
        rightPanel.add(titleLabel);

        add(rightPanel);
        initializeInventoryDisplay();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                int newWidth = Math.max(getWidth() - 325, 700);
                int newHeight = Math.max(getHeight() - 120, 600);
                rightPanel.setBounds(275, 60, newWidth, newHeight);
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

    private void initializeInventoryDisplay() {
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

        for (String motorcycle : ownedMotorcycles) {
            ImageIcon icon = loadImage(motorcycle);
            if (icon != null && index < 2) {
                maxHeightRow1 = Math.max(maxHeightRow1, icon.getIconHeight());
            }
            index++;
            if (index >= 4) break;
        }

        index = 0;
        motorcycleLabels.clear();
        for (String motorcycle : ownedMotorcycles) {
            String displayImage = motorcycle.equals(selectedMotorcycle) ? motorcycle.replace(".png", "select.png") : motorcycle;
            ImageIcon icon = loadImage(displayImage);
            if (icon == null) {
                icon = loadImage(motorcycle);
            }
            if (icon != null) {
                JLabel itemLabel = new JLabel(motorcycle, icon, JLabel.LEFT);
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
                        updateInventoryDisplay();
                        saveToFile();
                        if (mainLevel != null) {
                            mainLevel.notifyMotorcycleChanged();
                            System.out.println("Notified MainLevel: Selected motorcycle changed to " + motorcycleName);
                        } else {
                            System.err.println("MainLevel is null, cannot notify!");
                        }
                    }
                });

                motorcycleLabels.add(itemLabel);
                rightPanel.add(itemLabel);
                index++;
                if (index >= 4) break;
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void updateInventoryDisplay() {
        if (coinLabel != null) {
            coinLabel.setText("Coins: " + coins);
        }

        int index = 0;
        for (String motorcycle : ownedMotorcycles) {
            String displayImage = motorcycle.equals(selectedMotorcycle) ? motorcycle.replace(".png", "select.png") : motorcycle;
            ImageIcon icon = loadImage(displayImage);
            if (icon == null) {
                icon = loadImage(motorcycle);
            }
            if (icon != null) {
                if (index < motorcycleLabels.size()) {
                    motorcycleLabels.get(index).setIcon(icon);
                    motorcycleLabels.get(index).setText(motorcycle);
                } else {
                    JLabel itemLabel = new JLabel(motorcycle, icon, JLabel.LEFT);
                    int row = index / 2;
                    int col = index % 2;
                    int x = START_X + col * X_GAP;
                    int y = START_Y;
                    if (row == 1) {
                        y += motorcycleLabels.get(0).getHeight() + Y_GAP;
                    }
                    itemLabel.setBounds(x, y, MOTORCYCLE_WIDTH + 200, icon.getIconHeight());

                    final String motorcycleName = motorcycle;
                    itemLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            selectedMotorcycle = motorcycleName;
                            updateInventoryDisplay();
                            saveToFile();
                            if (mainLevel != null) {
                                mainLevel.notifyMotorcycleChanged();
                                System.out.println("Notified MainLevel: Selected motorcycle changed to " + motorcycleName);
                            } else {
                                System.err.println("MainLevel is null, cannot notify!");
                            }
                        }
                    });

                    motorcycleLabels.add(itemLabel);
                    rightPanel.add(itemLabel);
                }
                index++;
                if (index >= 4) break;
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
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
        motorcycleLabels.clear();
        rightPanel.removeAll();
        JLabel titleLabel = new JLabel("<html><b><span style=\"font-size: 24px;\">Inventory Section</span></b><br>"
                + "Your coins and owned items are here.</html>");
        titleLabel.setBounds(10, 10, 480, 50);
        rightPanel.add(titleLabel);
        initializeInventoryDisplay();
        saveToFile();
    }

    public int getCoins() {
        return coins;
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
            updateInventoryDisplay();
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