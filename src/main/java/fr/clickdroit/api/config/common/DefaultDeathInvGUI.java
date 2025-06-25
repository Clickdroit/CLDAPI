package fr.clickdroit.api.config.common;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.InventoryAPI;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultDeathInvGUI implements CustomInventory {
    private static final String GREEN_CHECK_URL = "http://textures.minecraft.net/texture/a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6";

    public String getName() {
        return "Définir l'inventaire de §cmort" ;
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = InventoryAPI.itemsDeath[slot];
            if (item != null)
                slots[slot] = item;
        }
        slots[36] = InventoryAPI.itemsDeath[36];
        slots[37] = InventoryAPI.itemsDeath[37];
        slots[38] = InventoryAPI.itemsDeath[38];
        slots[39] = InventoryAPI.itemsDeath[39];
        ItemStack barrier = new ItemCreator(Material.BARRIER)
                .setName("§c§lSlot vide")
                .addLore("§7Cette zone ne peux pas")
                .addLore("§7à être remplie.")
                .getItem();

        for (int i = 40; i <= 44; i++) {
            slots[i] = barrier;
        }

        // Dernière ligne - Glass pane gris (slots 45-51)
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 7) // Gris
                .setName("§0") // Nom invisible
                .getItem();

        for (int i = 45; i <= 51; i++) {
            slots[i] = glassPane;
        }

        // Tête avec coche verte pour définir l'inventaire (slot 52)
        slots[52] = new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§8| §fDéfinir §cl'inventaire")
                .addLore("")
                .addLore("  §8| §fVous permet de §cmodifier")
                .addLore("  §8| §fl'inventaire §apar défaut")
                .addLore("  §8| §fdes joueurs lors du spawn.")
                .addLore("")
                .addLore("§a▶ Cliquez pour définir")
                .addLore("")
                .setSkullURL(GREEN_CHECK_URL)
                .getItem();

        // Bouton retour standard (slot 53)
        slots[53] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        GamePlayer gamePlayer;
        switch (slot) {
            case 52:
                gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
                gamePlayer.setEditingDeathInv(true);
                player.setGameMode(GameMode.CREATIVE);
                player.setAllowFlight(false);
                player.sendMessage(CommonString.BAR.getMessage());
                player.sendMessage("");
                player.sendMessage("§c§lDéfinir l'inventaire de mort");
                player.sendMessage("");
                player.sendMessage("§cVoici les commandes §4:");
                player.sendMessage(" §8| §4/§cfinish §8: §fSauvegarder l'inventaire.");
                player.sendMessage(" §8| §4/§cenchant §8: §fEnchanter l'objet dans votre main.");
                player.sendMessage("");
                player.sendMessage(CommonString.BAR.getMessage());
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.closeInventory();
                InventoryAPI.giveDeathInvent(player);
                break;
            case 53:
                API.getAPI().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 6;
    }
}

