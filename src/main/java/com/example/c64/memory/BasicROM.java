package com.example.c64.memory;


import java.io.IOException;
import java.nio.file.Path;

public class BasicROM extends ROM {

    public static final int SIZE = 0x2000;

    public BasicROM() {

        super(SIZE);
    }

    public BasicROM(Path path)
        throws IOException {

        super(path, SIZE);
    }
}
