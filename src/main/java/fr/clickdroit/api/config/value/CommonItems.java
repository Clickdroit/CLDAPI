package fr.clickdroit.api.config.value;

import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CommonItems {
    GUI_BACK_ITEM("en ", Material.ARROW, 0),
            GUI_CLOSE_ITEM("l'inventaire", Material.ARROW, 0);

    private final String name;

    private final Material material;

    private final int data;

    CommonItems(String name, Material material, int data) {
        this.name = name;
        this.material = material;
        this.data = data;
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public ItemStack getItem() {
        return (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setName(this.name).getItem();
    }
}

