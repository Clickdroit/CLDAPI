package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;

public class RandomTeam extends ScenarioManager {
    public void configure() {
        this.scenario = Scenario.RANDOMTEAM;
    }

    public void onEnable() {
        API.getAPI().getGameManager().getTeamManager().resetTeams();
    }

    public void onDisable() {}

    public void onStart() {}
}

