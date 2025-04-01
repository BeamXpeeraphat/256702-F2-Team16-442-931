package com.project;

import javax.swing.*;
import java.awt.*;

public class PurchasePanel extends JPanel {
    private JPanel rightPanel;
    private ImageIcon backgroundImage;

    public PurchasePanel(MainGameWindow mainGameWindow) {
        setLayout(null);
        setOpaque(false); // ไม่มีพื้นหลังซ้ำ

        // โหลดภาพ nothingherelol.png
        try {
            backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("com/project/nothingherelol.png"));
            if (backgroundImage.getImage() == null) {
                System.err.println("Failed to load nothingherelol.png - Image is null");
            } else {
                System.out.println("Successfully loaded nothingherelol.png");
                // ปรับขนาดรูปภาพ (เช่น ลดลงเหลือ 50% ของขนาดเดิม)
                Image scaledImage = backgroundImage.getImage().getScaledInstance(
                    backgroundImage.getIconWidth() / 2, 
                    backgroundImage.getIconHeight() / 2, 
                    Image.SCALE_SMOOTH
                );
                backgroundImage = new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading nothingherelol.png: " + e.getMessage());
            backgroundImage = null;
        }

        // แผงด้านขวา
        rightPanel = new JPanel();
        rightPanel.setBounds(275, 60, 500, 500);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150));
        rightPanel.setLayout(new BorderLayout());

        // ข้อความ Purchase Section และรายละเอียด
        JLabel label = new JLabel("<html><b><span style=\"font-size: 24px;\">Purchase Section</span></b><br>"
                + "Purchase options and details go here.</html>");
        label.setBounds(10, 10, 480, 50); // กำหนดตำแหน่งชิดซ้ายบน
        rightPanel.add(label, BorderLayout.NORTH);

        if (backgroundImage != null) {
            // สร้าง JLabel เพื่อแสดงรูปภาพที่ปรับขนาดแล้ว
            JLabel imageLabel = new JLabel(backgroundImage);
            // ตั้งค่า alignment ให้อยู่กึ่งกลาง
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);

            // ห่อ imageLabel ด้วย JPanel เพื่อควบคุมตำแหน่งใน BorderLayout.CENTER
            JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false);
            imagePanel.add(imageLabel);

            rightPanel.add(imagePanel, BorderLayout.CENTER);
        } else {
            rightPanel.add(new JLabel("Image not found"), BorderLayout.CENTER);
        }

        add(rightPanel);

        // ปรับขนาดอัตโนมัติ    
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                rightPanel.setBounds(275, 60, getWidth() - 325, getHeight() - 120);
                revalidate();
                repaint();
            }
        });
    }
}