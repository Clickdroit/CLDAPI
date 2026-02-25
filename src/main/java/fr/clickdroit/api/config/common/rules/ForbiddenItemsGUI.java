package fr.clickdroit.api.config.common.rules;

import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ForbiddenItemsGUI implements CustomInventory {
    private final GameManager gameManager;

    // URLs des textures pour les têtes personnalisées
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";

    public ForbiddenItemsGUI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "§c Objets interdits";
    }

    @Override
    public int getSlots() {
        return 27; // 3 rangées
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[27];

        // Remplir avec du verre rouge pour les bordures
        fillWithGlass(slots);

        // Titre
        slots[4] = createTitleItem();

        // Items utilisables
        UseItems[] items = UseItems.values();
        int[] itemSlots = {10, 11, 12, 13, 14, 15}; // Ligne du milieu

        for (int i = 0; i < items.length && i < itemSlots.length; i++) {
            slots[itemSlots[i]] = createEnhancedUseItem(items[i]);
        }

        // Retour
        slots[22] = createBackArrowHead("§fRevenir en arrière", "§7Retourner à la gestion des règles");

        return () -> slots;
    }

    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 14) // Rouge
                .setName("§0")
                .getItem();

        // Remplir tous les slots d'abord
        for (int i = 0; i < slots.length; i++) {
            slots[i] = glassPane;
        }
    }

    private ItemStack createTitleItem() {
        return new ItemCreator(Material.ANVIL)
                .setName("§8| §cConfiguration des objets")
                .addLore("")
                .addLore("  §8| §fDéfinissez quels objets sont")
                .addLore("  §8| §fautorisés ou interdits dans")
                .addLore("  §8| §fla partie.")
                .addLore("")
                .addLore("  §7§oCliquez sur les objets ci-dessous")
                .addLore("  §7§opour les autoriser/interdire.")
                .addLore("")
                .getItem();
    }

    private ItemStack createEnhancedUseItem(UseItems useItem) {
        String statusColor = useItem.isEnabled() ? "§a" : "§c";
        String status = useItem.isEnabled() ? "Autorisé" : "Interdit";

        return new ItemCreator(useItem.getMaterial())
                .setDurability(useItem.getData())
                .setName("§8| §f" + useItem.getName())
                .addLore("")
                .addLore("  §8| §fStatut: " + statusColor + status)
                .addLore("")
                .addLore("§e▶ §fCliquez pour " + (useItem.isEnabled() ? "§cinterdire" : "§aautoriser"))
                .addLore("")
                .getItem();
    }

    private ItemStack createBackArrowHead(String name, String lore) {
        return new ItemCreator(Material.ARROW) // Version 1.8 - utilise une flèche normale
                .setName(name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour retourner")
                .getItem();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Retour
        if (clickedItem.getType() == Material.ARROW && slot == 22) {
            this.gameManager.getApi().openInventory(player, GameRulesManagerGUI.class);
            return;
        }

        // Gestion des items utilisables
        for (UseItems useItem : UseItems.values()) {
            if (useItem.getMaterial() == clickedItem.getType() &&
                    clickedItem.hasItemMeta() &&
                    clickedItem.getItemMeta().hasDisplayName() &&
                    clickedItem.getItemMeta().getDisplayName().contains(useItem.getName())) {

                useItem.toggleEnabled();
                player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.5F);

                this.gameManager.getApi().openInventory(player, getClass());
                break;
            }
        }
    }

    @Override
    public int getRows() {
        return 3;
    }
}
