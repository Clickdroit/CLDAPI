package fr.clickdroit.api.config.intValue;


import fr.clickdroit.api.API;
import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.time.Instant;
import java.util.Date;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DateGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public DateGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = this.gameManager.getGameConfig();
    }

    public String getName() {
        return "Heure d'ouverture";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        Date date = this.gameConfig.getOpenDate();
        slots[0] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(14)).getItem();
                slots[1] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(11)).getItem();
                        slots[4] = (new ItemCreator(Material.WATCH)).setName("de lancement")
                                .addLore("permet de dl'heure")
                                .addLore("de la partie.")
                                .addLore("")
                                .addLore("d'ouverture:" + date.getHours() + ":" + date.getMinutes())
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(12)).getItem();
                slots[8] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(10)).getItem();
                        slots[9] = (new ItemCreator(Material.SLIME_BALL)).setName("l'heure").getItem();
        slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        Date date = this.gameConfig.getOpenDate();
        switch (slot) {
            case 0:
                date.setMinutes(date.getMinutes() - 30);
                break;
            case 1:
                date.setMinutes(date.getMinutes() - 10);
                break;
            case 7:
                date.setMinutes(date.getMinutes() + 10);
                break;
            case 8:
                date.setMinutes(date.getMinutes() + 30);
                break;
            case 9:
                this.gameConfig.setServerHourSetup(true);
                API.getAPI().openInventory(player, ConfigMainGUI.class);
                break;
        }
        Date currentDate = Date.from(Instant.now());
        System.out.println("currentDate : " + currentDate.getHours() + ":" + currentDate.getMinutes());
        System.out.println("date : " + date.getHours() + ":" + date.getMinutes());
        if (date.getHours() < currentDate.getHours())
            date.setHours(currentDate.getHours());
        if (date.getMinutes() < currentDate.getMinutes())
            date.setMinutes(currentDate.getMinutes());
        if (slot == 13) {
            API.getAPI().openInventory(player, ConfigMainGUI.class);
        } else {
            API.getAPI().openInventory(player, getClass());
        }
    }

    public int getRows() {
        return 2;
    }
}
