package fr.clickdroit.api.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventaire GUI avec support de pagination.
 */
public abstract class PaginatedGUI extends GUIInventory {

    protected int currentPage = 0;
    protected int itemsPerPage;
    protected List<ItemStack> items = new ArrayList<>();

    // Slots pour la navigation (dernière ligne par défaut)
    protected int previousPageSlot;
    protected int nextPageSlot;
    protected int pageInfoSlot;

    protected PaginatedGUI(String name, int rows) {
        super(name, rows);
        this.itemsPerPage = (rows - 1) * 7; // Laisse la bordure et la dernière ligne
        this.previousPageSlot = (rows - 1) * 9 + 3;
        this.nextPageSlot = (rows - 1) * 9 + 5;
        this.pageInfoSlot = (rows - 1) * 9 + 4;
    }

    @Override
    protected ItemStack[] buildContents(Player player) {
        clear();

        // Remplir avec les items de la page actuelle
        List<ItemStack> pageItems = getPageItems();
        int slot = 10; // Commence à la deuxième ligne, deuxième colonne

        for (ItemStack item : pageItems) {
            // Skip les bordures
            if (slot % 9 == 0) slot++;
            if (slot % 9 == 8) slot += 2;
            if (slot >= (rows - 1) * 9) break;

            final int currentSlot = slot;
            setItem(slot, item, (p, i, c) -> onItemClick(p, i, c, currentSlot));
            slot++;
        }

        // Ajouter la navigation
        addNavigation();

        // Bordure
        border(getBorderItem());

        return contents;
    }

    /**
     * Retourne les items à afficher (à implémenter).
     */
    protected abstract List<ItemStack> getAllItems(Player player);

    /**
     * Appelé lors du clic sur un item de la liste.
     */
    protected abstract void onItemClick(Player player, ItemStack item, ClickType clickType, int slot);

    /**
     * Retourne l'item de bordure.
     */
    protected abstract ItemStack getBorderItem();

    /**
     * Retourne l'item de page précédente.
     */
    protected abstract ItemStack getPreviousPageItem();

    /**
     * Retourne l'item de page suivante.
     */
    protected abstract ItemStack getNextPageItem();

    /**
     * Retourne l'item d'info de page.
     */
    protected abstract ItemStack getPageInfoItem(int currentPage, int totalPages);

    private List<ItemStack> getPageItems() {
        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());

        if (start >= items.size()) {
            return new ArrayList<>();
        }

        return items.subList(start, end);
    }

    private void addNavigation() {
        int totalPages = getTotalPages();

        // Page précédente
        if (currentPage > 0) {
            setItem(previousPageSlot, getPreviousPageItem(), (p, i, c) -> {
                currentPage--;
                refresh(p);
            });
        }

        // Info page
        setItem(pageInfoSlot, getPageInfoItem(currentPage + 1, totalPages), null);

        // Page suivante
        if (currentPage < totalPages - 1) {
            setItem(nextPageSlot, getNextPageItem(), (p, i, c) -> {
                currentPage++;
                refresh(p);
            });
        }
    }

    public int getTotalPages() {
        return Math.max(1, (int) Math.ceil((double) items.size() / itemsPerPage));
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int page) {
        this.currentPage = Math.max(0, Math.min(page, getTotalPages() - 1));
    }

    public void setItems(List<ItemStack> items) {
        this.items = new ArrayList<>(items);
        this.currentPage = 0;
    }
}

