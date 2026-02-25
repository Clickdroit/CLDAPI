package fr.clickdroit.api.event.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Event fired specifically when a player is killed in the UHC game.
 * Allows external plugins (like Discord webhooks or stats DBs) to record the
 * kill easily.
 */
public class UHCPlayerKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final Player victim;
    private final List<ItemStack> drops;

    public UHCPlayerKillEvent(Player killer, Player victim, List<ItemStack> drops) {
        this.killer = killer;
        this.victim = victim;
        this.drops = drops;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
        return victim;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
