package fr.clickdroit.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class HologramCreate {
    public void create() {
        Bukkit.getWorld("Lobby").getEntitiesByClass(ArmorStand.class).forEach(Entity::remove);
        List<String> hologram = new ArrayList<>();
        hologram.add("§f(§a!§f) §fBien le bonjour §f(§a!§f) ");
        hologram.add("");
        hologram.add(" §8> §8Voici certaines §crègles§f à respecter.");
        hologram.add(" §8| §fLe respect des §cgroupes§f.");
        hologram.add(" §8| §fNe pas §cSoundBoard.§f ");
        hologram.add(" §8| §fNe pas §ctuer§f sans raison.");
        hologram.add(" §8| §fNe pas §cdévoiler§f son rôle");
        hologram.add("§4");
        hologram.add(" §8 > §8Voici les §ccommandes§f à connaitre.");
        hologram.add(" §8| §f/§cdoc §8• §fPermet de voir le §cdocument explicatif§f du mode.");
        hologram.add(" §8| §f/mumble §8• §fPermet de voir le mumble de la §c§1game ");
        hologram.add(" §8| §f/§crules §8• §fPermet de voir les §crègles§f.");
        hologram.add(" §8| §f/helpop §8• §fPermet de §cdemander§f de l'aide.");
        hologram.add("§4");
        hologram.add(" §8 > §f Bonne §achance§f à tous !");
        hologram.add("§4");
        new Hologram(new Location(Bukkit.getWorld("Lobby"), -65.9D, 103.6D, 15.2D), hologram, 800);
    }
}
