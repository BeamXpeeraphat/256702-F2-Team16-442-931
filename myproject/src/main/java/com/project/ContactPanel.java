package com.project;

import javax.swing.*;
import java.awt.*;

public class ContactPanel extends JPanel {
    private JPanel rightPanel;
    private ImageIcon backgroundImage;

    public ContactPanel(MainGameWindow mainGameWindow) {
        setLayout(null);
        setOpaque(false); // ไม่มีพื้นหลังซ้ำ

        // โหลดภาพ contactme.png
        try {
            backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("com/project/contactme.png"));
            if (backgroundImage.getImage() == null) {
                System.err.println("Failed to load contactme.png - Image is null");
            } else {
                System.out.println("Successfully loaded contactme.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading contactme.png: " + e.getMessage());
            backgroundImage = null;
        }

        // แผงด้านขวา
        rightPanel = new JPanel();
        rightPanel.setBounds(275, 60, 1000, 800);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150));
        
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(new JLabel("<html><b><span style=\"font-size: 24px;\">Contact Section</span></b><br>"
                + "Contact information goes here.</html>"), BorderLayout.NORTH);
        
        if (backgroundImage != null) {
            // สร้าง JLabel โดยไม่ปรับสเกลรูปภาพ
            JLabel imageLabel = new JLabel(backgroundImage) {
                @Override
                public Dimension getPreferredSize() {
                    // คืนค่าขนาดจริงของรูปภาพ
                    return new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
                }
            };
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