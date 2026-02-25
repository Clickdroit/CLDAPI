package fr.clickdroit.api.config.common.rules;

import fr.clickdroit.api.common.rules.items.GeneralRules;
import fr.clickdroit.api.config.borderValue.BorderManagerGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GeneralRulesGUI implements CustomInventory {
    private final GameManager gameManager;
    private final Map<Integer, GeneralRules> rules;

    public GeneralRulesGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.rules = new HashMap<>();
    }

    private void setup() {
        // Règles de jeu (lignes du milieu)
        this.rules.put(19, GeneralRules.STRIPMINING);
        this.rules.put(20, GeneralRules.IPVP);
        this.rules.put(21, GeneralRules.CROSSTEAM);
        this.rules.put(22, GeneralRules.TOWER);
        this.rules.put(23, GeneralRules.DIGDOWN);
        this.rules.put(24, GeneralRules.ROLLERCOASTER);
        this.rules.put(28, GeneralRules.HEALTH);
        this.rules.put(29, GeneralRules.MUMBLE);
        this.rules.put(30, GeneralRules.PRIVATEMSG);
    }

    @Override
    public String getName() {
        return "§6Règles de la partie";
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        // Verre de bordure (jaune/orange pour différencier)
        Integer[] glass = {
                0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53
        };

        for (int glassSlot : glass) {
            slots[glassSlot] = new ItemCreator(Material.STAINED_GLASS_PANE)
                    .setDurability(1) // Orange
                    .setName("§0")
                    .getItem();
        }

        // Titre informatif
        slots[4] = createInfoItem();

        setup();

        // Placer les règles
        for (Map.Entry<Integer, GeneralRules> entry : this.rules.entrySet()) {
            slots[entry.getKey()] = createEnhancedRuleItem(entry.getValue());
        }

        // Retour
        slots[49] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    private ItemStack createInfoItem() {
        return new ItemCreator(Material.PAPER)
                .setName("§8| §6Règles de la partie")
                .addLore("")
                .addLore("  §8| §fConfigurez les règles principales")
                .addLore("  §8| §fde votre partie UHC.")
                .addLore("")
                .addLore("  §7§oÉquipement: Autorisations diamant")
                .addLore("  §7§oComportement: StripMining, iPvP, etc.")
                .addLore("  §7§oCommunication: Mumble, messages privés")
                .addLore("")
                .getItem();
    }

    private ItemStack createEnhancedRuleItem(GeneralRules rule) {
        String statusColor = rule.isEnabled() ? "§a" : "§c";
        String status = rule.isEnabled() ? "Autorisé" : "Interdit";

        return new ItemCreator(rule.getMaterial())
                .setDurability(rule.getData())
                .setName("§8| §f" + rule.getName())
                .addLore("")
                .addLore("  §8| §fStatut: " + statusColor + status)
                .addLore("")
                .addLore("§e▶ §fCliquez pour " + (rule.isEnabled() ? "§cinterdire" : "§aautoriser"))
                .addLore("")
                .getItem();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // CORRECTION : Vérifier d'abord si le slot contient une règle
        if (this.rules.containsKey(slot)) {
            GeneralRules rule = this.rules.get(slot);
            rule.toggleEnabled();

            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.5F);
            player.sendMessage("§7Règle §f" + rule.getName() + " §7" +
                    (rule.isEnabled() ? "§aautorisée" : "§cinterdite") + "§7!");

            // Rafraîchir l'inventaire
            this.gameManager.getApi().openInventory(player, getClass());
            return;
        }

        // Gestion du retour - vérifier le type d'item
        if (clickedItem != null && clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();
            if (itemName.contains("§8| §fRevenir en §carrière")) {
                this.gameManager.getApi().openInventory(player, GameRulesManagerGUI.class);
                return;
            }
        }
    }

    @Override
    public int getRows() {
        return 6;
    }
}
