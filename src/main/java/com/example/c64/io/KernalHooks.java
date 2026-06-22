package com.example.c64.io;

import com.example.c64.cpu.CPU6510;

public class KernalHooks {

    public boolean handle(CPU6510 cpu) {

        // CHROUT
        if (cpu.PC == 0xFFD2) {
            System.out.print((char)cpu.A);

            cpu.PC = cpuPopWord(cpu);
            return true;
        }

        return false;
    }

    private int cpuPopWord(CPU6510 cpu) {
        try {
            var m = CPU6510.class.getDeclaredMethod("popWord");
            m.setAccessible(true);
            return (int)m.invoke(cpu);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}