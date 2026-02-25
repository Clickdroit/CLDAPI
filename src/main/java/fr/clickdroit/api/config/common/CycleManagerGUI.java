package fr.clickdroit.api.config.common;

import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CycleManagerGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    // Enum pour les modes de cycle
    public enum CycleMode {
        ETERNAL_DAY(0, "§eJour éternel", "Le monde restera toujours en journée"),
        ETERNAL_NIGHT(1, "§8Nuit éternelle", "Le monde restera toujours en nuit"),
        NORMAL_CYCLE(2, "§6Cycle normal", "Alternance jour/nuit normale");

        private final int id;
        private final String displayName;
        private final String description;

        CycleMode(int id, String displayName, String description) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
        }

        public int getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }

        public static CycleMode fromId(int id) {
            for (CycleMode mode : values()) {
                if (mode.getId() == id) return mode;
            }
            return NORMAL_CYCLE; // Par défaut
        }
    }

    public CycleManagerGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @Override
    public String getName() {
        return "Mode jour/nuit";
    }

    @Override
    public int getSlots() {
        return 27; // 3 rangées
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[27];

        // Remplir avec du verre pour la bordure
        fillWithGlass(slots);

        long duration = this.gameConfig.getDayNightDuration();
        CycleMode currentMode;
        if (duration == 0) {
            currentMode = CycleMode.ETERNAL_DAY;
        } else if (duration == -1) {
            currentMode = CycleMode.ETERNAL_NIGHT;
        } else {
            currentMode = CycleMode.NORMAL_CYCLE;
        }

        // Options de cycle
        slots[11] = createCycleModeItem(CycleMode.ETERNAL_DAY, currentMode);
        slots[13] = createCycleModeItem(CycleMode.NORMAL_CYCLE, currentMode);
        slots[15] = createCycleModeItem(CycleMode.ETERNAL_NIGHT, currentMode);

        // Flèche de retour
        slots[22] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 7) // Gris
                .setName("§0") // Nom invisible
                .getItem();

        for (int i = 0; i < slots.length; i++) {
            slots[i] = glassPane;
        }
    }

    private ItemStack createCycleModeItem(CycleMode mode, CycleMode currentMode) {
        Material material;
        short durability = 0;
        boolean isSelected = (mode == currentMode);

        switch (mode) {
            case ETERNAL_DAY:
                material = Material.GLOWSTONE;
                break;
            case ETERNAL_NIGHT:
                material = Material.OBSIDIAN;
                break;
            case NORMAL_CYCLE:
                material = Material.WATCH;
                break;
            default:
                material = Material.WATCH;
        }

        ItemCreator creator = new ItemCreator(material);
        if (durability > 0) {
            creator.setDurability(durability);
        }

        creator.setName("§8| " + mode.getDisplayName())
                .addLore("")
                .addLore("  §8| §f" + mode.getDescription())
                .addLore("");

        if (isSelected) {
            creator.addLore(" §8> §fStatut: §a§lSÉLECTIONNÉ")
                    .addLore("")
                    .addLore("§7▶ Déjà activé");
        } else {
            creator.addLore(" §8> §fStatut: §c§lDÉSACTIVÉ")
                    .addLore("")
                    .addLore("§e▶ Cliquez pour activer");
        }

        creator.addLore("");

        return creator.getItem();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        CycleMode selectedMode = null;

        // Déterminer quel mode a été cliqué
        if (slot == 11) { // Jour éternel
            selectedMode = CycleMode.ETERNAL_DAY;
        } else if (slot == 13) { // Cycle normal
            selectedMode = CycleMode.NORMAL_CYCLE;
        } else if (slot == 15) { // Nuit éternelle
            selectedMode = CycleMode.ETERNAL_NIGHT;
        } else if (slot == 22) { // Gestion de la flèche de retour par SLOT
            this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
            player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,0.8F);
            return;
        }

        if (selectedMode != null) {
            // Appliquer le mode sélectionné
            switch (selectedMode) {
                case ETERNAL_DAY:
                    this.gameConfig.setDayNightDuration(0L); // 0 = Jour éternel
                    break;
                case ETERNAL_NIGHT:
                    this.gameConfig.setDayNightDuration(-1L); // -1 = Nuit éternelle
                    break;
                case NORMAL_CYCLE:
                    this.gameConfig.setDayNightDuration(1200L); // 20 minutes par défaut
                    break;
            }

            // Son de confirmation
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
