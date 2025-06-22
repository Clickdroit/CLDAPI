package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FinalHeal extends ScenarioManager {
    public void init() {
        for (UUID uuid : API.getAPI().getGameManager().getInGamePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null)
                continue;
            player.setHealth(player.getMaxHealth());
            Title.sendActionBar(player, "activ");
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5.0F, 1.0F);
        }
    }

    public void configure() {
        this.scenario = Scenario.FINALHEAL;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onStart() {}
}