package fr.clickdroit.api.config.scenario.special.runnable;

import fr.clickdroit.api.API;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MasterAppleRunnable extends BukkitRunnable {
    public void run() {
        for (UUID uuid : API.getAPI().getGameManager().getInGamePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.GOLDEN_APPLE)).setDurability(Integer.valueOf(1)).getItem() });
        }
    }
}
