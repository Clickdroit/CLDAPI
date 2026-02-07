package fr.clickdroit.api.event;

import fr.clickdroit.api.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

/**
 * Événement déclenché lorsqu'un joueur est éliminé de la partie.
 * Peut être annulé pour empêcher l'élimination.
 */
public class PlayerEliminatedEvent extends UHCEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final GamePlayer gamePlayer;
    private final Player killer;
    private final EliminationCause cause;

    public PlayerEliminatedEvent(GamePlayer gamePlayer, @Nullable Player killer, EliminationCause cause) {
        this.gamePlayer = gamePlayer;
        this.killer = killer;
        this.cause = cause;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    @Nullable
    public Player getKiller() {
        return killer;
    }

    public EliminationCause getCause() {
        return cause;
    }

    public boolean hasKiller() {
        return killer != null;
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

    public enum EliminationCause {
        KILLED_BY_PLAYER,
        KILLED_BY_MOB,
        ENVIRONMENT,
        DISCONNECT_TIMEOUT,
        HOST_ELIMINATED,
        OTHER
    }
}

