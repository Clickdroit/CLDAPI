package fr.clickdroit.api.config.borderValue;

import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class BorderEndSizeGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    // URLs des textures pour les têtes personnalisées
    private static final String RED_MINUS_URL = "http://textures.minecraft.net/texture/935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba";
    private static final String GREEN_PLUS_URL = "http://textures.minecraft.net/texture/9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777";
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";

    public BorderEndSizeGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @Override
    public String getName() {
        return "Configuration - Bordure finale";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[27];
        int value = this.gameConfig.getBorderEndSize();

        // Remplir d'abord avec du verre rouge pour la bordure
        fillWithGlass(slots);

        // Ligne du haut - Têtes pour diminuer (-100, -50, -10)
        slots[10] = createRedMinusHead("-100", "§7Diminuer de 100 blocs");
        slots[11] = createRedMinusHead("-50", "§7Diminuer de 50 blocs");
        slots[12] = createRedMinusHead("-10", "§7Diminuer de 10 blocs");

        // Item central - VERRE COLORÉ (au lieu d'une tête)
        slots[13] = new ItemCreator(Material.STAINED_GLASS)
                .setDurability((short) 14) // Rouge
                .setName("§8| §fBordure finale")
                .addLore("")
                .addLore("  §8| §fVous permet de §cmodifier")
                .addLore("  §8| §fla taille de la §cbordure finale§f")
                .addLore("  §8| §fde la partie.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c" + value + "x" + value + " blocs")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        // Ligne du haut - Têtes pour augmenter (+10, +50, +100)
        slots[14] = createGreenPlusHead("+10", "§7Augmenter de 10 blocs");
        slots[15] = createGreenPlusHead("+50", "§7Augmenter de 50 blocs");
        slots[16] = createGreenPlusHead("+100", "§7Augmenter de 100 blocs");

        // Flèche de retour avec une tête personnalisée (ligne du bas, centre)
        slots[22] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu des bordures");

        return () -> slots;
    }

    /**
     * Remplit les slots vides avec du verre coloré pour la bordure
     */
    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 14) // Rouge
                .setName("§0") // Nom invisible
                .getItem();

        // Remplir tous les slots d'abord
        for (int i = 0; i < slots.length; i++) {
            slots[i] = glassPane;
        }
    }

    /**
     * Crée une tête rouge avec un moins pour diminuer
     */
    private ItemStack createRedMinusHead(String name, String lore) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§c§l" + name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour diminuer")
                .setSkullURL(RED_MINUS_URL)
                .getItem();
    }

    /**
     * Crée une tête verte avec un plus pour augmenter
     */
    private ItemStack createGreenPlusHead(String name, String lore) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§a§l" + name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour augmenter")
                .setSkullURL(GREEN_PLUS_URL)
                .getItem();
    }

    /**
     * Crée une tête flèche pour le retour
     */
    private ItemStack createBackArrowHead(String name, String lore) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName(name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour retourner")
                .setSkullURL(LEFT_ARROW_URL)
                .getItem();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Gestion des têtes personnalisées
        if (clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Gestion des têtes de diminution (rouges)
            if (itemName.contains("§c§l-100")) {
                this.gameConfig.setBorderEndSize(this.gameConfig.getBorderEndSize() - 100);
            } else if (itemName.contains("§c§l-50")) {
                this.gameConfig.setBorderEndSize(this.gameConfig.getBorderEndSize() - 50);
            } else if (itemName.contains("§c§l-10")) {
                this.gameConfig.setBorderEndSize(this.gameConfig.getBorderEndSize() - 10);
            }
            // Gestion des têtes d'augmentation (vertes)
            else if (itemName.contains("§a§l+100")) {
                this.gameConfig.setBorderEndSize(this.gameConfig.getBorderEndSize() + 100);
            } else if (itemName.contains("§a§l+50")) {
                this.gameConfig.setBorderEndSize(this.gameConfig.getBorderEndSize() + 50);
            } else if (itemName.contains("§a§l+10")) {
                this.gameConfig.setBorderEndSize(this.gameConfig.getBorderEndSize() + 10);
            }
            // Gestion du retour
            else if (itemName.contains("§fRevenir en arrière")) {
                this.gameManager.getApi().openInventory(player, BorderManagerGUI.class);
                return;
            }

            // Vérifier les limites
            if (this.gameConfig.getBorderEndSize() > 500) {
                this.gameConfig.setBorderEndSize(500);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLa taille maximum de bordure finale est de §c500§f!");
            }
            if (this.gameConfig.getBorderEndSize() < 10) {
                this.gameConfig.setBorderEndSize(10);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLa taille minimum de bordure finale est de §c10§f!");
            }

            // Jouer un son de confirmation
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);

            // Actualiser l'inventaire
            this.gameManager.getApi().openInventory(player, getClass());
        }
    }

    @Override
    public int getRows() {
        return 3; // 27 slots (3 rangées de 9)
    }
}