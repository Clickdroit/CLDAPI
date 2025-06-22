package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MasterLevel extends ScenarioManager {
    public void configure() {
        this.scenario = Scenario.MASTERLEVEL;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void init() {
        for (Player players : Bukkit.getOnlinePlayers())
            players.setTotalExperience(this.scenario.getValue());
    }

    public void onStart() {
        for (Player players : Bukkit.getOnlinePlayers())
            players.setLevel(Scenario.MASTERLEVEL.getValue());
    }
}
