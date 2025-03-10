package com.project;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class CursorManager {
    private static Cursor normalCursor;
    private static Cursor clickCursor;

    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        try {
            Image normalImage = toolkit.getImage(CursorManager.class.getResource("/com/project/color-pixels-purple-flower-cursor.png"));
            if (normalImage == null) {
                System.err.println("Error: Cannot load normal cursor image at /com/project/color-pixels-purple-flower-cursor.png");
                normalCursor = Cursor.getDefaultCursor();
            } else {
                normalCursor = toolkit.createCustomCursor(normalImage, new Point(0, 0), "Normal Cursor");
                System.out.println("Normal cursor loaded successfully, size: " + normalImage.getWidth(null) + "x" + normalImage.getHeight(null));
            }
        } catch (Exception e) {
            System.err.println("Exception loading normal cursor: " + e.getMessage());
            normalCursor = Cursor.getDefaultCursor();
        }

        try {
            Image clickImage = toolkit.getImage(CursorManager.class.getResource("/com/project/color-pixels-purple-flower-pointer.png"));
            if (clickImage == null) {
                System.err.println("Error: Cannot load click cursor image at /com/project/color-pixels-purple-flower-pointer.png");
                clickCursor = Cursor.getDefaultCursor();
            } else {
                clickCursor = toolkit.createCustomCursor(clickImage, new Point(0, 0), "Click Cursor");
                System.out.println("Click cursor loaded successfully, size: " + clickImage.getWidth(null) + "x" + clickImage.getHeight(null));
            }
        } catch (Exception e) {
            System.err.println("Exception loading click cursor: " + e.getMessage());
            clickCursor = Cursor.getDefaultCursor();
        }
    }

    public static Cursor getNormalCursor() {
        return normalCursor != null ? normalCursor : Cursor.getDefaultCursor();
    }

    public static Cursor getClickCursor() {
        return clickCursor != null ? clickCursor : Cursor.getDefaultCursor();
    }
}