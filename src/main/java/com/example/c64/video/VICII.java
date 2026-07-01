package com.example.c64.video;

import com.example.c64.bus.Bus;
import com.example.c64.memory.Memory;

import java.awt.*;

public class VICII {

    private Memory memory;
    private Bus bus;
    private C64Screen screen;

    private int rasterX;
    private int rasterY;

    private boolean frameComplete;

    private final int[] registers =
        new int[0x40];

// Esto es de la implementacion basica, creo que con C64Screen no lo necesita
private final int[] framebuffer =
    new int[320 * 200];

private final byte[] colorRam =
    new byte[1024];


private final Sprite[] sprites =
    new Sprite[8];
    
    
private int cycleInLine;


private static final int[] COLORS = {

    0x000000,
    0xFFFFFF,
    0x880000,
    0xAAFFEE,

    0xCC44CC,
    0x00CC55,
    0x0000AA,
    0xEEEE77,

    0xDD8855,
    0x664400,
    0xFF7777,
    0x333333,

    0x777777,
    0xAAFF66,
    0x0088FF,
    0xBBBBBB
};


    public VICII(Bus bus) {
        this.bus = bus;
        this.screen = new C64Screen();
    }


// JAC Nos detenemos en el apartado Render frame completo porque diverge entre el uso de frameBuffer y el objeto screen
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
                    bus.read(addr) & 0xFF;

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

    cycleInLine++;

    if (cycleInLine >= 63) {

        cycleInLine = 0;

        rasterY++;

        if (rasterY >= 312) {

            rasterY = 0;

            frameComplete = true;
        }
    }

    updateRasterRegisters();

    checkRasterIRQ();
}



    public int read(int address) {


if(address >= 0xD800 &&
   address <= 0xDBFF) {

    return colorRam[
        address - 0xD800] & 0x0F;
}

        return registers[address & 0x3F];
    }

    public void write(int address, int value) {

    int reg = address & 0x3F;

    if (reg == 0x19) {

        registers[0x19] &= ~value;

        return;
    }

    registers[reg] = value & 0xFF;

    }

public int[] getFramebuffer() {

    return framebuffer;
}

private void updateRasterRegisters() {

    registers[0x12] = rasterY & 0xFF;

    if ((rasterY & 0x100) != 0) {

        registers[0x11] |= 0x80;

    } else {

        registers[0x11] &= 0x7F;
    }
}

private void checkRasterIRQ() {

    int compare =
        registers[0x12] |
        ((registers[0x11] & 0x80) << 1);

    if (rasterY == compare) {

        registers[0x19] |= 0x01;

        if ((registers[0x1A] & 0x01) != 0) {

            cpuIRQ();
        }
    }
}

private void cpuIRQ() {

    bus.requestIRQ();
}

private int getScreenBase() {

    return ((registers[0x18] >> 4) & 0x0F)
        * 1024;
}

private int getCharBase() {

    return ((registers[0x18] >> 1) & 0x07)
        * 2048;
}

private void renderTextLine(int raster) {

}


// JAC esto no se donde va
// if(isBadLine()) {

//     bus.stealCycles(40);
// }


private boolean isBadLine() {

    return rasterY >= 48 &&
           rasterY <= 247 &&
           (rasterY & 7)
               == (registers[0x11] & 7);
}

}

