package com.example.c64.video;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class C64Screen extends JPanel {

    public static final int WIDTH = 320;
    public static final int HEIGHT = 200;
    public static final int SCALE = 2;

    private BufferedImage image =
        new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    public C64Screen() {

        JFrame frame = new JFrame("C64 Emulator");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);

        frame.add(this);

        frame.setVisible(true);
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(
            image,
            0,
            0,
            WIDTH * SCALE,
            HEIGHT * SCALE,
            null
        );
    }

    public void refresh() {
        repaint();
    }
}