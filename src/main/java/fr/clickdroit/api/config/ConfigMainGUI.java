package fr.clickdroit.api.config;


import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.borderValue.BorderManagerGUI;
import fr.clickdroit.api.config.intValue.SlotsGUI;
import fr.clickdroit.api.config.teamvalue.TeamManagerGUI;
import fr.clickdroit.api.config.value.OpenVar;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.worlds.BiomeChanger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

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
        return "§f(§c!§f) §cConfiguration";
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
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(14)).setName("§f").getItem();
        }
        slots[2] = (new ItemCreator(Material.RED_ROSE)).setName("§8| §fPanel d'§cAdministration").addLore("").addLore(" §8> §fAccès §f: §c§lAdministration").addLore("").addLore("  §8| §fPermetd'acceder au").addLore("  §8| §fpanel d'§cadministration§f").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[4] = (new ItemCreator(Material.SAPLING)).setName("§8| §fPré-charger").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet de pré-charger").addLore("  §8| §ftoute la §2map§f.").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        GameConfig.WaitingTeleportationState waitingState = this.gameConfig.getTeleportationState();
        slots[6] = (new ItemCreator(waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? Material.EYE_OF_ENDER : Material.ENDER_PEARL)).setName(waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? "§8| §fTéléportation au §alobby": "§8| §fTéléportation à la §asalle des règles").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet de teleporter les §ajoueurs" ).addLore("  §8| §f" + (waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? "au point d'apparition" : "dans la salle des r") + "§f").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[10] = OpenVar.SLOTS.getItem();

        if (this.gameManager.getModuleManager().getCurrentModule().hasTeam())
            slots[25] = (new ItemCreator(Material.BANNER)).setName("§8| §fGestion des §céquipes").setDurability(Integer.valueOf(15)).addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet de gérer les").addLore("  §8| §coptions §fdes équipes").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[11] = (new ItemCreator(Material.BARRIER)).setName("§8| §fStopper le serveur").setDurability(Integer.valueOf(15)).addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet de stopper").addLore("  §8| §fle §cserveur").addLore("").addLore("").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        slots[15] = (new ItemCreator(Material.EYE_OF_ENDER)).setName("§8| §fSpectateurs").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore(" §8> §fStatut §f: " + (this.gameConfig.isSpectators() ? "§aActivé": "§cDésactivé")).addLore("").addLore("  §8| §fPermet d'§aaccepter§f ou §cnon§f la présence").addLore("  §8| §fdes spectateurs dans la §cpartie§f ").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        slots[16] = (new ItemCreator(Material.NETHERRACK)).setName("§8| §fNether").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore(" §8> §fStatut §f: " + (this.gameConfig.isNether() ? "§aActivé": "§cDésactivé")).addLore("").addLore("  §ç| §fPermet d'§aaccepter§f ou §cnon§f").addLore("  §8| §fdes joueurs à aller dans le §cnether§f").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        slots[22] = (new ItemCreator(Material.ITEM_FRAME)).setName("§8| §fOptions de la §cpartie").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'accéder aux").addLore("  §8| §coptions§f/§crègles§f de la partie").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();

        if (!this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.UHC))
            slots[31] = (new ItemCreator(Material.PRISMARINE_SHARD)).setName("§8| §fMode de §cjeu").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore(" §8> §fMode §f: §6§l"+ this.gameManager.getModuleManager().getCurrentModule().getName()).addLore("").addLore("  §8| §fPermet de modifier les options").addLore("  §8| §cliées§f au mode de jeu §aactif§f" ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[37] = (new ItemCreator(Material.STAINED_GLASS)).setDurability(Integer.valueOf(9)).setName("§8| §fGestion de la §cbordure").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet de modifier la §ataille").addLore("  §8| §fet la §bvitesse de la §cbordure§f.").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[43] = (new ItemCreator(Material.BOOK)).setName("§8| §fGestion des §cscénarios").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'§aajouter§f des scénarios").addLore("  §8| §fquidynamiseront la §cpartie§f." ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[47] = (new ItemCreator(Material.WATCH)).setName("§8| §fAccessibilité de la §cpartie").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore(" §8> §fStatut §f: " + this.gameConfig.getGameAccess().getMessage()).addLore("").addLore("  §8| §fPermet de §cmodifier§f l'accessibilité").addLore("  §8| §fà la partie pour les §cjoueurs§f.").addLore("").addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();

        if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.DEMONSLAYER)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lDEMONSLAYER§f)").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.UHC)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lUHC§f)").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'accéder à").addLore(" §8| §fvos §cconfigurations§f.").addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.LG)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lLG§f)").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.NARUTO)) {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lNARUTO§f)").addLore("").addLore(" ").addLore("").addLore("  d'acc").addLore(" " ).addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else {
            slots[51] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lUHC§f)").addLore("").addLore(" dans ce mode.").addLore("").getItem();
        }
        if (this.api.getGameManager().getGameState().equals(GameState.WAITING)) {
            slots[49] = (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(10)).setName("§8| §fLancement de la §cpartie ").addLore("").addLore(" §8> §fTout est §aprêt§f ?").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet de lancer la §cpartie§f si").addLore("  §8| §fvous avez fini la config de la §cpartie§f.").addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        } else if (this.api.getGameManager().getGameState().equals(GameState.STARTING)) {
            slots[49] = (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(8)).setName("§8| §cArrêter§f le lancement§f.").addLore("").addLore(" §8> §fPas sûr ? §cArrête !").addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'arrêter la §cpartie§f si").addLore("  §8| §fvous avez fini la config de la §cpartie§f.").addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        }
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        if (slot == 6) {
            if (this.gameManager.getGameState().equals(GameState.WAITING))
                if (this.gameConfig.getTeleportationState().equals(GameConfig.WaitingTeleportationState.IN_ROOM)) {
                    this.gameConfig.setTeleportationState(GameConfig.WaitingTeleportationState.IN_LOBBY);
                    player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
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
                    player.sendMessage("§fVous venez de §alancer §fla prégénération de la map.");
                    break;
                }
                player.sendMessage("§fLe serveur est §cchargé§f ou est §centrain§f..." );
                break;
            case RED_ROSE:
                if (GamePlayer.getPlayer(player.getUniqueId()).isOp()) {
                    this.api.openInventory(player, AdminPanelGUI.class);
                    break;
                }
                player.sendMessage("§fVous n'êtes §cpas autorisé§f à faire ceci.");
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
                        Title.sendTitle(players, 10, 40, 10, "§cUHC", "§cLancement annulé... :c");
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
                player.sendMessage("§fVous devez §cpré-charger§f la map avant d'ouvrir la §cpartie§f.");
                player.closeInventory();
                break;

        }
    }

    public int getRows() {
        return 6;
    }
}

