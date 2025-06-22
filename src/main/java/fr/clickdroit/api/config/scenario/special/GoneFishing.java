package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GoneFishing extends ScenarioManager {
    public void configure() {
        this.scenario = Scenario.GONEFISHING;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onStart() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.FISHING_ROD)).setUnbreakable(Boolean.valueOf(true)).addEnchantment(Enchantment.LUCK, Integer.valueOf(250)).addEnchantment(Enchantment.LURE, Integer.valueOf(7)).getItem() });
            players.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ANVIL)).setAmount(Integer.valueOf(64)).getItem() });
        }
    }
}
