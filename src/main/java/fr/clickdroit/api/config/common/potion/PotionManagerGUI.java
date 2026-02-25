package fr.clickdroit.api.config.common.potion;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class PotionManagerGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;
    private final Map<Integer, Potions> potionsMap;

    // Map pour stocker la catégorie actuellement sélectionnée pour chaque potion
    private final Map<Potions, PotionCategory> selectedCategories;

    // Instance statique pour permettre l'accès depuis d'autres classes
    private static PotionManagerGUI instance;

    public PotionManagerGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
        this.potionsMap = new HashMap<>();
        this.selectedCategories = new HashMap<>();
        instance = this;
        initializeSelectedCategories();
    }

    // Méthode statique pour accéder à l'instance
    public static PotionManagerGUI getInstance() {
        return instance;
    }

    public String getName() {
        return "Gestion des Potions";
    }

    private void initializeSelectedCategories() {
        // Initialiser toutes les potions avec la première catégorie disponible
        for (Potions potion : Potions.values()) {
            this.selectedCategories.put(potion, potion.getAvailableCategories()[0]);
        }
    }

    private void setupPotions() {
        // Layout des potions dans le GUI (5x9 = 45 slots) - seulement les potions 1.8 survie
        this.potionsMap.put(10, Potions.SPEED);
        this.potionsMap.put(11, Potions.STRENGTH);
        this.potionsMap.put(12, Potions.JUMP_BOOST);
        this.potionsMap.put(13, Potions.REGENERATION);
        this.potionsMap.put(14, Potions.HEAL);
        this.potionsMap.put(15, Potions.POISON);
        this.potionsMap.put(16, Potions.HARM);

        this.potionsMap.put(19, Potions.FIRE_RESISTANCE);
        this.potionsMap.put(20, Potions.NIGHT_VISION);
        this.potionsMap.put(21, Potions.INVISIBILITY);
        this.potionsMap.put(22, Potions.WATER_BREATHING);
        this.potionsMap.put(23, Potions.SLOWNESS);
        this.potionsMap.put(24, Potions.WEAKNESS);
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        // Remplir avec du verre gris pour la bordure
        fillWithGlass(slots);

        setupPotions();

        // Placer les potions avec leurs catégories actuelles
        for (Map.Entry<Integer, Potions> entry : this.potionsMap.entrySet()) {
            Potions potion = entry.getValue();
            PotionCategory selectedCategory = this.selectedCategories.get(potion);
            slots[entry.getKey()] = potion.getItemForCategory(selectedCategory);
        }

        // Bouton de retour
        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setDurability(7) // Gris
                .setName("§0")
                .getItem();

        for (int i = 0; i < slots.length; i++) {
            slots[i] = glassPane;
        }
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        if (slot == 40) {
            this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
            player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,0.8F);
            return;
        }

        if (this.potionsMap.containsKey(slot)) {
            Potions potion = this.potionsMap.get(slot);
            PotionCategory currentCategory = this.selectedCategories.get(potion);

            if (clickType == ClickType.RIGHT) {
                // Changer de catégorie (clic droit)
                PotionCategory[] availableCategories = potion.getAvailableCategories();
                int currentIndex = 0;

                // Trouver l'index actuel
                for (int i = 0; i < availableCategories.length; i++) {
                    if (availableCategories[i] == currentCategory) {
                        currentIndex = i;
                        break;
                    }
                }

                // Passer à la catégorie suivante (cyclique)
                int nextIndex = (currentIndex + 1) % availableCategories.length;
                this.selectedCategories.put(potion, availableCategories[nextIndex]);

            } else if (clickType == ClickType.LEFT) {
                // Toggle l'état de la catégorie actuelle (clic gauche)
                currentCategory.toggleEnabled(potion);
            }

            // Rafraîchir le GUI
            API.getAPI().openInventory(player, getClass());
        }
    }

    public int getRows() {
        return 5;
    }

    public enum PotionCategory {
        NIVEAU_II("Niveau II"),
        LONGUE_DUREE("Longue durée"),
        SPLASH("Splash");

        private final String displayName;
        private final Map<Potions, Boolean> enabledStates = new HashMap<>();

        PotionCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isEnabled(Potions potion) {
            return enabledStates.getOrDefault(potion, true);
        }

        public void toggleEnabled(Potions potion) {
            enabledStates.put(potion, !isEnabled(potion));
        }

        public String getStatusText(Potions potion) {
            return isEnabled(potion) ? "§aAutorisé" : "§cDésactivé";
        }
    }

    public enum Potions {
        // Potions avec niveau II possible (Minecraft 1.8 survie)
        SPEED("Vitesse", Material.POTION, 8258, Material.SUGAR,
                "Augmente la vitesse de déplacement",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        STRENGTH("Force", Material.POTION, 8233, Material.BLAZE_POWDER,
                "Augmente les dégâts d'attaque",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        JUMP_BOOST("Saut amélioré", Material.POTION, 8267, Material.RABBIT_FOOT,
                "Permet de sauter plus haut",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        REGENERATION("Régénération", Material.POTION, 8257, Material.GHAST_TEAR,
                "Régénère la santé au fil du temps",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        HEAL("Soin instantané", Material.POTION, 8229, Material.SPECKLED_MELON,
                "Restaure instantanément la santé",
                PotionCategory.NIVEAU_II, PotionCategory.SPLASH),

        POISON("Poison", Material.POTION, 8260, Material.SPIDER_EYE,
                "Inflige des dégâts de poison",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        HARM("Dégâts instantanés", Material.POTION, 8236, Material.FERMENTED_SPIDER_EYE,
                "Inflige des dégâts instantanés",
                PotionCategory.NIVEAU_II, PotionCategory.SPLASH),

        SLOWNESS("Lenteur", Material.POTION, 8234, Material.FERMENTED_SPIDER_EYE,
                "Réduit la vitesse de déplacement",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        // Potions sans niveau II (Minecraft 1.8 survie)
        FIRE_RESISTANCE("Résistance au feu", Material.POTION, 8259, Material.MAGMA_CREAM,
                "Immunité contre le feu et la lave",
                PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        NIGHT_VISION("Vision nocturne", Material.POTION, 8262, Material.GOLDEN_CARROT,
                "Permet de voir dans l'obscurité",
                PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        INVISIBILITY("Invisibilité", Material.POTION, 8270, Material.GOLDEN_CARROT,
                "Rend le joueur invisible",
                PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        WATER_BREATHING("Respiration aquatique", Material.POTION, 8269, Material.RAW_FISH,
                "Permet de respirer sous l'eau",
                PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH),

        WEAKNESS("Faiblesse", Material.POTION, 8232, Material.FERMENTED_SPIDER_EYE,
                "Réduit les dégâts d'attaque",
                PotionCategory.NIVEAU_II, PotionCategory.LONGUE_DUREE, PotionCategory.SPLASH);

        private final String name;
        private final Material itemMaterial;
        private final int idItem;
        private final Material material;
        private final String description;
        private final PotionCategory[] availableCategories;

        Potions(String name, Material itemMaterial, int idItem, Material material, String description, PotionCategory... availableCategories) {
            this.name = name;
            this.itemMaterial = itemMaterial;
            this.idItem = idItem;
            this.material = material;
            this.description = description;
            this.availableCategories = availableCategories;
        }

        public ItemStack getItemForCategory(PotionCategory selectedCategory) {
            ItemCreator creator = new ItemCreator(getItemMaterial())
                    .setName("§e" + getName())
                    .setDurability(getIdItem())
                    .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
                    .addLore("")
                    .addLore("§7Description: §f" + getDescription())
                    .addLore("");

            // Ajouter les informations des catégories
            for (PotionCategory category : availableCategories) {
                String status = category.getStatusText(this);
                String indicator = (category == selectedCategory) ? "§8➤ §6" : "§8  ";
                creator.addLore(indicator + category.getDisplayName() + ": " + status);
            }

            creator.addLore("")
                    .addLore("§8§l▪ §7Clic gauche: §fBasculer " + selectedCategory.getDisplayName().toLowerCase())
                    .addLore("§8§l▪ §7Clic droit: §fChanger de catégorie");

            return creator.getItem();
        }

        public String getName() {
            return this.name;
        }

        public Material getItemMaterial() {
            return this.itemMaterial;
        }

        public int getIdItem() {
            return this.idItem;
        }

        public Material getMaterial() {
            return this.material;
        }

        public String getDescription() {
            return this.description;
        }

        public PotionCategory[] getAvailableCategories() {
            return this.availableCategories;
        }

        // Méthodes de compatibilité pour les autres classes
        public boolean isEnabled() {
            // Une potion est considérée comme activée si au moins une de ses catégories est activée
            for (PotionCategory category : availableCategories) {
                if (category.isEnabled(this)) {
                    return true;
                }
            }
            return false;
        }

        public void toggleEnabled() {
            // Pour la compatibilité, on active/désactive toutes les catégories
            boolean shouldEnable = !isEnabled();
            for (PotionCategory category : availableCategories) {
                if (shouldEnable && !category.isEnabled(this)) {
                    category.toggleEnabled(this);
                } else if (!shouldEnable && category.isEnabled(this)) {
                    category.toggleEnabled(this);
                }
            }
        }

        // Méthode pour vérifier si une catégorie spécifique est activée
        public boolean isCategoryEnabled(PotionCategory category) {
            return category.isEnabled(this);
        }

        // Méthodes pour activer/désactiver des catégories spécifiques
        public boolean isSplashEnabled() {
            return PotionCategory.SPLASH.isEnabled(this);
        }

        public boolean isLevelIIEnabled() {
            return PotionCategory.NIVEAU_II.isEnabled(this);
        }

        public boolean isLongDurationEnabled() {
            return PotionCategory.LONGUE_DUREE.isEnabled(this);
        }
    }
}
