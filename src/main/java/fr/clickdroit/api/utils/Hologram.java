package fr.clickdroit.api.utils;

import fr.clickdroit.api.API;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.Plugin;

public class Hologram {
    private final Location location;

    private final List<String> msg;

    private int time;

    public Hologram(Location location, List<String> msg, int time) {
        this.location = location;
        this.msg = msg;
        this.time = time;
        for (String string : msg) {
            build(string);
            this.location.setY(this.location.getY() - 0.23D);
        }
    }

    public Hologram(Location location, List<String> msg) {
        this.location = location;
        this.msg = msg;
        for (String string : msg) {
            build(string);
            this.location.setY(this.location.getY() - 0.23D);
        }
    }

    private void build(String line) {
        ArmorStand armorStand = (ArmorStand)this.location.getWorld().spawn(this.location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(line);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        Objects.requireNonNull(armorStand);
        Bukkit.getScheduler().runTaskLater((Plugin)API.getAPI(), armorStand::remove, 20L * this.time);
    }
}
