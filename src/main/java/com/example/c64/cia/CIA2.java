package com.example.c64.cia;

import com.example.c64.bus.Bus;

public class CIA2 {

    private final Bus bus;

    private final int[] registers = new int[16];

    private int timerA;
    private int timerB;

    private int latchA;
    private int latchB;

    private boolean timerARunning;
    private boolean timerBRunning;

    private int irqMask;

    public CIA2(Bus bus) {

        this.bus = bus;
    }


public int read(int address) {

    int reg = address & 0x0F;

    switch (reg) {

        case 0x00:
            return registers[0];

        case 0x01:
            return registers[1];

        case 0x04:
            return timerA & 0xFF;

        case 0x05:
            return (timerA >> 8) & 0xFF;

        case 0x06:
            return timerB & 0xFF;

        case 0x07:
            return (timerB >> 8) & 0xFF;

        case 0x0D:

            int value = registers[0x0D];

            registers[0x0D] = 0;

            return value;

        default:
            return registers[reg];
    }
}

public void write(int address, int value) {

    int reg = address & 0x0F;

    value &= 0xFF;

    registers[reg] = value;

    switch (reg) {

        case 0x00:

            updateVicBank(value);
            break;

        case 0x04:

            latchA = (latchA & 0xFF00) | value;
            break;

        case 0x05:

            latchA = (latchA & 0x00FF) | (value << 8);
            break;

        case 0x06:

            latchB = (latchB & 0xFF00) | value;
            break;

        case 0x07:

            latchB = (latchB & 0x00FF) | (value << 8);
            break;

        case 0x0D:

            if ((value & 0x80) != 0) {

                irqMask |= value & 0x1F;

            } else {

                irqMask &= ~(value & 0x1F);
            }

            break;

        case 0x0E:

            timerARunning = (value & 1) != 0;

            if ((value & 0x10) != 0)
                timerA = latchA;

            break;

        case 0x0F:

            timerBRunning = (value & 1) != 0;

            if ((value & 0x10) != 0)
                timerB = latchB;

            break;
    }
}

public void clock() {

    clockTimerA();

    clockTimerB();
}

private void clockTimerA() {

    if (!timerARunning)
        return;

    timerA--;

    if (timerA < 0) {

        timerA = latchA;

        timerAUnderflow();
    }
}

private void clockTimerB() {

    if (!timerBRunning)
        return;

    timerB--;

    if (timerB < 0) {

        timerB = latchB;

        timerBUnderflow();
    }
}

private void timerAUnderflow() {

    registers[0x0D] |= 0x01;

    if ((irqMask & 0x01) != 0) {

        registers[0x0D] |= 0x80;

        bus.requestNMI();
    
    }
}

private void timerBUnderflow() {

    registers[0x0D] |= 0x02;

    if ((irqMask & 0x02) != 0) {

        registers[0x0D] |= 0x80;

        bus.requestNMI();
    }
}

private void updateVicBank(int value) {

    int bank = (~value) & 0x03;

    bus.setVicBank(bank);
}

public void reset() {

    java.util.Arrays.fill(registers, 0);

    timerA = 0xFFFF;
    timerB = 0xFFFF;

    latchA = 0xFFFF;
    latchB = 0xFFFF;

    irqMask = 0;

    timerARunning = false;
    timerBRunning = false;
}



}