package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class NineSlots extends ScenarioManager implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (API.getAPI().getGameManager().getGameState().equals(GameState.PLAYING)) {
            Player player = (Player)event.getWhoClicked();
            ItemStack itemStack = event.getCurrentItem();
            if (player == null || event.getInventory() == null || itemStack == null || event.getAction() == null)
                return;
            if (itemStack.hasItemMeta() && itemStack
                    .getType() == Material.BARRIER)
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onDeath(PlayerDeathEvent event) {
        event.getDrops().remove(Material.BARRIER);
    }

    public void configure() {
        this.scenario = Scenario.NINE_SLOTS;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {
        ItemStack item = (new ItemCreator(Material.BARRIER)).setName("§cSlot verouillé").getItem();
        for (int i = 9; i < 36; i++) {
            for (UUID uuid : API.getAPI().getGameManager().getInGamePlayers())
                Bukkit.getPlayer(uuid).getInventory().setItem(i, item);
        }
    }
}
