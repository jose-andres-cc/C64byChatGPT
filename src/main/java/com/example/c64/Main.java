package com.example.c64;

import com.example.c64.cpu.CPU6510;
import com.example.c64.io.KernalHooks;
import com.example.c64.bus.Bus;
import com.example.c64.util.RomLoader;
import com.example.c64.video.VICII;

public class Main {

    public static void main(String[] args)
        throws Exception {

        //Memory mem = new Memory();
        Bus bus = new Bus();

    C64 c64 = new C64();

    c64.initialize();

bus.getRam().write(0x0400, (byte)'H');
bus.getRam().write(0x0401, (byte)'E');
bus.getRam().write(0x0402, (byte)'L');
bus.getRam().write(0x0403, (byte)'L');
bus.getRam().write(0x0404, (byte)'O');


    c64.run();

        //bus.getBasicRom().load("roms/basic_generic.rom");

        // mem.loadBasic(
        //     RomLoader.load("basic.rom")
        // );

        //bus.getKernalRom().load("roms/kernal_generic.rom");

        // KernalHooks hooks =
        //     new KernalHooks();

        // CPU6510 cpu =
        //     new CPU6510(bus, hooks);

        // VICII vic =
        //     new VICII(bus);

        //cpu.reset();

        while (true) {

bus.getRam().write(0x0400, (byte)'H');
bus.getRam().write(0x0401, (byte)'E');
bus.getRam().write(0x0402, (byte)'L');
bus.getRam().write(0x0403, (byte)'L');
bus.getRam().write(0x0404, (byte)'O');
// vic.renderFrame();

//             // Ejecutar CPU
//             for (int i = 0; i < 1000; i++) {
//                 cpu.step();
//             }

//             // Render
//             vic.renderFrame();

//             Thread.sleep(16);
         }
     }
 }






// public class Main {

//     public static void main(String[] args) throws Exception {

//         Memory mem = new Memory();

//         mem.loadBasic(RomLoader.load("basic.rom"));
//         mem.loadKernal(RomLoader.load("kernal.rom"));

//         KernalHooks hooks = new KernalHooks();
//         CPU6510 cpu = new CPU6510(mem, hooks);

//         cpu.reset();

//         while (true) {
//             cpu.step();
//         }
//     }
// }



// package com.example.c64;

// import com.example.c64.cpu.CPU6510;
// import com.example.c64.memory.Memory;

// public class Main {
//     public static void main(String[] args) {
//         Memory mem = new Memory();
//         CPU6510 cpu = new CPU6510(mem);

//         mem.write(0x8000,(byte)0xA2);
//         mem.write(0x8001,(byte)0x10);
//         mem.write(0x8002,(byte)0xE8);
//         mem.write(0x8003,(byte)0xEA);

//         cpu.reset(0x8000);

//         for(int i=0;i<3;i++) cpu.step();

//         System.out.println("X=" + cpu.X);
//     }
// }


