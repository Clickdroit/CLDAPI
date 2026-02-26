package fr.clickdroit.api.game.task;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioValueType;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.module.Modules;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.ApolloManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalTask extends BukkitRunnable {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    private final Modules modules;

    private int episodeTime;

    private int globalTime;

    public GlobalTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
        this.modules = gameManager.getApi().getModules();
    }

    public void run() {
        if (this.episodeTime >= (this.gameConfig.isGameDev() ? 60 : this.gameConfig.getEpisodeTime())) {
            this.episodeTime = 0;
            this.gameManager.getEpisodeManager().switchEpisode();
        }
        if (Rules.noDamage.isActive())
            if (this.globalTime >= 30) {
                Rules.noDamage.setActive(false);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendActionBar(players, "§8• §fL'invincibilité est désormais §cdésactivé§f §8•");
                    players.playSound(players.getLocation(), Sound.VILLAGER_HIT, 3.0F, 1.0F);
                });
            } else {
                int timeLeft = 30 - this.globalTime;
                Bukkit.getOnlinePlayers()
                        .forEach(players -> Title.sendActionBar(players, "§8 §fFin de l'§cinvincibilité dans §c"
                                + timeLeft + "§fseconde" + ((timeLeft > 1) ? "§fs" : "§f") + "§8•"));
            }
        // Auto-Heal feature: heal all players 5 seconds before PvP starts
        if (!Rules.pvp.isActive()) {
            int pvpTime = this.gameConfig.getPvpTime();
            int timeUntilPvp = pvpTime - getGlobalTime();

            // Heal all players 5 seconds before PvP
            if (timeUntilPvp == 5) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (this.gameManager.getInGamePlayers().contains(player.getUniqueId())) {
                        player.setHealth(player.getMaxHealth());
                        player.setFoodLevel(20);
                        player.setSaturation(20.0F);
                        Title.sendActionBar(player, "§8• §a§l❤ AUTO-HEAL ❤ §fVous avez été soigné avant le PvP! §8•");
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.5F);
                    }
                });
                Bukkit.broadcastMessage(
                        "§8§l[§a§lAUTO-HEAL§8§l] §fTous les joueurs ont été §asoignés§f 5 secondes avant le PvP!");
            }

            // Activate PvP when time is reached
            if (getGlobalTime() >= pvpTime) {
                Rules.pvp.setActive(true);
                ApolloManager.sendPvPTitle();
            }
        }
        for (GamePlayer gamePlayers : GamePlayer.getGamePlayers()) {
            Player players = gamePlayers.getPlayer();
            if (players == null)
                continue;
            if (gamePlayers.getInvincibilityCount() > 0)
                gamePlayers.removeInvincibilityCount();
            if (gamePlayers.getInvincibilityNoFallCount() > 0)
                gamePlayers.removeInvincibilityNoFallCount();
        }
        for (Scenario scenario : this.gameManager.getEnabledScenarios()) {
            if (scenario.isEnabled() && (scenario
                    .getScenarioValueType() == ScenarioValueType.TIME
                    || scenario.getScenarioValueType() == ScenarioValueType.TIMEMIN)
                    && ((scenario.getScenarioValueType() == ScenarioValueType.TIME) ? scenario.getValue()
                            : (scenario.getValue() * 60)) == this.globalTime)
                scenario.getScenarioManager().init();
        }
        if (!this.gameManager.getBorder().isStart() &&
                getGlobalTime() >= this.gameConfig.getBorderTime()) {
            this.gameManager.getBorder().startReduce((this.gameConfig.getBorderEndSize() * 2),
                    this.gameConfig.getBorderBlocksPerSecond());
            ApolloManager.sendBorderNotification();
        }
        int sizeBorder = (int) (this.gameManager.getBorder().getWorldBorder().getSize() / 2.0D);
        Bukkit.getOnlinePlayers().forEach(players -> {
            if (!this.gameManager.getInGamePlayers().contains(players.getUniqueId())) {
                if (players.getLocation().getY() < 0.0D)
                    players.teleport(this.gameManager.getApi().getLobbyPopulator().getCenter());
                if (!isInBorder(players, sizeBorder) && players.getLocation().getWorld().getName().equals("world"))
                    players.teleport(this.gameManager.getApi().getLobbyPopulator().getCenter());
            }
        });
        this.modules.onClockUpdate(this.globalTime);
        this.episodeTime++;
        this.globalTime++;
    }

    private boolean isInBorder(Player player, int borderSize) {
        int borderSizeLess = borderSize - borderSize - borderSize;
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        if (x > borderSize)
            return false;
        if (z > borderSize)
            return false;
        if (x < borderSizeLess)
            return false;
        if (z < borderSizeLess)
            return false;
        return true;
    }

    public int getEpisodeTime() {
        return this.episodeTime;
    }

    public void setEpisodeTime(int episodeTime) {
        this.episodeTime = episodeTime;
    }

    public int getGlobalTime() {
        return this.globalTime;
    }

    public void setGlobalTime(int globalTime) {
        this.globalTime = globalTime;
    }
}
