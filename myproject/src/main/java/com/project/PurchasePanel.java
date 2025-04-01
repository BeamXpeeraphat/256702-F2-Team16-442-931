package com.project;

import javax.swing.*;
import java.awt.*;

public class PurchasePanel extends JPanel {
    private JPanel rightPanel;

    public PurchasePanel(MainGameWindow mainGameWindow) {
        setLayout(null);
        setOpaque(false); // ไม่มีพื้นหลังซ้ำ

        // แผงด้านขวา
        rightPanel = new JPanel();
        rightPanel.setBounds(275, 60, 500, 500);
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(255, 255, 255, 150));
        rightPanel.setLayout(null); // ใช้ null layout เพื่อควบคุมตำแหน่ง

        // ข้อความ Purchase Section และรายละเอียด
        JLabel label = new JLabel("<html><b><span style=\"font-size: 24px;\">Purchase Section</span></b><br>"
                + "Purchase options and details go here.</html>");
        label.setBounds(10, 10, 480, 50); // กำหนดตำแหน่งชิดซ้ายบน
        rightPanel.add(label);
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