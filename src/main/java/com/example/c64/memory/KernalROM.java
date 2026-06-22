package com.example.c64.memory;


import java.io.IOException;
import java.nio.file.Path;

public class KernalROM extends ROM {

    public static final int SIZE = 0x2000;

    public KernalROM() {

        super(SIZE);
    }

    public KernalROM(Path path)
        throws IOException {

        super(path, SIZE);
    }
}
