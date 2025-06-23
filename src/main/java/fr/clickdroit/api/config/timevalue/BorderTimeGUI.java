package fr.clickdroit.api.config.timevalue;

import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BorderTimeGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    // URLs des textures pour les têtes personnalisées
    private static final String RED_MINUS_URL = "http://textures.minecraft.net/texture/935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba";
    private static final String GREEN_PLUS_URL = "http://textures.minecraft.net/texture/9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777";
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";

    public BorderTimeGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @Override
    public String getName() {
        return "Bordure - Temps d'activation";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[27];

        // Remplir d'abord avec du verre gris pour la bordure
        fillWithGlass(slots);

        // Ligne du haut - Têtes pour diminuer (-2m, -1m, -10s)
        slots[10] = createRedMinusHead("-2m", "§7Diminuer de 2 minutes");
        slots[11] = createRedMinusHead("-1m", "§7Diminuer de 1 minute");
        slots[12] = createRedMinusHead("-10s", "§7Diminuer de 10 secondes");

        // Item central - VERRE COLORÉ (Bordure)
        slots[13] = new ItemCreator(Material.STAINED_GLASS)
                .setDurability((short) 9) // Cyan pour représenter la bordure
                .setName("§8| §fTemps d'activation de la §bbordure")
                .addLore("")
                .addLore("  §8| §fVous permet de §cmodifier")
                .addLore("  §8| §fle temps avant que la §bbordure")
                .addLore("  §8| §fcommence à se §crétrécir§f.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c" + formatTime(this.gameConfig.getBorderTime()))
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        // Ligne du haut - Têtes pour augmenter (+10s, +1m, +2m)
        slots[14] = createGreenPlusHead("+10s", "§7Augmenter de 10 secondes");
        slots[15] = createGreenPlusHead("+1m", "§7Augmenter de 1 minute");
        slots[16] = createGreenPlusHead("+2m", "§7Augmenter de 2 minutes");

        // Flèche de retour avec une tête personnalisée (ligne du bas, centre)
        slots[22] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu des options");

        return () -> slots;
    }

    /**
     * Remplit les slots vides avec du verre coloré pour la bordure
     */
    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 7) // Gris
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

    /**
     * Formate le temps en secondes vers un format lisible (ex: "30m" ou "1h 15m")
     */
    private String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            if (seconds % 60 == 0) {
                return (seconds / 60) + "m";
            } else {
                return (seconds / 60) + "m " + (seconds % 60) + "s";
            }
        } else {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            if (minutes == 0) {
                return hours + "h";
            } else {
                return hours + "h " + minutes + "m";
            }
        }
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Gestion des têtes personnalisées
        if (clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Gestion des têtes de diminution (rouges)
            if (itemName.contains("§c§l-10s")) {
                this.gameConfig.setBorderTime(this.gameConfig.getBorderTime() - 10);
            } else if (itemName.contains("§c§l-1m")) {
                this.gameConfig.setBorderTime(this.gameConfig.getBorderTime() - 60);
            } else if (itemName.contains("§c§l-2m")) {
                this.gameConfig.setBorderTime(this.gameConfig.getBorderTime() - 120);
            }
            // Gestion des têtes d'augmentation (vertes)
            else if (itemName.contains("§a§l+10s")) {
                this.gameConfig.setBorderTime(this.gameConfig.getBorderTime() + 10);
            } else if (itemName.contains("§a§l+1m")) {
                this.gameConfig.setBorderTime(this.gameConfig.getBorderTime() + 60);
            } else if (itemName.contains("§a§l+2m")) {
                this.gameConfig.setBorderTime(this.gameConfig.getBorderTime() + 120);
            }
            // Gestion du retour
            else if (itemName.contains("§fRevenir en arrière")) {
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                return;
            }

            // Vérifier les limites
            if (this.gameConfig.getBorderTime() > 7200) {
                this.gameConfig.setBorderTime(7200);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLe temps maximum d'activation de la bordure est de §b2h§f!");
            }
            if (this.gameConfig.getBorderTime() < 120) {
                this.gameConfig.setBorderTime(120);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLe temps minimum d'activation de la bordure est de §b2m§f!");
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