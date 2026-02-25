package fr.clickdroit.api.config;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.module.GameModule;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * GUI de sélection du mode de jeu.
 * <p>
 * Cette interface permet aux hosts de sélectionner le mode de jeu parmi :
 * <ul>
 * <li>Les modes intégrés (UHC, LG, etc.)</li>
 * <li>Les modules externes enregistrés par d'autres plugins</li>
 * </ul>
 * </p>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameModeSelectionGUI implements CustomInventory {

    private static final String EXTERNAL_MODULE_LORE_PREFIX = "§0§0§0";

    @Override
    public String getName() {
        return "§f(§c!§f) §cSélection du mode de jeu";
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        API api = API.getAPI();

        // Bordure en verre cyan
        Integer[] glass = { 0, 1, 7, 8, 9, 17, 36, 37, 43, 44 };
        for (int j : glass) {
            slots[j] = new ItemCreator(Material.STAINED_GLASS_PANE)
                    .setDurability(11)
                    .setName("§f")
                    .getItem();
        }

        // Module UHC intégré (toujours en premier)
        ModuleType currentModule = api.getGameManager().getModuleManager().getCurrentModule();
        GameModule activeExternalModule = api.getActiveGameModule();

        int slotIndex = 10;

        // Ajouter les modules intégrés (ModuleType)
        for (ModuleType moduleType : ModuleType.values()) {
            if (slotIndex >= 35)
                break; // Éviter de dépasser les slots disponibles

            boolean isSelected = (activeExternalModule == null && currentModule == moduleType);

            ItemCreator item = new ItemCreator(moduleType.getMaterial())
                    .setDurability(moduleType.getData())
                    .setName((isSelected ? "§a§l✓ " : "§f") + moduleType.getColor() + moduleType.getName())
                    .addLore("")
                    .addLore(" §8> §fMode intégré")
                    .addLore(" §8> §fÉquipes §f: " + (moduleType.hasTeam() ? "§aOui" : "§cNon"))
                    .addLore(" §8> §fRôles §f: " + (moduleType.isHasRole() ? "§aOui" : "§cNon"))
                    .addLore("");

            if (isSelected) {
                item.addLore("  §a§lMODE ACTIF")
                        .addLore("");
            } else {
                item.addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                        .addLore("");
            }

            slots[slotIndex] = item.getItem();
            slotIndex++;

            // Sauter les bordures
            if (slotIndex == 17)
                slotIndex = 19;
            if (slotIndex == 26)
                slotIndex = 28;
        }

        // Ajouter les modules externes
        Collection<GameModule> externalModules = api.getModuleRegistry().getRegisteredModules();
        for (GameModule module : externalModules) {
            if (slotIndex >= 35)
                break;

            boolean isSelected = (activeExternalModule != null &&
                    activeExternalModule.getId().equals(module.getId()));

            ItemCreator item = new ItemCreator(module.getIconMaterial())
                    .setDurability(module.getIconData())
                    .setName((isSelected ? "§a§l✓ " : "§f") + module.getColor() + module.getDisplayName())
                    .addLore("")
                    .addLore(" §8> §6Module externe")
                    .addLore(" §8> §fPlugin §f: §e" + module.getOwnerPlugin().getName())
                    .addLore(" §8> §fÉquipes §f: " + (module.hasTeams() ? "§aOui" : "§cNon"))
                    .addLore(" §8> §fRôles §f: " + (module.hasRoles() ? "§aOui" : "§cNon"))
                    .addLore("");

            // Ajouter la description du module
            for (String line : module.getDescription()) {
                item.addLore("  §7" + line);
            }
            item.addLore("");

            if (isSelected) {
                item.addLore("  §a§lMODE ACTIF")
                        .addLore("");
            } else {
                item.addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                        .addLore("");
            }

            // Ajouter une ligne cachée pour identifier le module externe
            item.addLore(EXTERNAL_MODULE_LORE_PREFIX + module.getId());

            slots[slotIndex] = item.getItem();
            slotIndex++;

            // Sauter les bordures
            if (slotIndex == 17)
                slotIndex = 19;
            if (slotIndex == 26)
                slotIndex = 28;
        }

        // Bouton retour
        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        API api = API.getAPI();

        // Vérifier que la partie n'est pas en cours
        if (!api.getGameManager().getGameState().equals(GameState.WAITING)) {
            player.sendMessage("§cVous ne pouvez pas changer de mode de jeu pendant une partie !");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            return;
        }

        // Bouton retour
        if (clickedItem.getType() == Material.ARROW) {
            api.openInventory(player, ConfigMainGUI.class);
            return;
        }

        // Vérifier si c'est un module externe (en cherchant dans le lore)
        if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasLore()) {
            for (String loreLine : clickedItem.getItemMeta().getLore()) {
                if (loreLine.startsWith(EXTERNAL_MODULE_LORE_PREFIX)) {
                    String moduleId = loreLine.substring(EXTERNAL_MODULE_LORE_PREFIX.length());
                    GameModule module = api.getModuleRegistry().getModule(moduleId);

                    if (module != null) {
                        // Activer le module externe
                        api.setActiveGameModule(module);
                        player.sendMessage(
                                "§aMode de jeu changé vers : " + module.getColor() + module.getDisplayName());
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                        api.openInventory(player, getClass());
                    }
                    return;
                }
            }
        }

        // Sinon, chercher parmi les ModuleType intégrés
        for (ModuleType moduleType : ModuleType.values()) {
            if (clickedItem.getType() == moduleType.getMaterial() &&
                    clickedItem.getDurability() == moduleType.getData()) {

                // Désactiver tout module externe actif
                api.clearActiveGameModule();

                // Définir le module intégré
                api.getGameManager().getModuleManager().setCurrentModule(moduleType);

                player.sendMessage("§aMode de jeu changé vers : " + moduleType.getColor() + moduleType.getName());
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                api.openInventory(player, getClass());
                return;
            }
        }
    }

    @Override
    public int getRows() {
        return 5;
    }
}
