package com.example.c64.video;

import java.awt.*;

public class Charset {

    public static void drawChar(
        Graphics2D g,
        char c,
        int x,
        int y
    ) {

        g.setColor(Color.GREEN);

        g.setFont(new Font("Monospaced", Font.BOLD, 8));

        g.drawString(String.valueOf(c), x, y + 8);
    }
}
