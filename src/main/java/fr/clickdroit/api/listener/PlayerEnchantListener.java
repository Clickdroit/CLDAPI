package fr.clickdroit.api.listener;

import fr.clickdroit.api.config.common.rules.GameRulesManagerGUI;
import org.bukkit.Material;
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
        if (containsBlockedEnchant(event.getEnchantsToAdd(), event.getItem())) {
            event.setCancelled(true);
            player.sendMessage("§cCet enchantement est désactivé ou dépasse la limite autorisée.");
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
        if (containsBlockedEnchant(result.getEnchantments(), result)) {
            getBlockedEnchant(result.getEnchantments(), result).keySet().forEach(enchant -> result.removeEnchantment(enchant));
            player.sendMessage("§cCet enchantement est désactivé ou dépasse la limite autorisée.");
        }
    }

    private boolean containsBlockedEnchant(Map<Enchantment, Integer> enchantments, ItemStack item) {
        return enchantments.entrySet().stream().anyMatch(x -> isBlockedEnchant(x.getKey(), x.getValue(), item));
    }

    private Map<Enchantment, Integer> getBlockedEnchant(Map<Enchantment, Integer> enchantments, ItemStack item) {
        return enchantments.entrySet().stream()
                .filter(x -> isBlockedEnchant(x.getKey(), x.getValue(), item))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isBlockedEnchant(Enchantment enchant, int level, ItemStack item) {
        // Déterminer le type d'équipement et récupérer la limite correspondante
        int maxAllowed = getMaxAllowedLevel(enchant, item);

        // Si maxAllowed est 0, l'enchantement est complètement désactivé
        // Si le niveau demandé dépasse le maximum autorisé, c'est bloqué
        return maxAllowed == 0 || level > maxAllowed;
    }

    private int getMaxAllowedLevel(Enchantment enchant, ItemStack item) {
        Material material = item.getType();

        // === ARMURES ===
        if (isIronArmor(material)) {
            if (enchant == Enchantment.PROTECTION_ENVIRONMENTAL) {
                return GameRulesManagerGUI.EquipmentEnchants.IRON_PROTECTION.getLevel();
            } else if (enchant == Enchantment.THORNS) {
                return GameRulesManagerGUI.EquipmentEnchants.IRON_THORNS.getLevel();
            }
        } else if (isDiamondArmor(material)) {
            if (enchant == Enchantment.PROTECTION_ENVIRONMENTAL) {
                return GameRulesManagerGUI.EquipmentEnchants.DIAMOND_PROTECTION.getLevel();
            } else if (enchant == Enchantment.THORNS) {
                return GameRulesManagerGUI.EquipmentEnchants.DIAMOND_THORNS.getLevel();
            }
        }

        // === ÉPÉES ===
        else if (material == Material.IRON_SWORD) {
            if (enchant == Enchantment.DAMAGE_ALL) {
                return GameRulesManagerGUI.EquipmentEnchants.IRON_SHARPNESS.getLevel();
            } else if (enchant == Enchantment.FIRE_ASPECT) {
                return GameRulesManagerGUI.EquipmentEnchants.IRON_FIRE_ASPECT.getLevel();
            } else if (enchant == Enchantment.KNOCKBACK) {
                return GameRulesManagerGUI.EquipmentEnchants.IRON_KNOCKBACK.getLevel();
            }
        } else if (material == Material.DIAMOND_SWORD) {
            if (enchant == Enchantment.DAMAGE_ALL) {
                return GameRulesManagerGUI.EquipmentEnchants.DIAMOND_SHARPNESS.getLevel();
            } else if (enchant == Enchantment.FIRE_ASPECT) {
                return GameRulesManagerGUI.EquipmentEnchants.DIAMOND_FIRE_ASPECT.getLevel();
            } else if (enchant == Enchantment.KNOCKBACK) {
                return GameRulesManagerGUI.EquipmentEnchants.DIAMOND_KNOCKBACK.getLevel();
            }
        }

        // === ARC ===
        else if (material == Material.BOW) {
            if (enchant == Enchantment.ARROW_INFINITE) {
                return GameRulesManagerGUI.EquipmentEnchants.BOW_INFINITY.getLevel();
            } else if (enchant == Enchantment.ARROW_FIRE) {
                return GameRulesManagerGUI.EquipmentEnchants.BOW_FLAME.getLevel();
            } else if (enchant == Enchantment.ARROW_DAMAGE) {
                return GameRulesManagerGUI.EquipmentEnchants.BOW_POWER.getLevel();
            }
        }

        // Si l'enchantement n'est pas géré par notre système, autoriser le niveau vanilla max
        return enchant.getMaxLevel();
    }

    /**
     * Vérifie si l'item est une armure en fer
     */
    private boolean isIronArmor(Material material) {
        return material == Material.IRON_HELMET ||
                material == Material.IRON_CHESTPLATE ||
                material == Material.IRON_LEGGINGS ||
                material == Material.IRON_BOOTS;
    }

    /**
     * Vérifie si l'item est une armure en diamant
     */
    private boolean isDiamondArmor(Material material) {
        return material == Material.DIAMOND_HELMET ||
                material == Material.DIAMOND_CHESTPLATE ||
                material == Material.DIAMOND_LEGGINGS ||
                material == Material.DIAMOND_BOOTS;
    }
}
