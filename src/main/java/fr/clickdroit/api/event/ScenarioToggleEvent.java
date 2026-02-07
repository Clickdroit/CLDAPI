package fr.clickdroit.api.event;

import fr.clickdroit.api.config.scenario.Scenario;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Événement déclenché lors de l'activation/désactivation d'un scénario.
 */
public class ScenarioToggleEvent extends UHCEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final Scenario scenario;
    private final boolean enabling;

    public ScenarioToggleEvent(Scenario scenario, boolean enabling) {
        this.scenario = scenario;
        this.enabling = enabling;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public boolean isEnabling() {
        return enabling;
    }

    public boolean isDisabling() {
        return !enabling;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

