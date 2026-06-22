
package com.example.c64.cpu;

import com.example.c64.memory.Memory;

public class CPU6510 {
    public int A,X,Y,PC,SP=0xFF;
    public boolean C,Z,I,D,B,V,N;
    private final Memory memory;

    public CPU6510(Memory memory){ this.memory = memory; }

    private int read(int a){ return memory.read(a)&0xFF; }
    private void write(int a,int v){ memory.write(a,(byte)(v&0xFF)); }

    private int readWord(int a){
        int lo=read(a); int hi=read(a+1);
        return (hi<<8)|lo;
    }

    //public void reset(int start){ PC=start; }
public void reset() {

    SP = 0xFD;

    setStatusRegister(0x24);

    PC = readVector(0xFFFC);

//cpuPortDataDirection = 0x2F;
//cpuPortData = 0x37;

}

    private void setZN(int v){
        Z=(v&0xFF)==0;
        N=(v&0x80)!=0;
    }

    private void setStatusRegister(int p) {
        p |= 0x20;

        C = (p & 0x01) != 0;
        Z = (p & 0x02) != 0;
        I = (p & 0x04) != 0;
        D = (p & 0x08) != 0;
        B = (p & 0x10) != 0;
        V = (p & 0x40) != 0;
        N = (p & 0x80) != 0;
    }    
    
    private int readVector(int address) {

    int lo = read(address);
    int hi = read(address + 1);

    return (hi << 8) | lo;
}
    private int imm(){ return read(PC++); }
    private int zp(){ return read(PC++); }
    private int zpx(){ return (read(PC++)+X)&0xFF; }
    private int zpy(){ return (read(PC++)+Y)&0xFF; }
    private int abs(){ int a=readWord(PC); PC+=2; return a; }
    private int absX(){ int a=readWord(PC); PC+=2; return (a+X)&0xFFFF; }
    private int absY(){ int a=readWord(PC); PC+=2; return (a+Y)&0xFFFF; }

    private int indX(){
        int zp=(read(PC++)+X)&0xFF;
        int lo=read(zp); int hi=read((zp+1)&0xFF);
        return (hi<<8)|lo;
    }

    private int indY(){
        int zp=read(PC++);
        int lo=read(zp); int hi=read((zp+1)&0xFF);
        return (((hi<<8)|lo)+Y)&0xFFFF;
    }

    private int fetch(int addr){ return read(addr); }

    private void push(int v){ write(0x100+SP--,v); }
    private int pop(){ return read(0x100 + ++SP); }

    private int popWord(){
        int lo=pop(); int hi=pop();
        return (hi<<8)|lo;
    }

    private void compare(int reg,int value){
        int result=(reg-value)&0x1FF;
        C=reg>=value;
        Z=(reg&0xFF)==(value&0xFF);
        N=(result&0x80)!=0;
    }

    private void branch(boolean cond){
        int offset=read(PC++);
        if(offset>127) offset-=256;
        if(cond) PC=(PC+offset)&0xFFFF;
    }

    public void step(){
        int op=read(PC++);

        switch(op){
            case 0xA9 -> { A=imm(); setZN(A); }
            case 0xA5 -> { A=fetch(zp()); setZN(A); }
            case 0xAD -> { A=fetch(abs()); setZN(A); }

            case 0xA2 -> { X=imm(); setZN(X); }
            case 0xA6 -> { X=fetch(zp()); setZN(X); }
            case 0xB6 -> { X=fetch(zpy()); setZN(X); }
            case 0xAE -> { X=fetch(abs()); setZN(X); }
            case 0xBE -> { X=fetch(absY()); setZN(X); }

            case 0xA0 -> { Y=imm(); setZN(Y); }
            case 0xA4 -> { Y=fetch(zp()); setZN(Y); }
            case 0xB4 -> { Y=fetch(zpx()); setZN(Y); }
            case 0xAC -> { Y=fetch(abs()); setZN(Y); }
            case 0xBC -> { Y=fetch(absX()); setZN(Y); }

            case 0x8D -> write(abs(),A);

            case 0xAA -> { X=A; setZN(X); }
            case 0xE8 -> { X=(X+1)&0xFF; setZN(X); }

            case 0x20 -> {
                int addr=abs();
                push((PC-1)>>8);
                push((PC-1)&0xFF);
                PC=addr;
            }

            case 0x60 -> PC=popWord()+1;

            case 0xC9 -> compare(A,imm());
            case 0xC5 -> compare(A,fetch(zp()));
            case 0xD5 -> compare(A,fetch(zpx()));
            case 0xCD -> compare(A,fetch(abs()));
            case 0xDD -> compare(A,fetch(absX()));
            case 0xD9 -> compare(A,fetch(absY()));
            case 0xC1 -> compare(A,fetch(indX()));
            case 0xD1 -> compare(A,fetch(indY()));

            case 0xE0 -> compare(X,imm());
            case 0xE4 -> compare(X,fetch(zp()));
            case 0xEC -> compare(X,fetch(abs()));

            case 0xC0 -> compare(Y,imm());
            case 0xC4 -> compare(Y,fetch(zp()));
            case 0xCC -> compare(Y,fetch(abs()));

            case 0x10 -> branch(!N);
            case 0x30 -> branch(N);
            case 0x50 -> branch(!V);
            case 0x70 -> branch(V);
            case 0x90 -> branch(!C);
            case 0xB0 -> branch(C);
            case 0xD0 -> branch(!Z);
            case 0xF0 -> branch(Z);

            case 0x4C -> PC=abs();
            case 0xEA -> {}

            default -> throw new RuntimeException(String.format("Opcode %02X no implementado", op));
        }
    }
}
