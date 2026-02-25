package fr.clickdroit.api.config.value;

import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CommonItems {
    GUI_BACK_ITEM("§8| §fRevenir en §carrière", Material.SKULL_ITEM, 3, "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645"),
    GUI_CLOSE_ITEM("§8| §fFermer l'inventaire", Material.SKULL_ITEM, 3, "http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");

    private final String name;
    private final Material material;
    private final int data;
    private final String skullURL;

    CommonItems(String name, Material material, int data, String skullURL) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.skullURL = skullURL;
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

    public String getSkullURL() {
        return this.skullURL;
    }

    public ItemStack getItem() {
        ItemCreator creator = new ItemCreator(this.material)
                .setDurability(Integer.valueOf(this.data))
                .setName(this.name);

        // Si c'est une tête et qu'on a une URL, l'appliquer
        if (this.material == Material.SKULL_ITEM && this.skullURL != null && !this.skullURL.isEmpty()) {
            creator.setSkullURL(this.skullURL);
        }

        return creator.getItem();
    }
}
