package com.example.c64.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class RomLoader {

    public static byte[] load(String path) throws Exception {
        return Files.readAllBytes(Path.of(path));
    }
}
