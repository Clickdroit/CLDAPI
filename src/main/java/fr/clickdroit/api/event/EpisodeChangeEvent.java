package fr.clickdroit.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Événement déclenché lors du changement d'épisode.
 */
public class EpisodeChangeEvent extends UHCEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final int previousEpisode;
    private final int newEpisode;
    private final int episodeDurationSeconds;

    public EpisodeChangeEvent(int previousEpisode, int newEpisode, int episodeDurationSeconds) {
        this.previousEpisode = previousEpisode;
        this.newEpisode = newEpisode;
        this.episodeDurationSeconds = episodeDurationSeconds;
    }

    public int getPreviousEpisode() {
        return previousEpisode;
    }

    public int getNewEpisode() {
        return newEpisode;
    }

    public int getEpisodeDurationSeconds() {
        return episodeDurationSeconds;
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

