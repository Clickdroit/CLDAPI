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
    SLOTS("gameSlot", Material.SKULL_ITEM, 3, "§8| §fSlots",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle nombre de §cjoueurs§f autorisés", "  §8| §fà se §aconnecter§f à la §cpartie§f.", "" },
            false, true),

    PVP_TIME("pvpTime", Material.DIAMOND_SWORD, 0, "§8| §cP§fv§cP",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle temps avant l'activation", "  §8| §fdu §6PvP§f durant la §cpartie§f.", "" },
            true, false),

    EPISODE_TIME("episodeTime", Material.WATCH, 0, "§8| §fDurée §f: §cEpisode",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle temps entre chaque épisode.", "" },
            true, false),

    BORDER_TIME("borderTime", Material.STAINED_GLASS, 4, "§8| §fBordure",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle temps avant l'activation", "  §8| §fde la réduction de la", "  §8| §fbordure durant la partie.", "" },
            true, false),

    CYCLE_DURATION("dayNightDuration", Material.WATCH, 0, "§8| §fDurée du cycle jour/nuit",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fla durée du cycle", "  §8| §fjour/nuit de la §cpartie.", "  §8| §fLe temps du §bjour§f ou de la ", "  §8| §cnuit§f sera égal à la §cmoitié§f de", "  §8| §fla valeur choisie.", "" },
            false, true),

    DIAMOND_MAX("diamondMax", Material.DIAMOND, 0, "§8| §fDiamants",
            new String[] { "", "  §8| §fVous permet de §climiter", "  §8| §fle minage des diamants.", "" },
            false, true),

    GOLD_MAX("goldMax", Material.GOLD_INGOT, 0, "§8| §fOrs",
            new String[] { "", "  §8| §fVous permet de §climiter", "  §8| §fle minage de l'or.", "" },
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

            itemCreator.addLore(" §8> §fAccès §f: §6§lHost");
            itemCreator.addLore(" §8> §fConfiguration: §c" + (this.toDigital ? Chrono.timeToDigitalString(amount) : String.valueOf(amount)));
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

        itemCreator.addLore(" §8> §fConfiguration: §c" + result + " minute" + (result > 1 ? "s" : ""));
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

