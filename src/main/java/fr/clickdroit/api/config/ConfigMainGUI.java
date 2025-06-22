package fr.clickdroit.api.config;


import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.borderValue.BorderManagerGUI;
import fr.clickdroit.api.config.intValue.SlotsGUI;
import fr.clickdroit.api.config.teamValue.TeamManagerGUI;
import fr.clickdroit.api.config.value.OpenVar;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import fr.clickdroit.api.worlds.BiomeChanger;
import fr.clickdroit.api.utils.Title;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfigMainGUI implements CustomInventory {
    private final API api;

    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public ConfigMainGUI(API api) {
        this.api = api;
        this.gameManager = api.getGameManager();
        this.gameConfig = this.gameManager.getGameConfig();
    }

    public String getName() {
        return ";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        Integer[] arrayOfInteger1 = {
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(36), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46),
                Integer.valueOf(52), Integer.valueOf(53) }, glass = arrayOfInteger1;
        int i = arrayOfInteger1.length;
        byte b;
        for (b = 0; b < i; b = (byte)(b + 1)) {
            int j = arrayOfInteger1[b].intValue();
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(14)).setName("").getItem();
        }
        slots[2] = (new ItemCreator(Material.RED_ROSE)).setName("d').addLore("").addLore(" ).addLore("").addLore("  d'acceder au").addLore("  d').addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
                slots[4] = (new ItemCreator(Material.SAPLING)).setName(").addLore("").addLore(" ).addLore("").addLore("  de pr).addLore("  la ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        GameConfig.WaitingTeleportationState waitingState = this.gameConfig.getTeleportationState();
        slots[6] = (new ItemCreator(waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? Material.EYE_OF_ENDER : Material.ENDER_PEARL)).setName(waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? "au : "la des r).addLore("").addLore(" ).addLore("").addLore("  de teleporter les ).addLore("  + (waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? "au point d'apparition" : "dans la salle des r) + ").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[10] = OpenVar.SLOTS.getItem();
        if (this.gameManager.getModuleManager().getCurrentModule().hasTeam())
            slots[25] = (new ItemCreator(Material.BANNER)).setName("des ").setDurability(Integer.valueOf(15)).addLore("").addLore(" ").addLore("").addLore("  de gles").addLore("  ").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
                    slots[11] = (new ItemCreator(Material.BARRIER)).setName("le serveur").setDurability(Integer.valueOf(15)).addLore("").addLore("" ).addLore("").addLore("  de stopper").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        slots[15] = (new ItemCreator(Material.EYE_OF_ENDER)).setName("").addLore("").addLore(" ").addLore(" " + (this.gameConfig.isSpectators() ? ": ")).addLore("").addLore("  d'ou la pr").addLore("  spectateurs dans la ").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        slots[16] = (new ItemCreator(Material.NETHERRACK)).setName("").addLore("").addLore(" ").addLore(" " + (this.gameConfig.isNether() ? ": ")).addLore("").addLore("  d'ou ").addLore("  joueurs aller dans le ").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        slots[22] = (new ItemCreator(Material.ITEM_FRAME)).setName("de la ").addLore("").addLore(" ").addLore("").addLore("  d'accaux").addLore("  de la partie").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        if (!this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.UHC))
            slots[31] = (new ItemCreator(Material.PRISMARINE_SHARD)).setName("de ").addLore("").addLore(" ).addLore(" + this.gameManager.getModuleManager().getCurrentModule().getName()).addLore("").addLore("  de modifer les options").addLore("  au mode de jeu" ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[37] = (new ItemCreator(Material.STAINED_GLASS)).setDurability(Integer.valueOf(9)).setName("de la ).addLore("").addLore(""" ).addLore("").addLore("  de modifer la ").addLore("  la de la ").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[43] = (new ItemCreator(Material.BOOK)).setName("des ").addLore("").addLore("" ).addLore("").addLore("  d'des sc").addLore("  dynamiseront la"" ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[47] = (new ItemCreator(Material.WATCH)).setName("de la ").addLore("").addLore(" ").addLore(" " + this.gameConfig.getGameAccess().getMessage()).addLore("").addLore("  de l'accessibilit").addLore("  la partie pour les ").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.DEMONSLAYER)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.UHC)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.LG)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.NARUTO)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("").addLore("").addLore(" dans ce mode.").addLore("").getItem();
        }
        if (this.api.getGameManager().getGameState().equals(GameState.WAITING)) {
            slots[49] = (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(10)).setName("la ").addLore("").addLore(" est ?").addLore(" ").addLore("").addLore("  de lancer la si").addLore("  avez fini la config de la ").addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        } else if (this.api.getGameManager().getGameState().equals(GameState.STARTING)) {
            slots[49] = (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(8)).setName("le lancement").addLore("").addLore(" s? !").addLore(" ").addLore("").addLore("  d'arrla si").addLore("  avez mal fait la config de la ").addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        }
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        if (slot == 6) {
            if (this.gameManager.getGameState().equals(GameState.WAITING))
                if (this.gameConfig.getTeleportationState().equals(GameConfig.WaitingTeleportationState.IN_ROOM)) {
                    this.gameConfig.setTeleportationState(GameConfig.WaitingTeleportationState.IN_LOBBY);
                } else {
                    this.gameConfig.setTeleportationState(GameConfig.WaitingTeleportationState.IN_ROOM);
                }
            return;
        }
        switch (clickedItem.getType()) {
            case SAPLING:
                player.closeInventory();
                if (!this.gameManager.isPreloadFinished() && !this.gameManager.isPreload()) {
                    this.gameManager.setPreload(true);
                    BiomeChanger.addSapling();
                    player.sendMessage("venez de prde la map.");
                    break;
                }
                player.sendMessage("serveur est dou est );
                break;
            case RED_ROSE:
                if (GamePlayer.getPlayer(player.getUniqueId()).isHeadStaff()) {
                    this.api.openInventory(player, AdminPanelGUI.class);
                    break;
                }
                player.sendMessage("n'autorisfaire ceci.");
                break;
            case SKULL_ITEM:
                if (!this.gameManager.getModuleManager().getCurrentModule().isHasRole())
                    this.api.openInventory(player, SlotsGUI.class);
                break;
            case INK_SACK:
                if (clickedItem.getDurability() == 10) {
                    player.closeInventory();
                    this.api.getGameManager().startWithTimer();
                    break;
                }
                if (clickedItem.getDurability() == 8) {
                    this.gameManager.getStartGameCountDown().cancel();
                    this.api.getGameManager().setGameState(GameState.WAITING);
                    player.closeInventory();
                    Bukkit.getOnlinePlayers().forEach(players -> {
                        Title.sendTitle(players, 10, 40, 10, ", "annul:c");
                        players.setLevel(0);
                        players.setExp(0.0F);
                    });
                }
                break;
            case BANNER:
                this.api.openInventory(player, TeamManagerGUI.class);
                break;
            case EYE_OF_ENDER:
                this.gameConfig.setSpectators(!this.gameConfig.isSpectators());
                this.api.openInventory(player, getClass());
                break;
            case NETHERRACK:
                this.gameConfig.setNether(!this.gameConfig.isNether());
                this.api.openInventory(player, getClass());
                break;
            case STAINED_GLASS:
                this.api.openInventory(player, BorderManagerGUI.class);
                break;
            case ITEM_FRAME:
                this.api.openInventory(player, ConfigOptionsGUI.class);
                break;
            case BOOK:
                this.api.getCommon().getScenariosGUI().openInventory(player, 1);
            case WATCH:
                player.sendMessage("devez la map avant d'ouvrir la );
                        player.closeInventory();
                break;
        }
    }

    public int getRows() {
        return 6;
    }
}

