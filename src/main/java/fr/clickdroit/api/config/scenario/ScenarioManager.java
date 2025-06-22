package fr.clickdroit.api.config.scenario;

public abstract class ScenarioManager {
    protected Scenario scenario;

    public void activeScenario() {
        configure();
        this.scenario.toggleEnabled();
        if (this.scenario.isEnabled()) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public abstract void configure();

    public abstract void onStart();

    public abstract void onEnable();

    public abstract void onDisable();

    public void init() {}
}
