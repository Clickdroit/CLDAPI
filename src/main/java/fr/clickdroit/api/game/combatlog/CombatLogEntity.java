package fr.clickdroit.api.game.combatlog;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.UUID;

public class CombatLogEntity {
    private final UUID uuid;
    private final String name;
    private final Location location;
    private Villager entity;

    public CombatLogEntity(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.location = player.getLocation();
        spawnVillager();
    }

    public void spawnVillager() {
        this.entity = (Villager) this.location.getWorld().spawnEntity(this.location, EntityType.VILLAGER);

        setEntityNoAI(this.entity);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName("§8§l•§c§l" + this.name);

        // En 1.8, pas de setProfession(Profession.NITWIT), on utilise l'ID
        this.entity.setProfession(Villager.Profession.FARMER); // ou autre profession disponible en 1.8
    }

    private void setEntityNoAI(org.bukkit.entity.Entity entity) {
        try {
            net.minecraft.server.v1_8_R3.Entity nms = ((CraftEntity) entity).getHandle();
            NBTTagCompound tag = new NBTTagCompound();
            nms.c(tag);
            tag.setBoolean("NoAI", true);
            tag.setBoolean("Silent", true);
            tag.setBoolean("Invulnerable", false);
            tag.setBoolean("PersistenceRequired", true);
            ((EntityLiving) nms).a(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Villager getEntity() {
        return entity;
    }

    // Méthode pour supprimer l'entité
    public void remove() {
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }

    // Méthodes spécifiques pour Minecraft 1.8
    public void setHealth(double health) {
        if (entity != null) {
            entity.setHealth(health);
        }
    }

    public void damage(double damage) {
        if (entity != null) {
            entity.damage(damage);
        }
    }

    public boolean isDead() {
        return entity == null || entity.isDead();
    }
}