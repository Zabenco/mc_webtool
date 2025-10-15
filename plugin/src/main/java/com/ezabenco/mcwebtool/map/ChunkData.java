package com.ezabenco.mcwebtool.map;

public class ChunkData {
    public int cx, cz;
    public String[][] blocks; // or String[][] colors

    public ChunkData(int cx, int cz, String[][] blocks) {
        this.cx = cx;
        this.cz = cz;
        this.blocks = blocks;
    }
}
