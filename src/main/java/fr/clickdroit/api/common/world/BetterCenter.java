package fr.clickdroit.api.common.world;

import fr.clickdroit.api.utils.Reflection;
import net.minecraft.server.v1_8_R3.*;

import java.lang.reflect.Field;
import java.util.Random;

public class BetterCenter {
    public static void load() throws Exception {
        Field worldGenTreesField = BiomeBase.class.getDeclaredField("aA");
        worldGenTreesField.setAccessible(true);
        Field worldGenBigTreeField = BiomeBase.class.getDeclaredField("aB");
        worldGenBigTreeField.setAccessible(true);
        Field worldGenSwampTreeField = BiomeBase.class.getDeclaredField("aC");
        worldGenSwampTreeField.setAccessible(true);
        for (BiomeBase biome : BiomeBase.getBiomes()) {
            if (biome != null) {
                worldGenTreesField.set(biome, new WorldGenTreesPatched(false));
                worldGenBigTreeField.set(biome, new WorldGenBigTreePatched(false));
                worldGenSwampTreeField.set(biome, new WorldGenSwampTreePatched());
            }
        }
        Field worldGenForestFirstField = BiomeForest.class.getDeclaredField("aD");
        worldGenForestFirstField.setAccessible(true);
        Field worldGenForestSecondField = BiomeForest.class.getDeclaredField("aE");
        worldGenForestSecondField.setAccessible(true);
        Field worldGenForestTreeField = BiomeForest.class.getDeclaredField("aF");
        worldGenForestTreeField.setAccessible(true);
        Reflection.setFinalStatic(worldGenForestFirstField, new WorldGenForestPatched(false, true));
        Reflection.setFinalStatic(worldGenForestSecondField, new WorldGenForestPatched(false, false));
        Reflection.setFinalStatic(worldGenForestTreeField, new WorldGenForestTreePatched(false));
    }

    private static class WorldGenTreesPatched extends WorldGenTrees {
        WorldGenTreesPatched(boolean b) {
            super(b);
        }

        public boolean generate(World world, Random random, BlockPosition blockPosition) {
            return ((blockPosition.getX() <= -64 || blockPosition.getX() >= 64 || blockPosition.getZ() <= -64 || blockPosition.getZ() >= 64) && super.generate(world, random, blockPosition));
        }
    }

    private static class WorldGenBigTreePatched extends WorldGenBigTree {
        WorldGenBigTreePatched(boolean b) {
            super(b);
        }

        public boolean generate(World world, Random random, BlockPosition blockPosition) {
            return ((blockPosition.getX() <= -64 || blockPosition.getX() >= 64 || blockPosition.getZ() <= -64 || blockPosition.getZ() >= 64) && super.generate(world, random, blockPosition));
        }
    }

    private static class WorldGenSwampTreePatched extends WorldGenSwampTree {
        WorldGenSwampTreePatched() {
            super();
        }

        public boolean generate(World world, Random random, BlockPosition blockPosition) {
            return ((blockPosition.getX() <= -64 || blockPosition.getX() >= 64 || blockPosition.getZ() <= -64 || blockPosition.getZ() >= 64) && super.generate(world, random, blockPosition));
        }
    }

    private static class WorldGenForestPatched extends WorldGenForest {
        WorldGenForestPatched(boolean b1, boolean b2) {
            super(b1, b2);
        }

        public boolean generate(World world, Random random, BlockPosition blockPosition) {
            return ((blockPosition.getX() <= -64 || blockPosition.getX() >= 64 || blockPosition.getZ() <= -64 || blockPosition.getZ() >= 64) && super.generate(world, random, blockPosition));
        }
    }

    private static class WorldGenForestTreePatched extends WorldGenTrees {
        WorldGenForestTreePatched(boolean b) {
            super(b);
        }

        public boolean generate(World world, Random random, BlockPosition blockPosition) {
            return ((blockPosition.getX() <= -64 || blockPosition.getX() >= 64 || blockPosition.getZ() <= -64 || blockPosition.getZ() >= 64) && super.generate(world, random, blockPosition));
        }
    }
}
