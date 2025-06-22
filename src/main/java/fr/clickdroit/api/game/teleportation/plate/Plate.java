package fr.clickdroit.api.game.teleportation.plate;

import org.bukkit.Location;

public interface Plate {
    void build();

    void destroy();

    Location getTeleportLocation();
}
