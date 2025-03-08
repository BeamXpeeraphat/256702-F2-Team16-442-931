package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomePage extends JPanel {
    private MainGameWindow mainGameWindow;

    public HomePage(MainGameWindow mainGameWindow) {
        this.mainGameWindow = mainGameWindow;
        setLayout(new BorderLayout());

        ImageIcon backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("com/project/backgroundofhomepage.jpg"));
        JLabel backgroundLabel = new JLabel();
        if (backgroundImage.getImage() == null) {
            System.err.println("Error: backgroundofhomepage.jpg not found!");
            backgroundLabel.setText("Background image not found!");
        } else {
            backgroundLabel.setIcon(backgroundImage);
        }
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        // สร้างปุ่ม
        JButton playButton = new JButton("Play");
        JButton howToPlayButton = new JButton("How to Play");
        JButton shopButton = new JButton("Shop");
        JButton infoButton = new JButton("Info");
        JButton exitButton = new JButton("Exit");

        // บันทึกสีเริ่มต้นของปุ่มแต่ละปุ่ม
        Color defaultPlayColor = new Color(135, 206, 235);
        Color defaultHowToPlayColor = new Color(205, 133, 63);
        Color defaultShopColor = new Color(144, 238, 144);
        Color defaultInfoColor = new Color(255, 215, 0);
        Color defaultExitColor = new Color(255, 69, 0);

        // ตั้งค่าฟอนต์ตัวอักษรและสีของปุ่มเริ่มต้น
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(200, 50);
        setupButton(playButton, defaultPlayColor, Color.BLACK, buttonFont, buttonSize);
        setupButton(howToPlayButton, defaultHowToPlayColor, Color.BLACK, buttonFont, buttonSize);
        setupButton(shopButton, defaultShopColor, Color.BLACK, buttonFont, buttonSize);
        setupButton(infoButton, defaultInfoColor, Color.BLACK, buttonFont, buttonSize);
        setupButton(exitButton, defaultExitColor, Color.WHITE, buttonFont, buttonSize);

        // Panel สำหรับปุ่มตรงกลาง
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(playButton, gbc);
        gbc.gridy = 1;
        menuPanel.add(howToPlayButton, gbc);

        backgroundLabel.add(menuPanel, BorderLayout.CENTER);

        // Panel สำหรับปุ่ม Shop, Info และ Exit (ล่างซ้าย)
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(shopButton);
        bottomPanel.add(infoButton);
        bottomPanel.add(exitButton);

        // ปุ่ม Re:Game (ล่างขวา)
        JButton resetButton = new JButton("Re:Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(Color.RED);
        resetButton.setForeground(Color.WHITE);
        resetButton.setPreferredSize(new Dimension(150, 50)); // ลด height เป็น 25 (ปรับได้ตามต้องการ)
        resetButton.setMaximumSize(new Dimension(150, 50));   // จำกัดความสูงสูงสุด
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        // ใส่ resetButton ใน JPanel แยกเพื่อควบคุมขนาด
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 10)); // เดิม (20, 0)
        resetPanel.setOpaque(false);
        resetPanel.add(resetButton);

        // รวม bottomPanel และ resetPanel ใน container เดียว
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.add(bottomPanel, BorderLayout.WEST); // วาง Shop, Info, Exit ทางซ้าย
        bottomContainer.add(resetPanel, BorderLayout.EAST);  // วาง Re:Game ทางขวา
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // ระยะห่างจากขอบ

        backgroundLabel.add(bottomContainer, BorderLayout.SOUTH);

        // เพิ่มเอฟเฟกต์เปลี่ยนสีและเคอร์เซอร์เมื่อโฮเวอร์/คลิก
        addHoverEffect(playButton, defaultPlayColor);
        addHoverEffect(howToPlayButton, defaultHowToPlayColor);
        addHoverEffect(shopButton, defaultShopColor);
        addHoverEffect(infoButton, defaultInfoColor);
        addHoverEffect(exitButton, defaultExitColor);
        addHoverEffect(resetButton, Color.RED);

        // กำหนด ActionListener
        playButton.addActionListener(e -> mainGameWindow.showPanel("LevelGame"));
        howToPlayButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "<html><center><b><font size='6'>How to play</font></b></center><br>" +
            "<font size='4'>" +
            "- Press A to move left<br>" +
            "- Press D to move right<br>" +
            "- Press W or Spacebar to jump<br>" +
            "- Press H to honk the horn<br>" +
            "- Press P or ESC to pause the game and open various menus<br>" +
            "- Press M to enter the shop screen or change vehicles" +
            "</font></html>"));
        shopButton.addActionListener(e -> mainGameWindow.showPanel("ShopGame"));
        infoButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "<html><b><font size='4'>This game is created and developed by <br>" +
            "the collaboration between the project team <br>" +
            "codename: Mr.442 , Mr.931 </font></b><br></html>"));
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void setupButton(JButton button, Color bgColor, Color fgColor, Font font, Dimension size) {
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setPreferredSize(size);
    }

    private Color darkenColor(Color color, float factor) {
        int red = (int) (color.getRed() * factor);
        int green = (int) (color.getGreen() * factor);
        int blue = (int) (color.getBlue() * factor);
        return new Color(red, green, blue);
    }

    private void addHoverEffect(JButton button, Color defaultColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darkenColor(defaultColor, 0.6f));
                mainGameWindow.setCursor(CursorManager.getClickCursor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
                mainGameWindow.setCursor(CursorManager.getNormalCursor());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mainGameWindow.setCursor(CursorManager.getClickCursor());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mainGameWindow.setCursor(CursorManager.getNormalCursor());
            }
        });
    }

    private void resetGame() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "<html><center><b><span style=\"font-size: 20px;\">Are you sure you want to reset the game?<br>All progress will be lost.</span></b></center></html>",
            "Reset Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            ShopGame shopGame = mainGameWindow.getShopGame();
            if (shopGame != null && shopGame.getInventory() != null) {
                Inventory inventory = shopGame.getInventory();
                inventory.resetToDefault();
                inventory.saveToFile();
                JOptionPane.showMessageDialog(this, "Game has been reset to default!");
            }
        }
    }
}