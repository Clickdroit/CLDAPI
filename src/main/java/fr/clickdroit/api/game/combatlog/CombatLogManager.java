package fr.clickdroit.api.game.combatlog;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.CombatUtilsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogManager implements Listener {
    private final GameManager gameManager;
    private static final long COMBAT_TIME = 60L; // 60 secondes de combat

    public CombatLogManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        onLogout(event.getPlayer());
    }

    public void onLogout(Player player) {
        if (!GameUtils.isGameStarted())
            return;

        if (this.gameManager.getInGamePlayers().contains(player.getUniqueId())) {
            GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
            if (gamePlayer == null)
                return;

            long lastFight = CombatUtilsAPI.getTimeBeforeLastFight(gamePlayer);
            if (lastFight <= COMBAT_TIME) {
                CombatLogEntity combatLog = new CombatLogEntity(player);
                gamePlayer.setCombatLogEntity(combatLog);

                this.gameManager.broadcastWithPrefix("§c" + gamePlayer.getName() + " est mort en étant déconnecté !");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCombatLogDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.Villager))
            return;

        org.bukkit.entity.Villager villager = (org.bukkit.entity.Villager) event.getEntity();

        for (GamePlayer gamePlayer : GamePlayer.getGamePlayers()) {
            CombatLogEntity combatLog = gamePlayer.getCombatLogEntity();
            if (combatLog != null && combatLog.getEntity().equals(villager)) {
                handleCombatLogDeath(gamePlayer, combatLog);
                break;
            }
        }
    }

    private void handleCombatLogDeath(GamePlayer gamePlayer, CombatLogEntity combatLog) {
        combatLog.remove();
        gamePlayer.setCombatLogEntity(null);

        gamePlayer.setAlive(false);
        this.gameManager.getInGamePlayers().remove(gamePlayer.getUuid());

        this.gameManager.broadcastWithPrefix("§c" + gamePlayer.getName() + " est mort en étant déconnecté !");

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();
        GamePlayer gamePlayer = GamePlayer.getPlayer(damaged.getUniqueId());

        if (gamePlayer != null) {
            // Mettre à jour le temps de dernier combat
            gamePlayer.setLastFight(System.currentTimeMillis());
        }
    }

    // Méthode pour nettoyer les combat logs lors de la fin de partie
    public void cleanup() {
        for (GamePlayer gamePlayer : GamePlayer.getGamePlayers()) {
            CombatLogEntity combatLog = gamePlayer.getCombatLogEntity();
            if (combatLog != null) {
                combatLog.remove();
                gamePlayer.setCombatLogEntity(null);
            }
        }
    }
}