package com.example.c64.memory;


public class RAM implements Memory {

    private final byte[] data;

    public RAM(int size) {

        this.data = new byte[size];
    }

    @Override
    public int read(int address) {

        return data[address & (data.length - 1)] & 0xFF;
    }

    @Override
    public void write(int address, int value) {

        data[address & (data.length - 1)] =
            (byte)(value & 0xFF);
    }

    @Override
    public int size() {

        return data.length;
    }

    public void clear() {

        java.util.Arrays.fill(data, (byte)0);
    }

    public byte[] getRawArray() {

        return data;
    }
}