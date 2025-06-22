package fr.clickdroit.api.game.teleportation.player;

import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.teleportation.plate.Plate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SoloPlayerPlate implements PlayerPlate {
    private UUID uuid;

    private String name;

    private Plate plate;

    public SoloPlayerPlate(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public void assignPlate(Plate plate) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            this.plate = plate;
            plate.build();
            plate.getTeleportLocation().getChunk().load();
            player.teleport(plate.getTeleportLocation());
            player.getLocation().getChunk().load();
        }
    }

    public boolean removePlate() {
        if (this.plate != null) {
            this.plate.destroy();
            onPlateRemovePlayer();
            return true;
        }
        return false;
    }

    public Player onPlateRemovePlayer() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player != null)
            GameUtils.startPlayer(player, GameMode.SURVIVAL);
        return player;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}

