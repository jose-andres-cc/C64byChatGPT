
package com.example.c64.memory;

public class Memory_no_bus {

    private byte[] ram = new byte[65536];
    private byte[] basic = new byte[8192];
    private byte[] kernal = new byte[8192];

    public void loadBasic(byte[] data) {
        System.arraycopy(data, 0, basic, 0, 8192);
    }

    public void loadKernal(byte[] data) {
        System.arraycopy(data, 0, kernal, 0, 8192);
    }

    public byte read(int addr) {
        addr &= 0xFFFF;

        if (addr >= 0xA000 && addr <= 0xBFFF)
            return basic[addr - 0xA000];

        if (addr >= 0xE000)
            return kernal[addr - 0xE000];

        return ram[addr];
    }

    public void write(int addr, byte val) {
        addr &= 0xFFFF;

        if (addr >= 0xA000 && addr <= 0xBFFF) return;
        if (addr >= 0xE000) return;

        ram[addr] = val;
    }
}