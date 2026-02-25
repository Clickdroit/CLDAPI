package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;

public class SplitSpawn extends ScenarioManager {

    @Override
    public void configure() {
        this.scenario = Scenario.SPLIT_SPAWN;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onStart() {
        // Handled naturally via TeleportationManager.java logic injection.
    }
}

