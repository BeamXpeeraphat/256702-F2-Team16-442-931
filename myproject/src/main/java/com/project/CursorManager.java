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
        
        // โหลดเคอร์เซอร์เลื่อนเมาส์
        Image normalImage = toolkit.getImage(CursorManager.class.getResource("/com/project/color-pixels-purple-flower-cursor.png"));
        if (normalImage == null) {
            System.out.println("Error: Cannot load normal cursor image");
            normalCursor = Cursor.getDefaultCursor(); // ใช้เคอร์เซอร์เริ่มต้นถ้าโหลดไม่สำเร็จ
        } else {
            normalCursor = toolkit.createCustomCursor(normalImage, new Point(0, 0), "Normal Cursor");
        }

        // โหลดเคอร์เซอร์คลิกได้
        Image clickImage = toolkit.getImage(CursorManager.class.getResource("/com/project/color-pixels-purple-flower-pointer.png"));
        if (clickImage == null) {
            System.out.println("Error: Cannot load click cursor image");
            clickCursor = Cursor.getDefaultCursor(); // ใช้เคอร์เซอร์เริ่มต้นถ้าโหลดไม่สำเร็จ
        } else {
            clickCursor = toolkit.createCustomCursor(clickImage, new Point(0, 0), "Click Cursor");
        }
    }

    public static Cursor getNormalCursor() {
        return normalCursor;
    }

    public static Cursor getClickCursor() {
        return clickCursor;
    }
}