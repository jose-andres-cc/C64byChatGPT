
package com.example.c64.memory;

public class Memory {
    private final byte[] ram = new byte[65536];

    public byte read(int address) {
        return ram[address & 0xFFFF];
    }

    public void write(int address, byte value) {
        ram[address & 0xFFFF] = value;
    }
}
