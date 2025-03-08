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
        rightPanel.add(new JLabel("<html><b><span style=\"font-size: 24px;\">Purchase Section</span></b><br>"
                + "Purchase options and details go here.</html>"));
        add(rightPanel);

        // ปรับขนาดอัตโนมัติ
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                rightPanel.setBounds(275, 60, getWidth() - 325, getHeight() - 120);
                repaint();
            }
        });

        revalidate();
        repaint();
    }
}