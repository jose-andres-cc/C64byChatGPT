package com.example.c64.memory;

public interface Memory {

    int read(int address);

    void write(int address, int value);

    int size();
}
