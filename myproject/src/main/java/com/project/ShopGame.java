package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShopGame extends JPanel {
    private MainGameWindow mainGameWindow;
    private JPanel rightPanel; // แผงสำหรับแสดงเนื้อหาด้านขวา
    private CardLayout cardLayout;
    private JButton[] leftButtons; // เก็บปุ่มด้านซ้ายเพื่อจัดการสี
    private JButton currentSelectedButton; // เก็บปุ่มที่ถูกเลือกปัจจุบัน

    public ShopGame(MainGameWindow mainGameWindow) {
        this.mainGameWindow = mainGameWindow;
        setLayout(new BorderLayout());
        

        // ลบบรรทัดนี้: setBackground(Color.WHITE);

        // แผงด้านซ้ายสำหรับปุ่ม
        JPanel leftPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        leftPanel.setPreferredSize(new Dimension(150, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // สร้างปุ่มด้านซ้าย
        String[] buttonLabels = {"motorcycle", "purchase", "contact", "return"};
        Color defaultColor = Color.LIGHT_GRAY;
        Color clickedColor = Color.GRAY;
        leftButtons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setBackground(defaultColor);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(130, 30));

            leftButtons[i] = button;

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    resetButtonColors();
                    button.setBackground(clickedColor);
                    currentSelectedButton = button;
                    mainGameWindow.setCursor(CursorManager.getClickCursor());
                    updateRightPanel(button.getText());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mainGameWindow.setCursor(CursorManager.getNormalCursor());
                }
            });

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mainGameWindow.setCursor(CursorManager.getClickCursor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (button != currentSelectedButton) {
                        mainGameWindow.setCursor(CursorManager.getNormalCursor());
                    }
                }
            });

            leftPanel.add(button);
        }

        leftButtons[0].setBackground(clickedColor);
        currentSelectedButton = leftButtons[0];

        // แผงด้านขวา (กรอบใหญ่)
        rightPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) rightPanel.getLayout();
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        rightPanel.setBackground(Color.WHITE); // ยังคงไว้ที่แผงขวาเพื่อความสวยงาม
        rightPanel.setPreferredSize(new Dimension(400, 200));
        rightPanel.setMaximumSize(new Dimension(400, 200)); // จำกัดขนาดสูงสุด
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 5),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        rightPanel.add(createMotorcyclePanel(), "motorcycle");
        rightPanel.add(createPurchasePanel(), "purchase");
        rightPanel.add(createContactPanel(), "contact");

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        cardLayout.show(rightPanel, "motorcycle");
    }

    private void resetButtonColors() {
        for (JButton button : leftButtons) {
            button.setBackground(Color.LIGHT_GRAY);
        }
    }

    private JPanel createMotorcyclePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        String[] subLabels = {"y1", "y2", "y3", "y4"};
        for (String label : subLabels) {
            JButton subButton = new JButton(label);
            subButton.setBackground(Color.RED);
            subButton.setFont(new Font("Arial", Font.BOLD, 12));
            subButton.setPreferredSize(new Dimension(80, 40));
            panel.add(subButton);
        }

        JLabel detailLabel = new JLabel("<html><center>Details for Motorcycle<br>Select an item to purchase!</center></html>");
        detailLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        detailLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel fullPanel = new JPanel(new BorderLayout());
        fullPanel.add(panel, BorderLayout.CENTER);
        fullPanel.add(detailLabel, BorderLayout.SOUTH);
        return fullPanel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        String[] subLabels = {"y1", "y2", "y3", "y4"};
        for (String label : subLabels) {
            JButton subButton = new JButton(label);
            subButton.setBackground(Color.RED);
            subButton.setFont(new Font("Arial", Font.BOLD, 12));
            subButton.setPreferredSize(new Dimension(80, 40));
            panel.add(subButton);
        }

        JLabel detailLabel = new JLabel("<html><center>Purchase Options<br>Top up your balance here!</center></html>");
        detailLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        detailLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel fullPanel = new JPanel(new BorderLayout());
        fullPanel.add(panel, BorderLayout.CENTER);
        fullPanel.add(detailLabel, BorderLayout.SOUTH);
        return fullPanel;
    }

    private JPanel createContactPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        ImageIcon contactImage = new ImageIcon(getClass().getClassLoader().getResource("com/project/contactme.png"));
        if (contactImage.getImage() != null) {
            JLabel imageLabel = new JLabel(contactImage);
            panel.add(imageLabel, BorderLayout.CENTER);
        } else {
            JLabel errorLabel = new JLabel("Image not found: contactme.png");
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }

    private void updateRightPanel(String buttonLabel) {
        if (buttonLabel.equals("return")) {
            mainGameWindow.showPanel("HomePage");
        } else {
            cardLayout.show(rightPanel, buttonLabel);
        }
    }
}