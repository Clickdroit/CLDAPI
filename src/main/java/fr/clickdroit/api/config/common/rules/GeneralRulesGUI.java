package fr.clickdroit.api.config.common.rules;

import fr.clickdroit.api.common.rules.items.GeneralRules;
import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GeneralRulesGUI implements CustomInventory {
    private final GameManager gameManager;

    private final Map<Integer, GeneralRules> rules;

    private final Map<Integer, UseItems> itemsRules;

    public GeneralRulesGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.rules = new HashMap<>();
        this.itemsRules = new HashMap<>();
    }

    private void setup() {
        this.rules.put(Integer.valueOf(10), GeneralRules.DIAMOND_HELMET);
        this.rules.put(Integer.valueOf(11), GeneralRules.DIAMOND_CHESTPLATE);
        this.rules.put(Integer.valueOf(12), GeneralRules.DIAMOND_LEGGINGS);
        this.rules.put(Integer.valueOf(13), GeneralRules.DIAMOND_BOOTS);
        this.rules.put(Integer.valueOf(14), GeneralRules.DIAMOND_SWORD);
        this.rules.put(Integer.valueOf(19), GeneralRules.STRIPMINING);
        this.rules.put(Integer.valueOf(20), GeneralRules.IPVP);
        this.rules.put(Integer.valueOf(21), GeneralRules.CROSSTEAM);
        this.rules.put(Integer.valueOf(22), GeneralRules.TOWER);
        this.rules.put(Integer.valueOf(23), GeneralRules.DIGDOWN);
        this.rules.put(Integer.valueOf(24), GeneralRules.ROLLERCOASTER);
        this.rules.put(Integer.valueOf(28), GeneralRules.HEALTH);
        this.rules.put(Integer.valueOf(29), GeneralRules.MUMBLE);
        this.rules.put(Integer.valueOf(30), GeneralRules.PRIVATEMSG);
        int slot = 37;
        for (UseItems itemsRules : UseItems.values()) {
            this.itemsRules.put(Integer.valueOf(slot), itemsRules);
            slot++;
        }
    }

    public String getName() {
        return "Règles de la partie";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        Integer[] glass = {
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(36), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46),
                Integer.valueOf(52), Integer.valueOf(53) };
        Integer[] arrayOfInteger1;
        int i;
        byte b;
        for (arrayOfInteger1 = glass, i = arrayOfInteger1.length, b = 0; b < i; ) {
            int j = arrayOfInteger1[b].intValue();
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(7)).getItem();
            b = (byte)(b + 1);
        }
        setup();
        for (Map.Entry<Integer, GeneralRules> entry : this.rules.entrySet())
            slots[((Integer)entry.getKey()).intValue()] = ((GeneralRules)entry.getValue()).getItem();
        for (Map.Entry<Integer, UseItems> entry : this.itemsRules.entrySet())
            slots[((Integer)entry.getKey()).intValue()] = ((UseItems)entry.getValue()).getItem();
        slots[49] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        if (this.rules.containsKey(Integer.valueOf(slot))) {
            GeneralRules rules = this.rules.get(Integer.valueOf(slot));
            rules.toggleEnabled();
        }
        if (this.itemsRules.containsKey(Integer.valueOf(slot))) {
            UseItems rules = this.itemsRules.get(Integer.valueOf(slot));
            rules.toggleEnabled();
        }
        this.gameManager.getApi().openInventory(player, getClass());
        switch (clickedItem.getType()) {
            case ARROW:
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 6;
    }
}
