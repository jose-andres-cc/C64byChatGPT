package com.example.c64.video;

//import com.example.c64.bus.Bus;
import com.example.c64.memory.Memory;

import java.awt.*;

public class VICII {

    private Memory memory;
    //private Bus bus;
    private C64Screen screen;

    public VICII(Memory memory) {
        //this.bus = bus;
        this.memory = memory;
        this.screen = new C64Screen();
    }

    public void renderFrame() {

        Graphics2D g =
            screen.getImage().createGraphics();

        // Fondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 320, 200);

        // Screen RAM = 0x0400
        int screenBase = 0x0400;

        for (int row = 0; row < 25; row++) {

            for (int col = 0; col < 40; col++) {

                int addr =
                    screenBase + row * 40 + col;

                int code =
                    memory.read(addr) & 0xFF;

                char c;

                if (code >= 32 && code <= 126)
                    c = (char) code;
                else
                    c = '.';

                Charset.drawChar(
                    g,
                    c,
                    col * 8,
                    row * 8
                );
            }
        }

        g.dispose();

        screen.refresh();
    }

    public void clock() {

    // rasterCycle++;

    // if(rasterCycle == 63) {

    //     rasterCycle = 0;
    //     rasterLine++;
    // }
}


}
