package fr.clickdroit.api.config.value;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.utils.Chrono;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum OpenVar {
    SLOTS("gameSlot", Material.SKULL_ITEM, 3, "Slots",
            new String[] { "", "  permet de définir le", "  nombre de slots autorisés", "  durant la partie.", "" },
            false, true),

    PVP_TIME("pvpTime", Material.DIAMOND_SWORD, 0, "Temps PvP",
            new String[] { "", "  permet de définir le", "  temps avant l'activation", "  du PvP durant la partie.", "" },
            true, false),

    EPISODE_TIME("episodeTime", Material.WATCH, 0, "Temps Episode",
            new String[] { "", "  permet de définir le", "  temps entre chaque épisode.", "" },
            true, false),

    BORDER_TIME("borderTime", Material.STAINED_GLASS, 4, "Temps Bordure",
            new String[] { "", "  permet de définir le", "  temps avant l'activation", "  de la réduction de la bordure", "  durant la partie.", "" },
            true, false),

    CYCLE_DURATION("dayNightDuration", Material.WATCH, 0, "Durée du cycle jour/nuit",
            new String[] { "", "  permet de définir la", "  durée du cycle jour/nuit", "  de la partie.", "  Le temps du jour ou de la nuit", "  sera égal à la moitié de", "  la valeur choisie.", "" },
            false, true),

    DIAMOND_MAX("diamondMax", Material.DIAMOND, 0, "Limite Diamants",
            new String[] { "", "  permet de limiter le", "  minage des diamants.", "" },
            false, true),

    GOLD_MAX("goldMax", Material.GOLD_INGOT, 0, "Limite Or",
            new String[] { "", "  permet de limiter le", "  minage de l'or.", "" },
            false, true);

    private final String var;
    private final Material material;
    private final int data;
    private final String itemName;
    private final String[] itemDescription;
    private final boolean toDigital;
    private final boolean stack;

    OpenVar(String var, Material material, int data, String itemName, String[] itemDescription, boolean toDigital, boolean stack) {
        this.var = var;
        this.material = material;
        this.data = data;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.toDigital = toDigital;
        this.stack = stack;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public ItemStack getItem() {
        ItemCreator itemCreator = new ItemCreator(this.material)
                .setDurability(this.data)
                .setName(this.itemName)
                .setTableauLores(this.itemDescription)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        try {
            int amount = GameConfig.class.getField(this.var).getInt(API.getAPI().getGameManager().getGameConfig());

            itemCreator.addLore("");
            itemCreator.addLore("§7Valeur: §a" + (this.toDigital ?
                    Chrono.timeToDigitalString(amount) : String.valueOf(amount)));
            itemCreator.addLore("");
            itemCreator.addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage());
            itemCreator.addLore("");

            if (this.stack && amount > 0) {
                // Correction : utiliser Math.min et Math.max au lieu de casts incorrects
                int stackAmount = Math.min(Math.max(amount, 1), 64);
                itemCreator.setAmount(stackAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemCreator.getItem();
    }
    public ItemStack getItemCycle() {
        ItemCreator itemCreator = new ItemCreator(this.material)
                .setDurability(this.data)
                .setName(this.itemName)
                .setTableauLores(this.itemDescription);

        long value = API.getAPI().getGameManager().getGameConfig().getDayNightDuration();
        int result = Chrono.getCycleDurationTime(value);

        itemCreator.addLore("§7Durée: §a" + result + " minute" + (result > 1 ? "s" : ""));
        itemCreator.addLore("");
        itemCreator.addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage());
        itemCreator.addLore("");

        if (this.stack && result > 0) {
            int stackAmount = Math.min(Math.max(result, 1), 64);
            itemCreator.setAmount(stackAmount);
        }
        return itemCreator.getItem();
    }

    public String getVar() {
        return var;
    }

    public String getItemName() {
        return itemName;
    }

    public String[] getItemDescription() {
        return itemDescription;
    }

    public boolean isToDigital() {
        return toDigital;
    }

    public boolean isStack() {
        return stack;
    }
}

