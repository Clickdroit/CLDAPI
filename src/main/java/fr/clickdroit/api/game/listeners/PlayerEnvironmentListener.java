package fr.clickdroit.api.game.listeners;

import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class PlayerEnvironmentListener implements Listener {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    public PlayerEnvironmentListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (event.getFoodLevel() < player.getFoodLevel()) {
            player.setSaturation(10.0F);
            player.setExhaustion(0.0F);
        }
    }

    @EventHandler
    private void onFood(FoodLevelChangeEvent event) {
        if (!GameUtils.isGameStarted() || Rules.noDamage.isActive())
            event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!GameUtils.isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if (!GameUtils.isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerPortalEvent(PlayerPortalEvent event) {
        if (!this.gameConfig.isNether()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cLe Nether est désactivé.");
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        ItemStack item = event.getInventory().getResult();
        if (item == null)
            return;
        for (UseItems items : UseItems.values()) {
            if (items.equals(UseItems.FISHINGROD) &&
                    !items.isEnabled() && item
                            .getType() == items.getMaterial())
                event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getBucket().equals(Material.LAVA_BUCKET) && !Rules.pvp.isActive()) {
            event.getPlayer().sendMessage("§cLe PvP est désactivé, l'utilisation de sources de lave est interdite.");
            event.getPlayer().getWorld()
                    .getBlockAt(event.getBlockClicked().getLocation().add(event.getBlockFace().getModX(),
                            event.getBlockFace().getModY(), event.getBlockFace().getModZ()))
                    .setType(Material.AIR);
            event.getPlayer().getInventory().getItemInHand().setType(Material.LAVA_BUCKET);
            event.setCancelled(true);
        }
    }
}
