package com.ezabenco.mcwebtool.map;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.List;

public class ChunkScanner {

    // Returns a 16x16 array of block type names for the chunk's surface
    public static String[][] scanChunkSurface(Chunk chunk) {
        String[][] result = new String[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getWorld().getMaxHeight() - 1; y >= 0; y--) {
                    Block block = chunk.getBlock(x, y, z);
                    if (!block.getType().isAir()) {
                        result[x][z] = block.getType().name(); // e.g., "GRASS_BLOCK"
                        break;
                    }
                }
            }
        }
        return result;
    }
}
