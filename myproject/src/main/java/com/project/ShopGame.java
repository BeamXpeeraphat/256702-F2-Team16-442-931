package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class ShopGame extends JPanel {
    private MainGameWindow mainGameWindow;
    private JButton[] leftButtons;
    private JButton currentSelectedButton;
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private Inventory inventory;
    private JPanel leftPanel; // เพิ่มตัวแปรเพื่อเก็บ leftPanel

    public ShopGame(MainGameWindow mainGameWindow) {
        this.mainGameWindow = mainGameWindow;
        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        backgroundLabel = new JLabel() {
            private BufferedImage backgroundImage;

            {
                try {
                    URL imgURL = getClass().getResource("/com/project/backgroundofshop.jpg");
                    if (imgURL != null) {
                        backgroundImage = ImageIO.read(imgURL);
                        System.out.println("Image loaded successfully: " + imgURL);
                    } else {
                        System.err.println("Error: backgroundofshop.jpg not found at /com/project/");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                    backgroundImage = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.drawString("Failed to load background image!", 10, 20);
                }
            }
        };
        backgroundLabel.setBounds(0, 0, 800, 600);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
                layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
                repaint();
            }
        });

        // สร้าง leftPanel ครั้งเดียวใน constructor
        leftPanel = createLeftPanel();
        layeredPane.add(leftPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridLayout(5, 1, 5, 45));
        leftPanel.setBounds(50, 150, 175, 450);
        leftPanel.setOpaque(false);

        String[] buttonLabels = {"Motorcycle", "Purchase", "Contact", "Inventory", "Return"};
        leftButtons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setBackground(Color.LIGHT_GRAY); // สีเริ่มต้นเป็นเทาอ่อน
            button.setFont(new Font("Arial", Font.BOLD, 14));
            leftButtons[i] = button;

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!button.getText().equals("Return")) {
                        resetButtonColors();
                        button.setBackground(Color.GRAY);
                        currentSelectedButton = button;
                    }
                    mainGameWindow.setCursor(CursorManager.getClickCursor());
                    updatePanel(button.getText());
                }
            });
            leftPanel.add(button);
        }
        return leftPanel;
    }

    private void resetButtonColors() {
        for (JButton button : leftButtons) {
            button.setBackground(Color.LIGHT_GRAY);
        }
    }

    private void updatePanel(String buttonLabel) {
        // ลบเฉพาะเลเยอร์เนื้อหา ไม่สร้าง leftPanel ใหม่
        layeredPane.removeAll();
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(leftPanel, JLayeredPane.PALETTE_LAYER); // ใช้ leftPanel เดิม

        if (buttonLabel.equals("Return")) {
            if (inventory != null) {
                inventory.saveToFile();
            }
            mainGameWindow.showPanel("HomePage");
        } else {
            JPanel contentPanel = null;
            switch (buttonLabel) {
                case "Motorcycle":
                    contentPanel = new MotorcyclePanel(mainGameWindow);
                    inventory = ((MotorcyclePanel) contentPanel).getInventory();
                    break;
                case "Purchase":
                    contentPanel = new PurchasePanel(mainGameWindow);
                    break;
                case "Contact":
                    contentPanel = new ContactPanel(mainGameWindow);
                    break;
                case "Inventory":
                    if (inventory == null) {
                        contentPanel = new MotorcyclePanel(mainGameWindow);
                        inventory = ((MotorcyclePanel) contentPanel).getInventory();
                        contentPanel = inventory;
                    } else {
                        contentPanel = inventory;
                    }
                    break;
            }
            if (contentPanel != null) {
                contentPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                layeredPane.add(contentPanel, JLayeredPane.MODAL_LAYER);
            }
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}