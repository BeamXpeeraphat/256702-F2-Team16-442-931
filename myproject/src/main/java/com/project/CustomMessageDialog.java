package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomMessageDialog {
    public static void showMessageDialog(Component parent, String message, String title, int messageType) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        if (window == null || !(window instanceof JFrame)) {
            JOptionPane.showMessageDialog(parent, message, title, messageType);
            return;
        }
    
        JFrame frame = (JFrame) window;
        Dimension screenSize = frame.getSize();
    
        JDialog overlayDialog = new JDialog(frame, false);
        overlayDialog.setUndecorated(true);
        overlayDialog.setSize(screenSize);
        overlayDialog.setLocationRelativeTo(frame);
        overlayDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        overlayDialog.setLayout(new BorderLayout());
        overlayDialog.setBackground(new Color(0, 0, 0, 0));
        overlayDialog.setOpacity(0.0f);
    
        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setCursor(CursorManager.getNormalCursor());
    
        overlayPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                overlayPanel.setCursor(CursorManager.getNormalCursor());
                frame.setCursor(CursorManager.getNormalCursor());
            }
        });
    
        JOptionPane pane = new JOptionPane(message, messageType);
        JDialog messageDialog = pane.createDialog(frame, title);
        messageDialog.setModal(true);
        messageDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        messageDialog.setCursor(CursorManager.getNormalCursor()); // ตั้งค่า cursor สำหรับ dialog
        recursiveSetCursor(messageDialog.getContentPane()); // ตั้งค่า cursor ซ้ำสำหรับทุกปุ่ม
    
        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageDialog.dispose();
                overlayDialog.dispose();
                frame.setCursor(CursorManager.getNormalCursor());
                System.out.println("Overlay clicked, closing both dialogs");
            }
        });
    
        overlayDialog.add(overlayPanel, BorderLayout.CENTER);
    
        overlayDialog.setVisible(true);
        messageDialog.setVisible(true);
    
        messageDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                overlayDialog.dispose();
                frame.setCursor(CursorManager.getNormalCursor());
                System.out.println("Message closed, overlay disposed, cursor reset to normal: " + frame.getCursor().getName());
            }
        });
    }

    public static int showConfirmDialog(Component parent, String message, String title, int optionType, int messageType) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        if (window == null || !(window instanceof JFrame)) {
            return JOptionPane.showConfirmDialog(parent, message, title, optionType, messageType);
        }
    
        JFrame frame = (JFrame) window;
        Dimension screenSize = frame.getSize();
    
        JDialog overlayDialog = new JDialog(frame, false);
        overlayDialog.setUndecorated(true);
        overlayDialog.setSize(screenSize);
        overlayDialog.setLocationRelativeTo(frame);
        overlayDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        overlayDialog.setLayout(new BorderLayout());
        overlayDialog.setBackground(new Color(0, 0, 0, 0));
        overlayDialog.setOpacity(0.0f);
    
        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setCursor(CursorManager.getNormalCursor());
    
        overlayPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                overlayPanel.setCursor(CursorManager.getNormalCursor());
                frame.setCursor(CursorManager.getNormalCursor());
            }
        });
    
        JOptionPane pane = new JOptionPane(message, messageType, optionType);
        JDialog messageDialog = pane.createDialog(frame, title);
        messageDialog.setModal(true);
        messageDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        messageDialog.setCursor(CursorManager.getNormalCursor()); // ตั้งค่า cursor สำหรับ dialog
        recursiveSetCursor(messageDialog.getContentPane()); // ตั้งค่า cursor ซ้ำสำหรับทุกปุ่ม
    
        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageDialog.dispose();
                overlayDialog.dispose();
                frame.setCursor(CursorManager.getNormalCursor());
                System.out.println("Overlay clicked, closing both dialogs");
            }
        });
    
        overlayDialog.add(overlayPanel, BorderLayout.CENTER);
    
        overlayDialog.setVisible(true);
        messageDialog.setVisible(true);
    
        messageDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                overlayDialog.dispose();
                frame.setCursor(CursorManager.getNormalCursor());
                System.out.println("Message closed, overlay disposed, cursor reset to normal: " + frame.getCursor().getName());
            }
        });
    
        Object selectedValue = pane.getValue();
        if (selectedValue == null) {
            return JOptionPane.CLOSED_OPTION;
        }
        if (selectedValue instanceof Integer) {
            return (Integer) selectedValue;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    private static void recursiveSetCursor(Component component) {
        if (component instanceof JButton) {
            ((JButton) component).setCursor(CursorManager.getClickCursor());
        } else if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                recursiveSetCursor(child);
            }
        }
    }
}