package com.example.c64.memory;


import java.io.IOException;
import java.nio.file.Path;

public class CharacterROM extends ROM {

    public static final int SIZE = 0x1000;

    public CharacterROM() {

        super(SIZE);
    }

    public CharacterROM(Path path)
        throws IOException {

        super(path, SIZE);
    }
}
