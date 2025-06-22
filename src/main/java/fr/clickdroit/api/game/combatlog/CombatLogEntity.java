package fr.clickdroit.api.game.combatlog;


import java.util.UUID;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class CombatLogEntity {
    private final UUID uuid;

    private final String name;

    private final Location location;

    private Entity entity;

    public CombatLogEntity(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.location = player.getLocation();
        spawnVillager();
    }

    public void spawnVillager() {
        Entity entity = this.location.getWorld().spawnEntity(this.location, EntityType.VILLAGER);
        Villager villager = (Villager)entity;
        setEntityNoAI((Entity)villager);
        villager.setCustomNameVisible(true);
        villager.setCustomName(""+ this.name);
    }

    private void setEntityNoAI(Entity entity) {
        Entity nms = ((CraftEntity)entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nms.c(tag);
        tag.setBoolean("NoAI", true);
        EntityLiving entitys = (EntityLiving)nms;
        entitys.a(tag);
    }
}
