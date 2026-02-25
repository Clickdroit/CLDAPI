package fr.clickdroit.api.utils.nms;

import fr.clickdroit.api.API;
import fr.clickdroit.api.utils.Reflection;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BiomeDecorator;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.MobEffectList;
import org.bukkit.potion.PotionEffectType;

public class NMSPatcher {
    private Logger logger;

    public NMSPatcher(API api) {
        this.logger = api.getLogger();
        try {
            patchPotions();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Deprecated
    public void oldpatchBiomes() throws Exception {
        BiomeBase[] biomes = BiomeBase.getBiomes();
        Map<String, BiomeBase> biomesMap = BiomeBase.o;
        BiomeBase defaultBiome = BiomeBase.FOREST;
        fixAnimals();
        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("ad"), defaultBiome);
        for (int i = 0; i < biomes.length; i++) {
            if (biomes[i] != null) {
                if (!biomesMap.containsKey((biomes[i]).ah))
                    biomes[i] = defaultBiome;
                setReedsPerChunk(biomes[i], ((Integer)Reflection.getValue((biomes[i]).as, BiomeDecorator.class, true, "F")).intValue() * 2);
            }
        }
        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("biomes"), biomes);
    }

    public void patchPotions() throws Exception {
        Reflection.setFinalStatic(PotionEffectType.class.getDeclaredField("acceptingNew"), Boolean.valueOf(true));
        Field byIdField = Reflection.getField(PotionEffectType.class, true, "byId");
        Field byNameField = Reflection.getField(PotionEffectType.class, true, "byName");
        ((Map)byNameField.get(null)).remove("increase_damage");
        ((PotionEffectType[])byIdField.get(null))[5] = null;
        Reflection.setFinalStatic(MobEffectList.class.getDeclaredField("INCREASE_DAMAGE"), (new PotionAttackDamageNerf(5, new MinecraftKey("strength"), false, 9643043)).c("potion.damageBoost").a(GenericAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2));
        this.logger.info("Correction des effets de Force (130% => 26%, 260% => 52%)");
    }

    private void fixAnimals() throws Exception {
        addAnimalsSpawn("PLAINS", BiomeBase.PLAINS);
        addAnimalsSpawn("DESERT", BiomeBase.DESERT);
        addAnimalsSpawn("EXTREME_HILLS", BiomeBase.EXTREME_HILLS);
        addAnimalsSpawn("FOREST", BiomeBase.FOREST);
        addAnimalsSpawn("TAIGA", BiomeBase.TAIGA);
        addAnimalsSpawn("EXTREME_HILLS_PLUS", BiomeBase.EXTREME_HILLS_PLUS);
        addAnimalsSpawn("SAVANNA", BiomeBase.SAVANNA);
        addAnimalsSpawn("SAVANNA_PLATEAU", BiomeBase.SAVANNA_PLATEAU);
        addAnimalsSpawn("MESA", BiomeBase.MESA);
        addAnimalsSpawn("MESA_PLATEAU_F", BiomeBase.MESA_PLATEAU_F);
        addAnimalsSpawn("MESA_PLATEAU", BiomeBase.MESA_PLATEAU);
        addAnimalsSpawn("FOREST", BiomeBase.FOREST);
    }

    private void addAnimalsSpawn(String name, BiomeBase biomeBase) throws Exception {
        Field biome = BiomeBase.class.getDeclaredField(name);
        Field defaultMobField = BiomeBase.class.getDeclaredField("au");
        defaultMobField.setAccessible(true);
        ArrayList<BiomeBase.BiomeMeta> mobs = new ArrayList<>();
        mobs.add(new BiomeBase.BiomeMeta(EntitySheep.class, 15, 10, 10));
        mobs.add(new BiomeBase.BiomeMeta(EntityRabbit.class, 4, 3, 5));
        mobs.add(new BiomeBase.BiomeMeta(EntityPig.class, 15, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityChicken.class, 20, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityCow.class, 20, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityWolf.class, 5, 5, 10));
        defaultMobField.set(biomeBase, mobs);
        Reflection.setFinalStatic(biome, biomeBase);
    }

    private void setReedsPerChunk(BiomeBase biome, int value) throws NoSuchFieldException, IllegalAccessException {
        Reflection.setValue(biome.as, BiomeDecorator.class, true, "F", Integer.valueOf(value));
    }
}
