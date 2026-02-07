package fr.clickdroit.api.utils.gui;

import fr.clickdroit.api.API;
import fr.clickdroit.api.utils.CustomInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Classe de base améliorée pour les inventaires GUI.
 * Fournit des méthodes utilitaires pour créer des interfaces graphiques.
 */
public abstract class GUIInventory implements CustomInventory {

    protected final Map<Integer, ClickHandler> clickHandlers = new HashMap<>();
    protected final String name;
    protected final int rows;
    protected ItemStack[] contents;

    protected GUIInventory(String name, int rows) {
        this.name = name;
        this.rows = rows;
        this.contents = new ItemStack[rows * 9];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        return () -> buildContents(player);
    }

    /**
     * Construit le contenu de l'inventaire pour un joueur.
     * À implémenter par les sous-classes.
     *
     * @param player le joueur
     * @return le contenu de l'inventaire
     */
    protected abstract ItemStack[] buildContents(Player player);

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack item, int slot, ClickType clickType) {
        ClickHandler handler = clickHandlers.get(slot);
        if (handler != null) {
            handler.handle(player, item, clickType);
        }
    }

    // ===== Méthodes utilitaires =====

    /**
     * Définit un item avec un handler de clic.
     */
    protected void setItem(int slot, ItemStack item, ClickHandler handler) {
        if (slot >= 0 && slot < contents.length) {
            contents[slot] = item;
            if (handler != null) {
                clickHandlers.put(slot, handler);
            }
        }
    }

    /**
     * Définit un item sans handler.
     */
    protected void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    /**
     * Remplit tout l'inventaire avec un item.
     */
    protected void fill(ItemStack item) {
        for (int i = 0; i < contents.length; i++) {
            contents[i] = item;
        }
    }

    /**
     * Crée une bordure autour de l'inventaire.
     */
    protected void border(ItemStack item) {
        int size = rows * 9;
        for (int i = 0; i < 9; i++) {
            contents[i] = item; // Première ligne
            contents[size - 9 + i] = item; // Dernière ligne
        }
        for (int i = 1; i < rows - 1; i++) {
            contents[i * 9] = item; // Première colonne
            contents[i * 9 + 8] = item; // Dernière colonne
        }
    }

    /**
     * Remplit les slots vides avec un item.
     */
    protected void fillEmpty(ItemStack item) {
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] == null || contents[i].getType() == Material.AIR) {
                contents[i] = item;
            }
        }
    }

    /**
     * Efface tous les items et handlers.
     */
    protected void clear() {
        contents = new ItemStack[rows * 9];
        clickHandlers.clear();
    }

    /**
     * Ouvre cet inventaire pour un joueur.
     */
    public void open(Player player) {
        API.getAPI().openInventory(player, this.getClass());
    }

    /**
     * Rafraîchit l'inventaire pour un joueur.
     */
    public void refresh(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory != null && inventory.getTitle().equals(name)) {
            inventory.setContents(buildContents(player));
        }
    }

    /**
     * Interface fonctionnelle pour les handlers de clic.
     */
    @FunctionalInterface
    public interface ClickHandler {
        void handle(Player player, ItemStack item, ClickType clickType);
    }
}

