
package com.example.c64;

import com.example.c64.cpu.CPU6510;
import com.example.c64.memory.Memory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CpuTest {
 @Test
 void testLdxImmediate() {
   Memory mem = new Memory();
   CPU6510 cpu = new CPU6510(mem);
   mem.write(0x8000,(byte)0xA2);
   mem.write(0x8001,(byte)0x42);
   cpu.reset(0x8000);
   cpu.step();
   assertEquals(0x42,cpu.X);
 }
}
