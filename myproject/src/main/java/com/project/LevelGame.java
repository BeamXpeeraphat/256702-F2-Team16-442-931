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
JLabel backgroundLabel = new JLabel(backgroundImage) {
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage.getImage() != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Error: backgroundofgame.jpg not found!");
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
};
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        JPanel levelPanel = new JPanel(new GridBagLayout());
        levelPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JButton level1Button = new JButton("Level 1");
        JButton level2Button = new JButton("Level 2");
        JButton level0Button = new JButton("Coming soon");
        JButton backButton = new JButton("Back");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        level1Button.setFont(buttonFont);
        level2Button.setFont(buttonFont);
        level0Button.setFont(buttonFont);
        backButton.setFont(buttonFont);
        level1Button.setBackground(new Color(192, 192, 192));
        level1Button.setForeground(Color.BLACK);
        level2Button.setBackground(new Color(169, 169, 169));
        level2Button.setForeground(Color.BLACK);
        level0Button.setBackground(new Color(169, 169, 169));
        level0Button.setForeground(Color.BLACK);
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.WHITE);

        Dimension buttonSize = new Dimension(200, 50);
        level1Button.setPreferredSize(buttonSize);
        level2Button.setPreferredSize(buttonSize);
        level0Button.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        levelPanel.add(level1Button, gbc);
        gbc.gridy = 1;
        levelPanel.add(level2Button, gbc);
        gbc.gridy = 2;
        levelPanel.add(level0Button, gbc);
        gbc.gridy = 3;
        levelPanel.add(backButton, gbc);

        backgroundLabel.add(levelPanel, BorderLayout.CENTER);

        addHoverEffect(level1Button, new Color(50, 205, 50));
        addHoverEffect(level2Button, new Color(50, 205, 50));
        addHoverEffect(level0Button, new Color(50, 205, 50));
        addHoverEffect(backButton, new Color(255, 69, 0));

        level1Button.addActionListener(e -> mainGameWindow.showPanel("LevelOne"));
        level2Button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Level 2 is coming soon!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE));
        level0Button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Coming Soon!", "Coming Soon!", JOptionPane.INFORMATION_MESSAGE));
        backButton.addActionListener(e -> mainGameWindow.showPanel("HomePage"));
    }

    private void addHoverEffect(JButton button, Color defaultColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(defaultColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().equals(new Color(255, 69, 0)) ? 
                                     new Color(255, 69, 0) : new Color(192, 192, 192));
            }
        });
    }
}