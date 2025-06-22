package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.config.scenario.special.runnable.MasterAppleRunnable;
import org.bukkit.plugin.Plugin;

public class MasterApple extends ScenarioManager {
    public void configure() {
        this.scenario = Scenario.MASTERAPPLE;
    }

    public void onStart() {
        MasterAppleRunnable masterAppleRunnable = new MasterAppleRunnable();
        masterAppleRunnable.runTaskTimer((Plugin) API.getAPI(), (this.scenario.getValue() * 1200), (this.scenario.getValue() * 1200));
    }

    public void onEnable() {}

    public void onDisable() {}
}
