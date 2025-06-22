package fr.clickdroit.api.worlds;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class Generator extends BlockPopulator {
    public void populate(World world, Random random, Chunk chunk) {
        for (int ii = -8; ii < 8; ii++) {
            for (int i = -8; i < 8; i++) {
                if (world.getChunkAt(i, ii).equals(chunk))
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            int y = world.getHighestBlockYAt(chunk.getX() * 16 + x, chunk.getZ() * 16 + z);
                            world.getBlockAt(chunk.getX() * 16 + x, y + 1, chunk.getZ() * 16 + z).setBiome(Biome.ROOFED_FOREST);
                        }
                    }
            }
        }
    }
}
