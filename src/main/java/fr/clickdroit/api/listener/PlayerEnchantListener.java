package fr.clickdroit.api.listener;

import fr.clickdroit.api.config.common.EnchantMaxGUI;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayerEnchantListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EnchantItemEvent(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if (player == null || event.getItem() == null)
            return;
        if (containsBlockedEnchant(event.getEnchantsToAdd())) {
            event.setCancelled(true);
            player.sendMessage("§cCet enchantement est désactivé.");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAnvil(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player)event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        if (!(inv instanceof AnvilInventory))
            return;
        AnvilInventory anvil = (AnvilInventory)inv;
        InventoryView view = event.getView();
        int rawSlot = event.getRawSlot();
        if (rawSlot != view.convertSlot(rawSlot) || rawSlot != 2)
            return;
        ItemStack result = anvil.getItem(2);
        if (result == null || result.getEnchantments() == null)
            return;
        if (containsBlockedEnchant(result.getEnchantments())) {
            getBlockedEnchant(result.getEnchantments()).keySet().forEach(enchant -> result.removeEnchantment(enchant));
            player.sendMessage("§cCet enchantement est désactivé.");
        }
    }

    private boolean containsBlockedEnchant(Map<Enchantment, Integer> enchantments) {
        return enchantments.entrySet().stream().anyMatch(x -> isBlockedEnchant((Enchantment)x.getKey(), ((Integer)x.getValue()).intValue()));
    }

    private Map<Enchantment, Integer> getBlockedEnchant(Map<Enchantment, Integer> enchantments) {
        return (Map<Enchantment, Integer>)enchantments.entrySet().stream().filter(x -> isBlockedEnchant((Enchantment)x.getKey(), ((Integer)x.getValue()).intValue())).collect(Collectors.toMap(x -> (Enchantment)x.getKey(), x -> (Integer)x.getValue()));
    }

    private boolean isBlockedEnchant(Enchantment enchant, int value) {
        EnchantMaxGUI.Enchants ench = EnchantMaxGUI.Enchants.getEnchant(enchant);
        return (ench.getConfigValue() < value);
    }
}
