package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;

public class DiamondLess extends ScenarioManager {
    public void configure() {
        this.scenario = Scenario.DIAMOND_LESS;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onStart() {}
}

