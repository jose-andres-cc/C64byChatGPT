package com.example.c64.memory;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ROM implements Memory {

    protected byte[] data;

    protected ROM(int size) {

        data = new byte[size];
    }

    protected ROM(Path path, int expectedSize)
        throws IOException {

        data = Files.readAllBytes(path);

        if (data.length != expectedSize) {

            throw new IllegalArgumentException(
                "ROM size mismatch. Expected "
                    + expectedSize
                    + " bytes but found "
                    + data.length);
        }
    }

    @Override
    public int read(int address) {

        return data[address] & 0xFF;
    }

    @Override
    public void write(int address, int value) {

        // ROM: ignorar escritura
    }

    @Override
    public int size() {

        return data.length;
    }

    public void load(Path path)
        throws IOException {

        byte[] rom = Files.readAllBytes(path);

        if (rom.length != data.length) {

            throw new IllegalArgumentException(
                "ROM size mismatch");
        }

        System.arraycopy(
            rom,
            0,
            data,
            0,
            rom.length);
    }
}