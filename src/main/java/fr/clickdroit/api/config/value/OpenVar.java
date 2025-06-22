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
    SLOTS("gameSlot", Material.SKULL_ITEM, 3, "", new String[] { "", "  permet de ", "  nombre de autoris", "  se la ", "" }, false, true),
    PVP_TIME("pvpTime", Material.DIAMOND_SWORD, 0, "", new String[] { "", "  permet de" , "  temps avant l'", "  durant la" , "" }, true, false),
    EPISODE_TIME("episodeTime", Material.WATCH, 0, "", new String[] { "", "  permet de ", "  temps entre chaque ", "" }, true, false),
    BORDER_TIME("borderTime", Material.STAINED_GLASS, 4, "", new String[] { "", "  permet de" , "  temps avant l'activation", "  la rde la.", "  durant la partie.", "" }, true, false),
    CYCLE_DURATION("dayNightDuration", Material.WATCH, 0, "du cycle jour/nuit", new String[] { "", "  permet de ", "  durdu ", "  de la" , "  temps du ou de la", "  la de", "  valeur choisie.", "" }, false, true),
    DIAMOND_MAX("diamondMax", Material.DIAMOND, 0, "", new String[] { "", "  permet de ", "  minage des ", "" }, false, true),
    GOLD_MAX("goldMax", Material.GOLD_INGOT, 0, "", new String[] { "", "  permet de" , "  minage de l'", "" }, false, true);

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
        ItemCreator itemCreator = (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setName(this.itemName).setTableauLores(this.itemDescription).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        try {
            int amount = GameConfig.class.getField(this.var).getInt(API.getAPI().getGameManager().getGameConfig());
            itemCreator.addLore("" );
                    itemCreator.addLore(" "+ (this.toDigital ? Chrono.timeToDigitalString(amount) : (String)Integer.valueOf(amount)));
                            itemCreator.addLore("");
            itemCreator.addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage());
            itemCreator.addLore("");
            if (this.stack)
                itemCreator.setAmount(Integer.valueOf(GameConfig.class.getField(this.var).getInt(API.getAPI().getGameManager().getGameConfig())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemCreator.getItem();
    }

    public ItemStack getItemCycle() {
        ItemCreator itemCreator = (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setName(this.itemName).setTableauLores(this.itemDescription);
        long value = API.getAPI().getGameManager().getGameConfig().getDayNightDuration();
        int result = Chrono.getCycleDurationTime(value);
        itemCreator.addLore("" + result + " minute" + ((result > 1) ? "s" : ""));
                itemCreator.addLore("");
        itemCreator.addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage());
        itemCreator.addLore("");
        if (this.stack)
            itemCreator.setAmount(Integer.valueOf(result));
        return itemCreator.getItem();
    }
}

