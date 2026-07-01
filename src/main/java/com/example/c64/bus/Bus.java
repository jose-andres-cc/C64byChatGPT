package com.example.c64.bus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.example.c64.cpu.CPU6510;
import com.example.c64.memory.BasicROM;
import com.example.c64.memory.CharacterROM;
import com.example.c64.memory.KernalROM;
import com.example.c64.memory.RAM;
import com.example.c64.video.VICII;
import com.example.c64.cia.CIA1;
import com.example.c64.cia.CIA2;

public class Bus {

    //private final byte[] ram = new byte[65536];

    //private byte[] basicRom;
    //private byte[] kernalRom;
    //private byte[] charRom;

    private int cpuPortDataDirection = 0x2F;
    private int cpuPortData = 0x37;
    private int cpuPortDDR  = 0x2F;
//    private int cpuPortData = 0x37;



    private final RAM ram;

    private final BasicROM basicRom;

    private final KernalROM kernalRom;

    private final CharacterROM charRom;

    private VICII vic;

    private CIA1 cia1;

    private CIA2 cia2;

private int vicBank = 3;    

private CPU6510 cpu;

    public Bus() {

        ram = new RAM(65536);

        basicRom = new BasicROM();

        kernalRom = new KernalROM();

        charRom = new CharacterROM();
    }

    public void connectVIC(VICII vic) {
        this.vic = vic;
    }

     public void connectCIA1(CIA1 cia1) {
         this.cia1 = cia1;
     }

     public void connectCIA2(CIA2 cia2) {
         this.cia2 = cia2;
     }

public void connectCPU(CPU6510 cpu) {

    this.cpu = cpu;
}

public void requestIRQ() {

    cpu.irq();
}

public void requestNMI() {

    cpu.nmi();
}

    public int read(int address) {

        address &= 0xFFFF;

        //
        // CPU PORT
        //

        if (address == 0x0000) {
            return cpuPortDDR;
        }

        if (address == 0x0001) {
            return cpuPortData;
        }

        //
        // BASIC ROM
        //

        if (address >= 0xA000 && address <= 0xBFFF) {

            if (loram() && hiram()) {

                return basicRom.read(
                    address - 0xA000
                );
            }
        }

        //
        // D000-DFFF
        //

        if (address >= 0xD000 && address <= 0xDFFF) {

            //
            // I/O visible
            //

            if (charen()) {

                return readIO(address);
            }

            //
            // Character ROM visible
            //

            return charRom.read(
                address - 0xD000
            );
        }

        //
        // KERNAL ROM
        //

        if (address >= 0xE000 && address <= 0xFFFF) {

            if (hiram()) {

                return kernalRom.read(
                    address - 0xE000
                );
            }
        }

        //
        // RAM
        //

        return ram.read(address);
    }

    public void write(int address, int value) {

        address &= 0xFFFF;
        value   &= 0xFF;

        //
        // CPU PORT
        //

        if (address == 0x0000) {

            cpuPortDDR = value;
            return;
        }

        if (address == 0x0001) {

            cpuPortData = value;
            return;
        }

        //
        // D000-DFFF
        //

        if (address >= 0xD000 && address <= 0xDFFF) {

            if (charen()) {

                writeIO(address, value);
            }
        }

        //
        // Siempre escribir en RAM
        //

        ram.write(address, value);
    }

    private int readIO(int address) {

         if (vic != null &&
             address >= 0xD000 &&
             address <= 0xD3FF) {

             return vic.read(address);
         }

         if (cia1 != null &&
             address >= 0xDC00 &&
             address <= 0xDCFF) {

             return cia1.read(address);
         }

        if (cia2 != null &&
            address >= 0xDD00 &&
            address <= 0xDDFF) {

            return cia2.read(address);
        }

        return 0xFF;
    }

    private void writeIO(int address, int value) {

        if (vic != null &&
            address >= 0xD000 &&
            address <= 0xD3FF) {

            vic.write(address, value);
            return;
        }

        if (cia1 != null &&
            address >= 0xDC00 &&
            address <= 0xDCFF) {

            cia1.write(address, value);
            return;
        }

        if (cia2 != null &&
            address >= 0xDD00 &&
            address <= 0xDDFF) {

            cia2.write(address, value);
        }
    }

    public RAM getRam() {
        return ram;
    }

    public BasicROM getBasicRom() {
        return basicRom;
    }

    public KernalROM getKernalRom() {
        return kernalRom;
    }

    public CharacterROM getCharacterRom() {
        return charRom;
    }
        

// Helpers
private boolean loram() {
    return (cpuPortData & 0x01) != 0;
}

private boolean hiram() {
    return (cpuPortData & 0x02) != 0;
}

private boolean charen() {
    return (cpuPortData & 0x04) != 0;
}

// Carga de ROMs
// public void loadBasicRom(Path path) throws IOException {

//     basicRom = Files.readAllBytes(path);

//     if(basicRom.length != 0x2000) {
//         throw new IllegalArgumentException(
//             "BASIC ROM must be 8 KB");
//     }
// }
//public void loadKernalRom(Path path)
//public void loadCharRom(Path path)

public void loadRoms(
        Path basic,
        Path kernal,
        Path chars)
        throws IOException {

    basicRom.load(basic);

    kernalRom.load(kernal);

    charRom.load(chars);
}

public void setVicBank(int bank) {

    vicBank = bank & 0x03;
}

public int getVicBank() {

    return vicBank;
}


public int readVic(int address) {

    address &= 0x3FFF;

    int physicalAddress = (vicBank << 14) | address;

    return ram.read(physicalAddress);
}



}
