package fr.clickdroit.api.game.combatlog;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.CombatUtilsAPI;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogManager implements Listener {
    private final GameManager gameManager;

    public CombatLogManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        onLogout(event.getPlayer());
    }

    public void onLogout(Player player) {
        if (!GameUtils.isGameStarted()) {
            GamePlayer.removePlayer(player.getUniqueId());
            return;
        }

        if (this.gameManager.getInGamePlayers().contains(player.getUniqueId())) {
            GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
            if (gamePlayer == null)
                return;

            long lastFight = CombatUtilsAPI.getTimeBeforeLastFight(gamePlayer);
            if (lastFight <= UHCConstants.COMBAT_TIME_SECONDS) {
                CombatLogEntity combatLog = new CombatLogEntity(player);
                gamePlayer.setCombatLogEntity(combatLog);

                this.gameManager.broadcastWithPrefix(
                        UHCConstants.COLOR_ERROR + gamePlayer.getName() + " s'est déconnecté en combat !");
            }
        }
    }

    /**
     * Gère la reconnexion d'un joueur : récupère la vie du villageois et le despawn
     */
    public void onLogin(Player player) {
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
        if (gamePlayer == null)
            return;

        CombatLogEntity combatLog = gamePlayer.getCombatLogEntity();
        if (combatLog == null)
            return;

        // Récupérer la vie du villageois et l'appliquer au joueur
        double villagerHealth = combatLog.getCurrentHealth();
        if (villagerHealth > 0) {
            player.setHealth(Math.min(villagerHealth, player.getMaxHealth()));
        }

        // Supprimer le villageois
        combatLog.remove();
        gamePlayer.setCombatLogEntity(null);

        this.gameManager.broadcastWithPrefix(
                UHCConstants.COLOR_SUCCESS + gamePlayer.getName() + " s'est reconnecté.");
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

        // Drop des items du joueur
        if (gamePlayer.getLastLocation() != null) {
            for (org.bukkit.inventory.ItemStack item : gamePlayer.getPlayerInv()) {
                if (item != null && item.getType() != org.bukkit.Material.AIR)
                    gamePlayer.getLastLocation().getWorld().dropItemNaturally(gamePlayer.getLastLocation(), item);
            }
            for (org.bukkit.inventory.ItemStack item : gamePlayer.getPlayerArmor()) {
                if (item != null && item.getType() != org.bukkit.Material.AIR)
                    gamePlayer.getLastLocation().getWorld().dropItemNaturally(gamePlayer.getLastLocation(), item);
            }
        }

        // Élimination de l'équipe si dernier membre
        if (!GameUtils.isSoloMode() && this.gameManager.getTeamManager().getPlayerTeam().containsKey(gamePlayer.getUuid())) {
            fr.clickdroit.api.game.team.Teams team = (fr.clickdroit.api.game.team.Teams) this.gameManager.getTeamManager().getPlayerTeam().get(gamePlayer.getUuid());
            this.gameManager.getTeamManager().killTeam(team);
        }

        this.gameManager.broadcastWithPrefix("§c" + gamePlayer.getName() + " est mort en étant déconnecté !");

        // Vérifier la fin de partie
        fr.clickdroit.api.API api = this.gameManager.getApi();
        if (api.getModules() instanceof fr.clickdroit.api.module.games.UHCModule) {
            ((fr.clickdroit.api.module.games.UHCModule) api.getModules()).getUhcFinisher().tryFinishGame();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            if (event.getEntity() instanceof org.bukkit.entity.Villager) {
                org.bukkit.entity.Villager villager = (org.bukkit.entity.Villager) event.getEntity();
                for (GamePlayer gp : GamePlayer.getGamePlayers()) {
                    CombatLogEntity log = gp.getCombatLogEntity();
                    if (log != null && log.getEntity().equals(villager)) {
                        org.bukkit.Bukkit.getScheduler().runTaskLater(gameManager.getApi(), log::updateName, 1L);
                        break;
                    }
                }
            }
            return;
        }

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
