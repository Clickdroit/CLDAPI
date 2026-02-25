package fr.clickdroit.api.config.gamemode;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.module.GameModule;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * GUI de sélection des modes de jeu.
 * Affiche les modes intégrés (enum ModuleType) et les modules externes
 * enregistrés.
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameModeSelectionGUI implements CustomInventory {

    private final API api;
    private final GameManager gameManager;

    public GameModeSelectionGUI(API api) {
        this.api = api;
        this.gameManager = api.getGameManager();
    }

    @Override
    public String getName() {
        return "§f(§c!§f) §cSélection du Mode";
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        // Bordure en verre
        Integer[] glass = { 0, 1, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 43, 44 };
        for (int slot : glass) {
            slots[slot] = new ItemCreator(Material.STAINED_GLASS_PANE)
                    .setDurability(11)
                    .setName("§f")
                    .getItem();
        }

        ModuleType currentModule = gameManager.getModuleManager().getCurrentModule();
        int slotIndex = 10;

        // Modes intégrés (enum ModuleType)
        for (ModuleType moduleType : ModuleType.values()) {
            if (slotIndex >= 35)
                break; // Limite d'espace

            boolean isSelected = currentModule == moduleType;

            ItemCreator item = new ItemCreator(moduleType.getMaterial())
                    .setDurability(moduleType.getData())
                    .setName("§8| " + moduleType.getColor() + "§l" + moduleType.getName())
                    .addLore("")
                    .addLore(" §8> §fType §f: §eIntégré")
                    .addLore(" §8> §fRôles §f: " + (moduleType.isHasRole() ? "§aOui" : "§cNon"))
                    .addLore(" §8> §fÉquipes §f: " + (moduleType.hasTeam() ? "§aOui" : "§cNon"))
                    .addLore("");

            if (isSelected) {
                item.addGlowEffect()
                        .addLore(" §a§l✓ SÉLECTIONNÉ")
                        .addLore("");
            } else {
                item.addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage())
                        .addLore("");
            }

            slots[slotIndex] = item.getItem();
            slotIndex++;

            // Sauter les slots de bordure
            if (slotIndex == 17 || slotIndex == 18)
                slotIndex = 19;
            if (slotIndex == 26 || slotIndex == 27)
                slotIndex = 28;
        }

        // Modules externes
        Collection<GameModule> externalModules = api.getModuleRegistry().getRegisteredModules();
        for (GameModule module : externalModules) {
            if (slotIndex >= 35)
                break;

            boolean isSelected = isExternalModuleSelected(module);

            ItemCreator item = new ItemCreator(module.getIconMaterial())
                    .setDurability(module.getIconData())
                    .setName("§8| " + module.getColor() + "§l" + module.getDisplayName())
                    .addLore("")
                    .addLore(" §8> §fType §f: §dExterne")
                    .addLore(" §8> §fID §f: §7" + module.getId())
                    .addLore(" §8> §fRôles §f: " + (module.hasRoles() ? "§aOui" : "§cNon"))
                    .addLore(" §8> §fÉquipes §f: " + (module.hasTeams() ? "§aOui" : "§cNon"))
                    .addLore("");

            if (isSelected) {
                item.addGlowEffect()
                        .addLore(" §a§l✓ SÉLECTIONNÉ")
                        .addLore("");
            } else {
                item.addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage())
                        .addLore("");
            }

            slots[slotIndex] = item.getItem();
            slotIndex++;

            if (slotIndex == 17 || slotIndex == 18)
                slotIndex = 19;
            if (slotIndex == 26 || slotIndex == 27)
                slotIndex = 28;
        }

        // Info sur le mode actuel
        slots[40] = new ItemCreator(Material.BOOK)
                .setName("§8| §fInformation")
                .addLore("")
                .addLore(" §8> §fMode actuel §f: " + currentModule.getColor() + currentModule.getName())
                .addLore("")
                .addLore("  §8| §fCliquez sur un mode pour")
                .addLore("  §8| §fle §asélectionner§f pour la partie.")
                .addLore("")
                .getItem();

        // Bouton retour
        slots[38] = new ItemCreator(Material.ARROW)
                .setName("§8| §cRetour")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();

        return () -> slots;
    }

    private boolean isExternalModuleSelected(GameModule module) {
        GameModule activeModule = api.getActiveGameModule();
        if (activeModule != null) {
            return activeModule.getId().equals(module.getId());
        }
        return false;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        // Retour
        if (slot == 38 && clickedItem.getType() == Material.ARROW) {
            api.openInventory(player, fr.clickdroit.api.config.ConfigMainGUI.class);
            return;
        }

        // Ignorer les bordures et l'info
        if (clickedItem.getType() == Material.STAINED_GLASS_PANE ||
                clickedItem.getType() == Material.BOOK) {
            return;
        }

        // Sélectionner un mode intégré
        for (ModuleType moduleType : ModuleType.values()) {
            if (clickedItem.getType() == moduleType.getMaterial()) {
                selectBuiltInModule(player, moduleType);
                return;
            }
        }

        // Sélectionner un module externe
        for (GameModule module : api.getModuleRegistry().getRegisteredModules()) {
            if (clickedItem.getType() == module.getIconMaterial()) {
                selectExternalModule(player, module);
                return;
            }
        }
    }

    private void selectBuiltInModule(Player player, ModuleType moduleType) {
        gameManager.getModuleManager().setCurrentModule(moduleType);

        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        player.sendMessage("");
        player.sendMessage("§a§l✓ Mode sélectionné !");
        player.sendMessage("§fLe mode §e" + moduleType.getName() + " §fest maintenant actif.");
        player.sendMessage("");

        // Rafraîchir le GUI
        api.openInventory(player, getClass());
    }

    private void selectExternalModule(Player player, GameModule module) {
        // Définir ce module comme module de jeu actif
        api.setActiveGameModule(module);

        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        player.sendMessage("");
        player.sendMessage("§a§l✓ Mode externe sélectionné !");
        player.sendMessage("§fLe mode §d" + module.getDisplayName() + " §fest maintenant actif.");
        player.sendMessage("");

        // Rafraîchir le GUI
        api.openInventory(player, getClass());
    }

    @Override
    public int getRows() {
        return 5;
    }
}
