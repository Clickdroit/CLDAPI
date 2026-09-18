package fr.clickdroit.api.game.combatlog;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

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
        spawnVillager(player);
    }

    public void spawnVillager(Player player) {
        this.entity = (Villager) this.location.getWorld().spawnEntity(this.location, EntityType.VILLAGER);

        setEntityNoAI(this.entity);
        this.entity.setMaxHealth(player.getMaxHealth());
        this.entity.setHealth(player.getHealth());
        this.entity.setCustomNameVisible(true);
        updateName();

        // Copier l'armure du joueur sur le villageois via NMS
        copyArmorToVillager(player);

        // En 1.8, pas de setProfession(Profession.NITWIT), on utilise l'ID
        this.entity.setProfession(Villager.Profession.FARMER);
    }

    /**
     * Copie l'armure du joueur sur le villageois via NMS (équipement slots)
     */
    private void copyArmorToVillager(Player player) {
        try {
            EntityLiving nmsEntity = ((CraftLivingEntity) this.entity).getHandle();
            ItemStack[] armor = player.getInventory().getArmorContents();

            // Slot 1 = bottes, 2 = jambières, 3 = plastron, 4 = casque
            for (int i = 0; i < armor.length; i++) {
                if (armor[i] != null) {
                    nmsEntity.setEquipment(i + 1, CraftItemStack.asNMSCopy(armor[i]));
                }
            }
        } catch (Exception e) {
            org.bukkit.Bukkit.getLogger().warning("Failed to copy armor to combat log villager: " + e.getMessage());
        }
    }

    public void updateName() {
        if (this.entity != null) {
            double hp = Math.round(this.entity.getHealth() * 10.0) / 10.0;
            this.entity.setCustomName("§8§l•§c§l" + this.name + " §c❤ " + hp);
        }
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
            org.bukkit.Bukkit.getLogger().severe("Failed to set NoAI for combat log entity: " + e.getMessage());
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

    /**
     * Retourne la vie actuelle du villageois
     */
    public double getCurrentHealth() {
        if (entity != null && !entity.isDead()) {
            return entity.getHealth();
        }
        return 0;
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
