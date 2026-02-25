package fr.clickdroit.api.game;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.common.rules.items.GeneralRules;
import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.utils.item.ItemCreator;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Random;

public class GameListener implements Listener {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    private final World world;

    private final Random random;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
        this.world = gameManager.getWorldPopulator().getGameWorld();
        this.random = new Random();
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < 4; i++) {
            if (event.getLine(i).matches("^[a-zA-Z0-9ÀÁÂÄÇÈÉÊËÎÍÎÏÒÓÕÖÛÛÛÛàáàäçèéëäîïôöûü &]*$")
                    && event.getLine(i).length() > 20)
                event.setCancelled(true);
        }
    }

    private int getRandInt(int max, int min) {
        return this.random.nextInt(max - min + 1) + min;
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event) {
        String msgToLowerCase = event.getMessage().toLowerCase();
        if (!GeneralRules.PRIVATEMSG.isEnabled())
            switch (msgToLowerCase) {
                case "/msg":
                case "/m":
                case "/t":
                case "/tell":
                case "/w":
                case "/whisper":
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cLes messages privés sont désactivés.");
                    break;
            }
    }

    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            int damage = this.gameConfig.getEnderpearlDamage();
            event.setCancelled(true);
            player.teleport(event.getTo());
            if (damage > 0)
                player.damage(damage);
        }
    }
}
