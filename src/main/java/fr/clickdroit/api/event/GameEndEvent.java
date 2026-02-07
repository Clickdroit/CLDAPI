package fr.clickdroit.api.event;

import fr.clickdroit.api.game.team.Teams;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

/**
 * Événement déclenché lorsque la partie se termine.
 */
public class GameEndEvent extends UHCEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final List<UUID> winners;
    private final Teams winningTeam;
    private final EndReason reason;
    private final long gameDurationSeconds;

    public GameEndEvent(List<UUID> winners, Teams winningTeam, EndReason reason, long gameDurationSeconds) {
        this.winners = winners;
        this.winningTeam = winningTeam;
        this.reason = reason;
        this.gameDurationSeconds = gameDurationSeconds;
    }

    public List<UUID> getWinners() {
        return winners;
    }

    public Teams getWinningTeam() {
        return winningTeam;
    }

    public EndReason getReason() {
        return reason;
    }

    public long getGameDurationSeconds() {
        return gameDurationSeconds;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum EndReason {
        LAST_PLAYER_STANDING,
        LAST_TEAM_STANDING,
        HOST_ENDED,
        TIME_LIMIT,
        OTHER
    }
}

