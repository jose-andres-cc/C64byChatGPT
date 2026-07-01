package com.example.c64;

import java.io.IOException;
import java.nio.file.Paths;

import com.example.c64.bus.Bus;
import com.example.c64.cpu.CPU6510;
import com.example.c64.io.KernalHooks;
import com.example.c64.video.VICII;
import com.example.c64.cia.CIA1;
import com.example.c64.cia.CIA2;

// Proyecto maven
// src/main/java/
// └── com/tuempresa/c64/
//     │
//     ├── C64.java
//     │
//     ├── bus/
//     │   ├── Bus.java
//     │   ├── MemoryMap.java
//     │   └── BankSwitcher.java
//     │
//     ├── cpu/
//     │   ├── CPU6510.java
//     │   ├── Opcode.java
//     │   ├── AddressingModes.java
//     │   └── InstructionSet.java
//     │
//     ├── vic/
//     │   ├── VICII.java
//     │   ├── Screen.java
//     │   └── Sprite.java
//     │
//     ├── cia/
//     │   ├── CIA1.java
//     │   └── CIA2.java
//     │
//     ├── memory/
//     │   ├── RAM.java
//     │   ├── ROM.java
//     │   ├── BasicROM.java
//     │   ├── KernalROM.java
//     │   └── CharacterROM.java
//     │
//     ├── io/
//     │   ├── KeyboardMatrix.java
//     │   └── Joystick.java
//     │
//     └── ui/
//         ├── EmulatorWindow.java
//         └── SwingScreen.java

// Relaciones
        //         +--------+
        //         |  C64   |
        //         +--------+
        //              |
        //              v
        //         +--------+
        //         |  Bus   |
        //         +--------+
        //          /  |   \
        //         /   |    \
        //        v    v     v

        //  CPU6510  VICII  CIA1/CIA2
        //        \    |     /
        //         \   |    /
        //          v  v   v

        //      RAM / ROM

public class C64 {

    private final Bus bus;

    private final CPU6510 cpu;

    private final VICII vic;

    private final CIA1 cia1;

     private final CIA2 cia2;

private volatile boolean running;

    public C64() {

        bus = new Bus();

// bus.loadRoms(
//     Paths.get("roms/basic.rom"),
//     Paths.get("roms/kernal.rom"),
//     Paths.get("roms/characters.rom")
// );

         vic = new VICII(bus);
        

        cia1 = new CIA1(bus);

         cia2 = new CIA2(bus);

        KernalHooks hooks =
            new KernalHooks();

         cpu = new CPU6510(bus, hooks);
         cpu.reset(); // Viene de main

         bus.connectCPU(cpu);
         bus.connectVIC(vic);
         bus.connectCIA1(cia1);
         bus.connectCIA2(cia2);
    }

    public void initialize() {

        try {

            bus.loadRoms(
                Paths.get("roms/basic_generic.rom"),
                Paths.get("roms/kernal_generic.rom"),
                Paths.get("roms/chargen_openroms.rom")
            );

        } catch (IOException e) {

            throw new RuntimeException(
                "ROM loading failed" + e.getMessage(),
                e
            );
        }
    }


    public void run() {

        running = true;

        cpu.reset();

        while (running) {

            clock();
        }
    }


// public void run() {

//     running = true;

//     cpu.reset();

//     long frameStart;

//     while (running) {

//         frameStart = System.nanoTime();

//         while (!vic.isFrameComplete()) {

//             clock();
//         }

//         vic.clearFrameFlag();

//         screen.repaint();

//         syncFrame(frameStart);
//     }
// }


private void syncFrame(long frameStart) {

    long frameTime =
        20_000_000L;

    long elapsed =
        System.nanoTime() - frameStart;

    long remaining =
        frameTime - elapsed;

    if (remaining > 0) {

        try {

            Thread.sleep(
                remaining / 1_000_000L,
                (int)(remaining % 1_000_000L)
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}


public void stop() {

    running = false;
}


// CPU   ≈ 0.985 MHz
// VIC-II ≈ 7.88 MHz

        public void clock() {

long before = cpu.getTotalCycles();

cpu.clock();

     long consumed =
         cpu.getTotalCycles() - before;

     for (long i = 0; i < consumed * 8; i++) {

         vic.clock();
         // Temporal
         vic.renderFrame();
     }

     for (long i = 0; i < consumed; i++) {

         cia1.clock();
         cia2.clock();
     }


    }



}
