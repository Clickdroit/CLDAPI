package fr.clickdroit.api.utils;

import org.bukkit.Material;

public enum CommonItems {
    GUI_BACK_ARROW(Material.ARROW, 0, "§fRevenir en arrière");

    private Material material;

    private int data;

    private String name;

    CommonItems(Material material, int data, String name) {
        this.material = material;
        this.data = data;
        this.name = name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public String getName() {
        return this.name;
    }

    public ItemCreator getItem() {
        return (new ItemCreator(this.material)).setName(this.name).setDurability(Integer.valueOf(this.data));
    }
    }
