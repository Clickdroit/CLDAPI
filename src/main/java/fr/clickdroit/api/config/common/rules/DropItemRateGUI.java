package fr.clickdroit.api.config.common.rules;

import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.config.ConfigOptionsGUI;
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

public class DropItemRateGUI implements CustomInventory {
    private final GameManager gameManager;

    // URLs des textures pour les têtes personnalisées
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";
    private static final String INFO_HEAD_URL = "http://textures.minecraft.net/texture/46ba63344f49dd1c4f5488e926bf3d9e2b29916a6c50d610bb40a5273dc8c82";

    public DropItemRateGUI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "Configuration - Taux de drop";
    }

    @Override
    public int getSlots() {
        return 45; // 5 rangées pour plus d'espace
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[45];

        // Remplir d'abord avec du verre vert pour la bordure
        fillWithGlass(slots);

        // Titre informatif en haut (avec tête personnalisée)
        slots[4] = createInfoHead();

        // Placer les items DropItemRate de manière organisée
        DropItemRate[] dropItems = DropItemRate.values();
        int[] itemSlots = {20, 21, 22, 23, 24}; // Ligne du milieu, centrée

        for (int i = 0; i < dropItems.length && i < itemSlots.length; i++) {
            slots[itemSlots[i]] = createEnhancedDropItem(dropItems[i]);
        }

        // Instructions en bas
        slots[37] = createInstructionHead();

        // Flèche de retour avec une tête personnalisée (ligne du bas, centre)
        slots[40] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu des options");

        return () -> slots;
    }

    /**
     * Remplit les slots vides avec du verre coloré pour la bordure
     */
    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 5) // Vert clair
                .setName("§0") // Nom invisible
                .getItem();

        // Positions spécifiques pour la bordure
        Integer[] glassPositions = {
                0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 38, 39, 41, 42, 43, 44
        };

        // Remplir d'abord tous les slots avec du verre transparent
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new ItemCreator(Material.STAINED_GLASS_PANE)
                    .setDurability((short) 15) // Noir transparent
                    .setName("§0")
                    .getItem();
        }

        // Puis appliquer le verre coloré aux positions de bordure
        for (int pos : glassPositions) {
            slots[pos] = glassPane;
        }
    }

    /**
     * Crée la tête d'information en haut
     */
    private ItemStack createInfoHead() {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§8| §fConfiguration des taux de drop")
                .addLore("")
                .addLore("  §8| §fPermet de §cmodifier§f les taux")
                .addLore("  §8| §fde drop des différents §aitems§f.")
                .addLore("")
                .addLore("  §7§oCliquez sur les items ci-dessous")
                .addLore("  §7§opour modifier leurs taux de drop.")
                .addLore("")
                .setSkullURL(INFO_HEAD_URL)
                .getItem();
    }

    /**
     * Crée une version améliorée de l'item drop avec plus d'informations
     */
    private ItemStack createEnhancedDropItem(DropItemRate dropItem) {
        String statusColor = dropItem.getAmount() > 0 ? "§a" : "§c";
        String status = dropItem.getAmount() > 0 ? "Activé" : "Désactivé";

        return new ItemCreator(dropItem.getMaterial())
                .setDurability(dropItem.getData())
                .setName("§8| §f" + dropItem.getName())
                .addLore("")
                .addLore("  §8| §fTaux actuel: " + statusColor + "+" + dropItem.getAmount() + "%")
                .addLore("  §8| §fStatut: " + statusColor + status)
                .addLore("")
                .addLore("§e▶ §fClic gauche: §a+5%")
                .addLore("§e▶ §fClic droit: §c-5%")
                .addLore("")
                .getItem();
    }

    /**
     * Crée la tête d'instructions
     */
    private ItemStack createInstructionHead() {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§8| §fInstructions")
                .addLore("")
                .addLore("  §e▶ §fClic gauche: §aAugmenter de 5%")
                .addLore("  §e▶ §fClic droit: §cDiminuer de 5%")
                .addLore("")
                .addLore("  §7Les taux peuvent aller de §c0%§7 à §a100%")
                .addLore("")
                .setSkullURL("http://textures.minecraft.net/texture/46ba63344f49dd1c4f5488e926bf3d9e2b29916a6c50d610bb40a5273dc8c82")
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
        // Gestion des têtes personnalisées pour le retour
        if (clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();
            if (itemName.contains("§fRevenir en arrière")) {
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                return;
            }
        }

        // Gestion des items de drop
        for (DropItemRate dropItemRate : DropItemRate.values()) {
            if (dropItemRate.getMaterial() == clickedItem.getType() &&
                    clickedItem.hasItemMeta() &&
                    clickedItem.getItemMeta().hasDisplayName() &&
                    clickedItem.getItemMeta().getDisplayName().contains(dropItemRate.getName())) {

                // Sauvegarder l'ancien taux pour les messages
                int oldAmount = dropItemRate.getAmount();

                // Modifier le taux
                dropItemRate.toggleAmount(clickType);

                // Jouer des sons différents selon l'action
                if (clickType == ClickType.LEFT) {
                    if (dropItemRate.getAmount() > oldAmount) {
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.5F);
                    } else {
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        player.sendMessage("§c§l✗ §fTaux maximum atteint (§c100%§f)!");
                    }
                } else if (clickType == ClickType.RIGHT) {
                    if (dropItemRate.getAmount() < oldAmount) {
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.8F);
                    } else {
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        player.sendMessage("§c§l✗ §fTaux minimum atteint (§c0%§f)!");
                    }
                }

                // Actualiser l'inventaire
                this.gameManager.getApi().openInventory(player, getClass());
                break;
            }
        }
    }

    @Override
    public int getRows() {
        return 5; // 45 slots (5 rangées de 9)
    }
}