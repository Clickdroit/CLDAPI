package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class KillSwitch extends ScenarioManager implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    private void PlayerDeathEvent(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (victim.getKiller() != null) {
            Player attacker = event.getEntity();
            PlayerInventory victimInventory = victim.getInventory(), attackerInventory = attacker.getInventory();
            ItemStack helmetVictim = victimInventory.getHelmet();
            ItemStack chestplateVictim = victimInventory.getChestplate();
            ItemStack leggingsVictim = victimInventory.getLeggings();
            ItemStack bootsVictim = victimInventory.getBoots();
            ItemStack helmetAttacker = attackerInventory.getHelmet();
            ItemStack chestplateAttacker = attackerInventory.getChestplate();
            ItemStack leggingsAttacker = attackerInventory.getLeggings();
            ItemStack bootsAttacker = attackerInventory.getBoots();
            ItemStack[] itemsVictim = new ItemStack[0];
            ItemStack[] itemsAttacker = new ItemStack[0];
            int i;
            for (i = 0; i < 36; i++) {
                if (victim.getInventory().getItem(i) != null)
                    itemsVictim[i] = victim.getInventory().getItem(i);
                if (attacker.getInventory().getItem(i) != null)
                    itemsAttacker[i] = attacker.getInventory().getItem(i);
            }
            attackerInventory.setHelmet(helmetVictim);
            attackerInventory.setChestplate(chestplateVictim);
            attackerInventory.setLeggings(leggingsVictim);
            attackerInventory.setBoots(bootsVictim);
            attacker.getInventory().clear();
            for (i = 0; i < 36; i++) {
                if (itemsVictim[i] != null && itemsVictim[i].getType() != Material.AIR)
                    attackerInventory.setItem(i, itemsVictim[i]);
            }
            event.getDrops().clear();
            event.getDrops().add(helmetAttacker);
            event.getDrops().add(chestplateAttacker);
            event.getDrops().add(leggingsAttacker);
            event.getDrops().add(bootsAttacker);
            for (i = 0; i < 36; i++) {
                if (itemsAttacker[i] != null && itemsAttacker[i].getType() != Material.AIR)
                    event.getDrops().add(itemsAttacker[i]);
            }
        }
    }

    public void configure() {
        this.scenario = Scenario.KILLSWITCH;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}

