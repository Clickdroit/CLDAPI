package fr.clickdroit.api.game.teleportation.player;

import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.teleportation.plate.Plate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TeamPlayerPlate implements PlayerPlate {
    private List<UUID> uuids;

    private Plate plate;

    private String teamName;

    public TeamPlayerPlate(List<UUID> uuids, String teamName) {
        this.uuids = uuids;
        this.teamName = teamName;
    }

    public void assignPlate(Plate plate) {
        for (UUID uuid : this.uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                this.plate = plate;
                plate.build();
                player.teleport(plate.getTeleportLocation());
                GameUtils.startPlayer(player, GameMode.ADVENTURE);
            }
        }
    }

    public boolean removePlate() {
        if (this.plate != null) {
            this.plate.destroy();
            for (UUID uuid : this.uuids) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null)
                    GameUtils.startPlayer(player, GameMode.SURVIVAL);
            }
            return true;
        }
        return false;
    }

    public List<UUID> getUuids() {
        return this.uuids;
    }

    public String getName() {
        return this.teamName;
    }
}
