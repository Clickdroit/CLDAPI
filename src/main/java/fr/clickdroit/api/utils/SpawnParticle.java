package fr.clickdroit.api.utils;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnParticle {
    public static void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
        EnumParticle particle = EnumParticle.valueOf(type);
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particle, false, x, y, z, offsetX, offsetY, offsetZ, data, amount, new int[] { 1 });
        for (Player player : world.getPlayers())
            Reflection.sendPacket(player, particles);
    }
}
