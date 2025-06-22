package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CatEyes extends ScenarioManager {
    public void configure() {
        this.scenario = Scenario.CAT_EYES;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onStart() {
        for (Player players : Bukkit.getOnlinePlayers())
            players.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2147483647, 0, true, false));
    }
}
