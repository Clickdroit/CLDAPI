package fr.clickdroit.api.config;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.borderValue.BorderManagerGUI;
import fr.clickdroit.api.config.common.GameAccess;
import fr.clickdroit.api.config.intValue.SlotsGUI;
import fr.clickdroit.api.config.teamvalue.TeamManagerGUI;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.module.GameModule;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
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
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9),
                Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(35),
                Integer.valueOf(36), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46),
                Integer.valueOf(52), Integer.valueOf(53) }, glass = arrayOfInteger1;
        int i = arrayOfInteger1.length;
        byte b;
        for (b = 0; b < i; b = (byte) (b + 1)) {
            int j = arrayOfInteger1[b].intValue();
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(11)).setName("§f")
                    .getItem();
        }
        // REMPLACEMENT DU SAPLING PAR UNE TÊTE PERSONNALISÉE
        slots[6] = (new ItemCreator(Material.SKULL_ITEM))
                .setDurability(3) // Tête de joueur
                .setSkullURL(
                        "http://textures.minecraft.net/texture/cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1")
                .setName("§8| §fPré-charger")
                .addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost")
                .addLore("")
                .addLore("  §8| §fPermet de pré-charger")
                .addLore("  §8| §ftoute la §2map§f.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();

        GameConfig.WaitingTeleportationState waitingState = this.gameConfig.getTeleportationState();
        slots[22] = (new ItemCreator(
                waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? Material.EYE_OF_ENDER
                        : Material.ENDER_PEARL))
                .setName(waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM)
                        ? "§8| §fTéléportation au §alobby"
                        : "§8| §fTéléportation à la §asalle des règles")
                .addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("")
                .addLore("  §8| §fPermet de teleporter les §ajoueurs")
                .addLore("  §8| §f"
                        + (waitingState.equals(GameConfig.WaitingTeleportationState.IN_ROOM) ? "au point d'apparition"
                                : "dans la salle des r")
                        + "§f")
                .addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[29] = (new ItemCreator(Material.SKULL_ITEM))
                .setDurability(3) // Tête de joueur
                .setSkullURL(
                        "http://textures.minecraft.net/texture/f0b6986d344be4485434c6501d9d6f815af412b265872263e8cb38fe61ca")
                .setName("§8| §fSlots")
                .addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost")
                .addLore(" §8> §fNombres de slots: §c§l" + this.gameConfig.getGameSlot())
                .addLore("")
                .addLore("  §8| §fVous permet de §cmodifier")
                .addLore("  §8| §fle nombre de §cjoueurs§f autorisés")
                .addLore("  §8| §fà se §aconnecter§f à la §cpartie§f.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();

        if (this.gameManager.getModuleManager().getCurrentModule().hasTeam())
            slots[32] = (new ItemCreator(Material.BANNER)).setName("§8| §fGestion des §céquipes")
                    .setDurability(Integer.valueOf(15)).addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("")
                    .addLore("  §8| §fPermet de gérer les").addLore("  §8| §coptions §fdes équipes").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[47] = (new ItemCreator(Material.BARRIER)).setName("§8| §fStopper le serveur")
                .setDurability(Integer.valueOf(15)).addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("")
                .addLore("  §8| §fPermet de stopper").addLore("  §8| §fle §cserveur").addLore("").addLore("")
                .addLore(" ").addLore("").addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("")
                .getItem();
        slots[31] = (new ItemCreator(Material.EYE_OF_ENDER)).setName("§8| §fSpectateurs").addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost")
                .addLore(" §8> §fStatut §f: " + (this.gameConfig.isSpectators() ? "§aActivé" : "§cDésactivé"))
                .addLore("").addLore("  §8| §fPermet d'§aaccepter§f ou §cnon§f la présence")
                .addLore("  §8| §fdes spectateurs dans la §cpartie§f ").addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        slots[24] = (new ItemCreator(Material.NETHERRACK)).setName("§8| §fNether").addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost")
                .addLore(" §8> §fStatut §f: " + (this.gameConfig.isNether() ? "§aActivé" : "§cDésactivé")).addLore("")
                .addLore("  §ç| §fPermet d'§aaccepter§f ou §cnon§f")
                .addLore("  §8| §fdes joueurs à aller dans le §cnether§f").addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();
        slots[23] = (new ItemCreator(Material.ITEM_FRAME)).setName("§8| §fOptions de la §cpartie").addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'accéder aux")
                .addLore("  §8| §coptions§f/§crègles§f de la partie").addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage()).addLore("").getItem();

        // Bouton de sélection de mode de jeu (toujours visible)
        GameModule activeModule = this.api.getActiveGameModule();
        String currentModeName = activeModule != null ? activeModule.getColor() + activeModule.getDisplayName()
                : this.gameManager.getModuleManager().getCurrentModule().getColor() +
                        this.gameManager.getModuleManager().getCurrentModule().getName();
        int moduleCount = this.api.getModuleRegistry().getModuleCount() + ModuleType.values().length;

        slots[4] = (new ItemCreator(Material.NETHER_STAR)).setName("§8| §fSélection du §cmode de jeu").addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost")
                .addLore(" §8> §fMode actuel §f: " + currentModeName)
                .addLore(" §8> §fModes disponibles §f: §e" + moduleCount)
                .addLore("").addLore("  §8| §fPermet de choisir le mode")
                .addLore("  §8| §fde jeu pour la §cpartie§f.").addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();

        // Bouton de configuration du mode actif (si ce n'est pas UHC standard)
        if (activeModule != null || !this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.UHC))
            slots[3] = (new ItemCreator(Material.PRISMARINE_SHARD)).setName("§8| §fConfig §cmode actif").addLore("")
                    .addLore(" §8> §fAccès §f: §6§lHost")
                    .addLore(" §8> §fMode §f: " + currentModeName)
                    .addLore("").addLore("  §8| §fPermet de modifier les options")
                    .addLore("  §8| §cliées§f au mode de jeu §aactif§f").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[33] = (new ItemCreator(Material.STAINED_GLASS)).setDurability(Integer.valueOf(9))
                .setName("§8| §fGestion de la §cbordure").addLore("").addLore(" §8> §fAccès §f: §6§lHost").addLore("")
                .addLore("  §8| §fPermet de modifier la §ataille").addLore("  §8| §fet la §bvitesse de la §cbordure§f.")
                .addLore("").addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        slots[51] = (new ItemCreator(Material.BOOK)).setName("§8| §fGestion des §cscénarios").addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'§aajouter§f des scénarios")
                .addLore("  §8| §fquidynamiseront la §cpartie§f.").addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        GameAccess currentAccess = this.gameConfig.getGameAccess();
        ItemCreator accessItem = new ItemCreator(Material.WATCH)
                .setName("§8| §fAccessibilité de la §cpartie")
                .addLore("")
                .addLore(" §8> §fAccès §f: §6§lHost")
                .addLore(" §8> §fStatut §f: " + currentAccess.getMessage())
                .addLore("");

        if (currentAccess == GameAccess.OPEN) {
            accessItem.addLore("  §8| §fLa partie est §aouverte§f à tous")
                    .addLore("  §8| §fles joueurs. Ils peuvent rejoindre")
                    .addLore("  §8| §flibrementle serveur.")
                    .addLore("")
                    .addLore("  §8| §e§lClic §8» §cFermer la partie");
        } else {
            accessItem.addLore("  §8| §fLa partie est §cfermée§f. Seuls")
                    .addLore("  §8| §fles §6administrateurs§f, §6hosts§f et")
                    .addLore("  §8| §fjoueurs §awhitelistés§f peuvent rejoindre.")
                    .addLore("")
                    .addLore("  §8| §e§lClic §8» §aOuvrir la partie");
        }

        accessItem.addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("");

        slots[20] = accessItem.getItem();

        if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.DEMONSLAYER)) {
            slots[2] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lDEMONSLAYER§f)").addLore("")
                    .addLore(" ").addLore("").addLore("  d'acc").addLore(" ").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.UHC)) {
            slots[2] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lUHC§f)").addLore("")
                    .addLore(" §8> §fAccès §f: §6§lHost").addLore("").addLore("  §8| §fPermet d'accéder à")
                    .addLore(" §8| §fvos §cconfigurations§f.").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.LG)) {
            slots[2] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lLG§f)").addLore("")
                    .addLore(" ").addLore("").addLore("  d'acc").addLore(" ").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
            slots[2] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lNARUTO§f)").addLore("")
                    .addLore(" ").addLore("").addLore("  d'acc").addLore(" ").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage()).addLore("").getItem();
        } else {
            slots[2] = (new ItemCreator(Material.PAPER)).setName("§8| §fPré-Config §f(§c§lUHC§f)").addLore("")
                    .addLore(" dans ce mode.").addLore("").getItem();
        }
        if (this.api.getGameManager().getGameState().equals(GameState.WAITING)) {
            slots[49] = (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(10))
                    .setName("§8| §fLancement de la §cpartie ").addLore("").addLore(" §8> §fTout est §aprêt§f ?")
                    .addLore(" §8> §fAccès §f: §6§lHost").addLore("")
                    .addLore("  §8| §fPermet de lancer la §cpartie§f si")
                    .addLore("  §8| §fvous avez fini la config de la §cpartie§f.").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        } else if (this.api.getGameManager().getGameState().equals(GameState.STARTING)) {
            slots[49] = (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(8))
                    .setName("§8| §cArrêter§f le lancement§f.").addLore("").addLore(" §8> §fPas sûr ? §cArrête !")
                    .addLore(" §8> §fAccès §f: §6§lHost").addLore("")
                    .addLore("  §8| §fPermet d'arrêter la §cpartie§f si")
                    .addLore("  §8| §fvous avez fini la config de la §cpartie§f.").addLore("")
                    .addLore(CommonString.CLICK_HERE_TO_ACTIVATE.getMessage()).addLore("").getItem();
        }
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        if (slot == 2 && clickedItem.getType() == Material.PAPER) {
            this.api.getModules().openConfig(player);
            return;
        }

        // Bouton de sélection de mode de jeu
        if (slot == 4 && clickedItem.getType() == Material.NETHER_STAR) {
            this.api.openInventory(player, GameModeSelectionGUI.class);
            return;
        }

        // Bouton de configuration du mode actif
        if (slot == 3 && clickedItem.getType() == Material.PRISMARINE_SHARD) {
            // Vérifier si un module externe est actif
            fr.clickdroit.api.module.GameModule activeModule = this.api.getActiveGameModule();
            if (activeModule != null) {
                activeModule.openConfig(player);
            } else {
                this.api.getModules().openConfig(player);
            }
            return;
        }

        if (slot == 22) {
            if (this.gameManager.getGameState().equals(GameState.WAITING))
                if (this.gameConfig.getTeleportationState().equals(GameConfig.WaitingTeleportationState.IN_ROOM)) {
                    this.gameConfig.setTeleportationState(GameConfig.WaitingTeleportationState.IN_LOBBY);
                    player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                } else {
                    this.gameConfig.setTeleportationState(GameConfig.WaitingTeleportationState.IN_ROOM);
                }
            return;
        }

        if (slot == 29 && clickedItem.getType() == Material.COMPASS) {
            // Clic sur l'item Slots (boussole au slot 10)
            this.api.openInventory(player, SlotsGUI.class);
            return;
        }

        switch (clickedItem.getType()) {
            case SKULL_ITEM:
                // Vérifier si c'est la tête de pré-chargement (slot 4)
                if (slot == 6) {
                    player.closeInventory();
                    if (!this.gameManager.isPreloadFinished() && !this.gameManager.isPreload()) {
                        this.gameManager.setPreload(true);
                        BiomeChanger.addSapling();
                        player.sendMessage("§fVous venez de §alancer §fla prégénération de la map.");

                        Bukkit.getScheduler().runTaskLater(this.api, () -> {
                            if (this.gameManager.isPreloadFinished()) {
                                player.sendMessage("§aLa prégénération de la map est terminée !");
                            }
                        }, 20L * 5);

                        break;
                    }
                    if (this.gameManager.isPreloadFinished()) {
                        player.sendMessage("§aLa map est déjà pré-chargée !");
                    } else {
                        player.sendMessage("§fLe serveur est §cchargé§f ou est §centrain§f de pré-charger...");
                    }
                    break;
                }
                // Garder le code existant pour les autres têtes
                if (!this.gameManager.getModuleManager().getCurrentModule().isHasRole())
                    this.api.openInventory(player, SlotsGUI.class);
                break;

            case RED_ROSE:
                if (GamePlayer.getPlayer(player.getUniqueId()).isOp()) {
                    this.api.openInventory(player, AdminPanelGUI.class);
                    break;
                }
                player.sendMessage("§fVous n'êtes §cpas autorisé§f à faire ceci.");
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
                this.api.getCommon().getScenariosGUI().openInventory(player);
                break;
            case WATCH:
                GameAccess currentAccess = this.gameConfig.getGameAccess();
                String playerCount = String.valueOf(Bukkit.getOnlinePlayers().size());

                if (currentAccess == GameAccess.OPEN) {
                    this.gameConfig.setGameAccess(GameAccess.CLOSE);
                    player.sendMessage("");
                    player.sendMessage("§c§l✗ Partie fermée !");
                    player.sendMessage("§fSeuls les administrateurs, hosts et joueurs");
                    player.sendMessage("§fwhitelistés peuvent maintenant rejoindre.");
                    player.sendMessage("§f(" + playerCount + " joueurs actuellement connectés)");
                    player.sendMessage("");
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.5F, 1.0F);
                } else {
                    this.gameConfig.setGameAccess(GameAccess.OPEN);
                    player.sendMessage("");
                    player.sendMessage("§a§l✓ Partie ouverte !");
                    player.sendMessage("§fTous les joueurs peuvent maintenant rejoindre");
                    player.sendMessage("§fle serveur librement.");
                    player.sendMessage("§f(" + playerCount + " joueurs actuellement connectés)");
                    player.sendMessage("");
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                }

                // Rafraîchir l'inventaire pour montrer le nouveau statut
                this.api.openInventory(player, getClass());
                break;
        }

    }

    public int getRows() {
        return 6;
    }
}
