package com.project;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private ImageIcon backgroundImage;

    public HomePage() {
        // ตั้งค่าหน้าต่างของ HomePage
        setTitle("Home Page - My Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // เต็มหน้าจอ
        setResizable(false); // ปิดการปรับขนาด ทำให้ปุ่ม Maximize ปิดการใช้งาน

        // โหลดและตั้งค่าพื้นหลัง
        backgroundImage = new ImageIcon(getClass().getResource("/com/project/backgroundofhomepage.jpg"));
        if (backgroundImage.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: Cannot load /com/project/backgroundofhomepage.jpg");
            System.out.println("Check if the file exists in src/com/project/ and the name is correct (case-sensitive).");
            setBackground(Color.GRAY); // สีสำรองถ้าโหลดภาพไม่ได้
        }

        // ใช้ BorderLayout เป็นเลย์เอาต์หลัก
        setLayout(new BorderLayout());

        // เพิ่ม JLabel สำหรับพื้นหลัง
        JLabel backgroundLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // ปรับขนาดภาพพื้นหลังให้เข้ากับหน้าต่าง (เต็มหน้าจอ)
                    Image scaledImage = backgroundImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    g.drawImage(scaledImage, 0, 0, this);
                }
            }
        };
        backgroundLabel.setLayout(new BorderLayout()); // ใช้ BorderLayout ใน JLabel
        add(backgroundLabel, BorderLayout.CENTER);

        // สร้าง JPanel สำหรับเมนู
        JPanel menuPanel = new JPanel(new GridBagLayout()); // ใช้ GridBagLayout
        menuPanel.setOpaque(false); // ทำให้พื้นหลังโปร่งใส

        // ตั้งค่า GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0); // ระยะห่างแนวตั้ง 20px
        gbc.anchor = GridBagConstraints.CENTER; // จัดกึ่งกลาง

        // เพิ่มปุ่มเมนู
        JButton startButton = new JButton("Start");
        JButton howToPlayButton = new JButton("How to Play");
        JButton infoButton = new JButton("Info");
        JButton exitButton = new JButton("Exit");

        // ปรับแต่งปุ่มให้เหมือนภาพ
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        startButton.setFont(buttonFont);
        howToPlayButton.setFont(buttonFont);
        infoButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);
        startButton.setBackground(new Color(135, 206, 235)); // สีน้ำเงินอ่อน
        startButton.setForeground(Color.BLACK);
        howToPlayButton.setBackground(new Color(205, 133, 63)); // สีน้ำตาล
        howToPlayButton.setForeground(Color.BLACK);
        infoButton.setBackground(new Color(255, 215, 0)); // สีเหลือง
        infoButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(255, 69, 0)); // สีส้มแดง
        exitButton.setForeground(Color.WHITE);

        // ปรับขนาดปุ่ม
        Dimension buttonSize = new Dimension(200, 50);
        startButton.setPreferredSize(buttonSize);
        howToPlayButton.setPreferredSize(buttonSize);
        infoButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // เพิ่มปุ่มลงใน menuPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(startButton, gbc);
        gbc.gridy = 1;
        menuPanel.add(howToPlayButton, gbc);
        gbc.gridy = 2;
        menuPanel.add(infoButton, gbc);
        gbc.gridy = 3;
        menuPanel.add(exitButton, gbc);

        // วาง menuPanel ตรงกลางหน้าต่าง
        backgroundLabel.add(menuPanel, BorderLayout.CENTER);

        // เพิ่ม ActionListener ให้ปุ่ม (ตัวอย่าง)
        startButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Starting the game!"));
        howToPlayButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "How to Play instructions here!"));
        infoButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Game Info here!"));
        exitButton.addActionListener(e -> System.exit(0)); // ออกโปรแกรม
    }

    // เมธอดเพื่อแสดงหน้าต่าง (ถ้าต้องการเรียกจากภายนอก)
    public void showWindow() {
        setVisible(true);
    }
}