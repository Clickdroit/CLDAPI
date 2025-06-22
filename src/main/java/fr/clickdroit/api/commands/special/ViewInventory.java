package fr.clickdroit.api.commands.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.utils.Chrono;
import fr.clickdroit.api.utils.ItemCreator;
import fr.clickdroit.api.utils.TranslateEffect;
import fr.clickdroit.api.utils.gui.GUIView;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class ViewInventory extends GUIView {
    private final Player target;

    private final List<Integer> slotsCancel;

    public ViewInventory(Player player, Player target) {
        super(54, player, target);
        this.target = target;
        this.slotsCancel = new ArrayList<>();
        this.slotsCancel.add(Integer.valueOf(36));
        this.slotsCancel.add(Integer.valueOf(37));
        this.slotsCancel.add(Integer.valueOf(38));
        this.slotsCancel.add(Integer.valueOf(39));
        this.slotsCancel.add(Integer.valueOf(45));
        this.slotsCancel.add(Integer.valueOf(46));
        this.slotsCancel.add(Integer.valueOf(47));
        this.slotsCancel.add(Integer.valueOf(48));
        this.slotsCancel.add(Integer.valueOf(49));
        this.slotsCancel.add(Integer.valueOf(50));
        GamePlayer gamePlayer = GamePlayer.getPlayer(target.getUniqueId());
        for (int i = 0; i < 36; i++) {
            ItemStack item = target.getInventory().getContents()[i];
            setItem(i, item);
        }
        setItem(36, target.getInventory().getHelmet());
        setItem(37, target.getInventory().getChestplate());
        setItem(38, target.getInventory().getLeggings());
        setItem(39, target.getInventory().getBoots());
        setItem(45, (new ItemCreator(Material.SKULL_ITEM)).setDurability(Integer.valueOf(3)).setOwner(target.getName()).setName(""+ target.getName()).addLore(gamePlayer.isAlive() ? ": Vivant" : ": mort").getItem());
        setItem(46, (new ItemCreator(Material.APPLE)).setName(": + (int)target.getHealth() + " + (int)target.getMaxHealth()).setAmount(Integer.valueOf((int)target.getHealth())).getItem());
        setItem(47, (new ItemCreator(Material.COOKED_BEEF)).setName(":" + target.getFoodLevel()).setAmount(Integer.valueOf(target.getFoodLevel())).getItem());
                ItemCreator itemCreator = (new ItemCreator(Material.POTION)).setDurability(Integer.valueOf(8265)).setName("").setAmount(Integer.valueOf(target.getActivePotionEffects().size())).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (target.getActivePotionEffects().isEmpty()) {
            itemCreator.addLore("effet");
        } else {
            for (PotionEffect potionEffect : target.getActivePotionEffects())
                itemCreator.addLore(""+ TranslateEffect.translate(potionEffect.getType()) + " " + (potionEffect.getAmplifier() + 1) + " " + Chrono.timeToDigitalString((potionEffect.getDuration() / 20)));
        }
        setItem(48, itemCreator.getItem());
        setItem(49, (new ItemCreator(Material.DIAMOND)).setAmount(Integer.valueOf(gamePlayer.getDiamonds())).setName(""+ gamePlayer.getDiamonds() + "" ).getItem());
        setItem(50, (new ItemCreator(Material.GOLD_INGOT)).setAmount(Integer.valueOf(gamePlayer.getGolds())).setName(""+ gamePlayer.getGolds() + "" ).getItem());
        (new BukkitRunnable() {
            public void run() {
                if (ViewInventory.this.isClosed()) {
                    cancel();
                    return;
                }
            }
        }).runTaskTimer((Plugin)API.getAPI(), 10L, 50L);
    }

    public void guiInteractEvent(final int slot, ItemStack itemStack, InventoryClickEvent event) {
        if (slot >= 36)
            event.setCancelled(true);
        final Player target = Bukkit.getPlayer(getTarget());
        (new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < 36; i++) {
                    ItemStack item = ViewInventory.this.getItem(slot);
                    target.getInventory().setItem(slot, item);
                }
            }
        }).runTaskLater((Plugin)API.getAPI(), 10L);
    }

    public void guiUserInteractEvent(int slot, ItemStack itemStack, InventoryClickEvent event) {
        (new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < 36; i++) {
                    ItemStack item = ViewInventory.this.target.getInventory().getContents()[i];
                    ViewInventory.this.setItem(i, item);
                }
            }
        }).runTaskLater((Plugin)API.getAPI(), 10L);
    }
}
