package fr.clickdroit.api.config.scenario;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import fr.clickdroit.api.API;

public abstract class ScenarioManager implements Listener {
    protected Scenario scenario;

    public void activeScenario() {
        configure();
        this.scenario.toggleEnabled();
        if (this.scenario.isEnabled()) {
            registerListeners();
            onEnable();
        } else {
            unregisterListeners();
            onDisable();
        }
    }

    protected void registerListeners() {
        Bukkit.getPluginManager().registerEvents(this, API.getAPI());
    }

    protected void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }

    public abstract void configure();

    public abstract void onStart();

    public abstract void onEnable();

    public abstract void onDisable();

    public void init() {
    }
}
