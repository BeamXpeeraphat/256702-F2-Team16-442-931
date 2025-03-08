package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelGame extends JPanel {
    private MainGameWindow mainGameWindow;

    public LevelGame(MainGameWindow mainGameWindow) {
        this.mainGameWindow = mainGameWindow;
        setLayout(new BorderLayout());

        ImageIcon backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("com/project/backgroundofgame.jpg"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        JPanel levelPanel = new JPanel(new GridBagLayout());
        levelPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JButton level1Button = new JButton("Level 1");
        JButton level2Button = new JButton("Level 2");
        JButton level3Button = new JButton("Level 3");
        JButton backButton = new JButton("Back");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        level1Button.setFont(buttonFont);
        level2Button.setFont(buttonFont);
        level3Button.setFont(buttonFont);
        backButton.setFont(buttonFont);
        level1Button.setBackground(new Color(50, 205, 50));
        level1Button.setForeground(Color.BLACK);
        level2Button.setBackground(new Color(50, 205, 50));
        level2Button.setForeground(Color.BLACK);
        level3Button.setBackground(new Color(50, 205, 50));
        level3Button.setForeground(Color.BLACK);
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.WHITE);

        Dimension buttonSize = new Dimension(200, 50);
        level1Button.setPreferredSize(buttonSize);
        level2Button.setPreferredSize(buttonSize);
        level3Button.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        levelPanel.add(level1Button, gbc);
        gbc.gridy = 1;
        levelPanel.add(level2Button, gbc);
        gbc.gridy = 2;
        levelPanel.add(level3Button, gbc);
        gbc.gridy = 3;
        levelPanel.add(backButton, gbc);

        backgroundLabel.add(levelPanel, BorderLayout.CENTER);

        // เพิ่มเอฟเฟกต์เปลี่ยนเคอร์เซอร์
        addHoverEffect(level1Button, new Color(50, 205, 50));
        addHoverEffect(level2Button, new Color(50, 205, 50));
        addHoverEffect(level3Button, new Color(50, 205, 50));
        addHoverEffect(backButton, new Color(255, 69, 0));

        // กำหนด ActionListener
        level1Button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Starting Level 1!"));
        level2Button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Starting Level 2!"));
        level3Button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Coming Soon!"));
        backButton.addActionListener(e -> mainGameWindow.showPanel("HomePage"));
    }

    // เพิ่ม MouseListener เพื่อให้ปุ่มเปลี่ยนเคอร์เซอร์
    private void addHoverEffect(JButton button, Color defaultColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mainGameWindow.setCursor(CursorManager.getClickCursor()); // เปลี่ยนเป็นเคอร์เซอร์คลิกเมื่อโฮเวอร์
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mainGameWindow.setCursor(CursorManager.getNormalCursor()); // กลับไปเคอร์เซอร์ปกติ
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mainGameWindow.setCursor(CursorManager.getClickCursor()); // เปลี่ยนเป็นเคอร์เซอร์คลิกเมื่อกด
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mainGameWindow.setCursor(CursorManager.getNormalCursor()); // กลับไปเคอร์เซอร์ปกติเมื่อปล่อย
            }
        });
    }
}