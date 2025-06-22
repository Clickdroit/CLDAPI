package fr.clickdroit.api.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class HologramCreate {
    public void create() {
        Bukkit.getWorld("Lobby").getEntitiesByClass(ArmorStand.class).forEach(Entity::remove);
        List<String> hologram = new ArrayList<>();
        hologram.add("le bonjour ");
        hologram.add("");
        hologram.add(" certaines respecter.");
        hologram.add("   respect des ");
        hologram.add("   pas ");
        hologram.add("   pas sans raison. ");
        hologram.add("   pas son r");
        hologram.add("");
        hologram.add(" les connaitre.");
        hologram.add("   de voir le explicatifdu mode.");
        hologram.add("   de voir le mumble de ");
        hologram.add("   de voir les ");
        hologram.add("   de de l'aide.");
        hologram.add("");
        hologram.add(" tous !");
        hologram.add("");
        new Hologram(new Location(Bukkit.getWorld("Lobby"), -65.9D, 103.6D, 15.2D), hologram, 800);
    }
}
