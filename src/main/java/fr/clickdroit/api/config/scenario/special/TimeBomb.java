package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeBomb extends ScenarioManager implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    private void onDeath(PlayerDeathEvent event) {
        if (this.scenario.isEnabled()) {
            Player player = event.getEntity();
            final Location location = player.getLocation();
            Block block = location.getBlock();
            block = block.getRelative(BlockFace.DOWN);
            block.setType(Material.CHEST);
            Chest chest = (Chest)block.getState();
            block = block.getRelative(BlockFace.NORTH);
            block.setType(Material.CHEST);
            for (ItemStack item : event.getDrops()) {
                if (item == null || item
                        .getType() == Material.AIR)
                    continue;
                chest.getInventory().addItem(new ItemStack[] { item });
            }
            event.getDrops().clear();
            final ArmorStand armorStand = (ArmorStand)player.getWorld().spawn(chest.getLocation().clone().add(0.5D, 1.0D, 0.0D), ArmorStand.class);
            armorStand.setCustomNameVisible(true);
            armorStand.setSmall(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.setCustomNameVisible(true);
            (new BukkitRunnable() {
                private int time = TimeBomb.this.scenario.getValue();

                public void run() {
                    armorStand.setCustomName("dans : "+ this.time);
                    if (this.time <= 0) {
                        location.getBlock().setType(Material.AIR);
                        location.getBlock().getRelative(BlockFace.NORTH).setType(Material.AIR);
                        location.getWorld().createExplosion(location, 5.0F);
                        armorStand.remove();
                        for (Entity entity : location.getWorld().getEntitiesByClass(Item.class)) {
                            if (entity.getLocation().distance(location) <= 5.0D)
                                entity.remove();
                        }
                        cancel();
                    }
                    this.time--;
                }
            }).runTaskTimer((Plugin)API.getAPI(), 0L, 20L);
        }
    }

    public void configure() {
        this.scenario = Scenario.TIMEBOMB;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}

