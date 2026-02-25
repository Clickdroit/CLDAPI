package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class GoldenHead extends ScenarioManager implements Listener {
    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (this.scenario.isEnabled()) {
            Player player = event.getEntity();
            event.getDrops().add((new ItemCreator(Material.SKULL_ITEM)).setDurability(Integer.valueOf(3)).setName("§eTête de §6" + player.getName()).setOwner(player.getName()).getItem());
        }
    }

    public void configure() {
        this.scenario = Scenario.GOLDENHEAD;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {
        ItemStack itemStack = (new ItemCreator(Material.GOLDEN_APPLE)).setDurability(Integer.valueOf(0)).setName("Head").getItem();
        ShapedRecipe goldenHeadRecipe = new ShapedRecipe(itemStack);
        goldenHeadRecipe.shape(new String[] { "@@@", "@#@", "@@@" });
        goldenHeadRecipe.setIngredient('@', Material.GOLD_INGOT);
        goldenHeadRecipe.setIngredient('#', Material.SKULL_ITEM, 3);
        Bukkit.getServer().addRecipe((Recipe)goldenHeadRecipe);
    }
}

