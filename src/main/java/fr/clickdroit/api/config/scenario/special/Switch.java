package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.utils.Title;

import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Switch extends ScenarioManager {
    private BukkitRunnable switchTask;
    private int minutesCounter = 0;

    @Override
    public void configure() {
        this.scenario = Scenario.SWITCH;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        if (switchTask != null) {
            switchTask.cancel();
            switchTask = null;
        }
    }

    @Override
    public void onStart() {
        if (!Scenario.SWITCH.isEnabled())
            return;

        switchTask = new BukkitRunnable() {
            @Override
            public void run() {
                GameManager manager = API.getAPI().getGameManager();
                if (manager.getGameState() != GameState.PLAYING) {
                    this.cancel();
                    return;
                }

                minutesCounter++;

                if (minutesCounter == 9) {
                    manager.broadcastWithPrefix(
                            "§cUn §lSwitch§c aléatoire des positions aura lieu dans §l1 minute§c !");
                }

                if (minutesCounter == 10) {
                    minutesCounter = 0;
                    executeSwitch(manager);
                }
            }
        };
        switchTask.runTaskTimer((Plugin) API.getAPI(), 1200L, 1200L); // Execute every minute
    }

    private void executeSwitch(GameManager manager) {
        List<Player> targetPlayers = manager.getInGamePlayers().stream()
                .map(Bukkit::getPlayer)
                .filter(p -> p != null && p.isOnline())
                .collect(Collectors.toList());

        if (targetPlayers.size() <= 1)
            return;

        Collections.shuffle(targetPlayers);

        Player firstPlayer = targetPlayers.get(0);
        Location firstLocation = firstPlayer.getLocation().clone();

        for (int i = 0; i < targetPlayers.size() - 1; i++) {
            Player current = targetPlayers.get(i);
            Player next = targetPlayers.get(i + 1);
            current.teleport(next.getLocation());
            sendSwitchAlert(current);
        }

        Player lastPlayer = targetPlayers.get(targetPlayers.size() - 1);
        lastPlayer.teleport(firstLocation);
        sendSwitchAlert(lastPlayer);
    }

    private void sendSwitchAlert(Player player) {
        Title.sendTitle(player, 10, 40, 10, "§c§lSWITCH !", "§fVous avez été téléporté.");
        player.playSound(player.getLocation(), Sound.ENDERMAN_HIT, 1.0f, 1.0f);
    }
}
