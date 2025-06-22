package fr.clickdroit.api.commands;

import com.google.common.base.Joiner;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.UHCInfos;
import fr.clickdroit.api.common.player.PlayerUtils;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.CommonString;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HostCommand implements CommandExecutor {
    private final GameManager gameManager;

    public HostCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.gameManager.hasHostAccess(player)) {
                StringBuilder stringBuilder;
                List<String> hosts;
                String msg;
                String name;
                String target;
                GamePlayer gamePlayer;
                if (arguments.length == 0) {
                    sendHelp(player);
                    return true;
                }
                switch (arguments[0]) {
                    case "help":
                        sendHelp(player);
                        break;
                    case "say":
                        if (arguments.length == 1) {
                            player.sendMessage("say <message> un message d'annonce.");
                            break;
                        }
                        stringBuilder = new StringBuilder();
                        for (String str : arguments)
                            stringBuilder.append(str).append(" ");
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("" + player.getName() + " " + stringBuilder.toString().replaceFirst("say", ""));
                        Bukkit.broadcastMessage("");
                        break;
                    case "name":
                        if (arguments.length == 1) {
                            player.sendMessage("name <message> le nom de l'host.");
                            break;
                        }
                        stringBuilder = new StringBuilder();
                        for (String str : arguments)
                            stringBuilder.append(str).append(" ");
                        name = stringBuilder.toString().replace("name", "").replace("&", "").substring(1);
                        if (name.length() <= 32) {
                            player.sendMessage("venez de changer le nom de votre serveur en" + name);
                            break;
                        }
                        player.sendMessage("ne pouvez pas changer le nom de l'host, si vous mettez plus de 32 caract");
                        break;
                    case "chat":
                        this.gameManager.getGameConfig().setChat(!this.gameManager.getGameConfig().isChat());
                        player.sendMessage("chat est d" + (this.gameManager.getGameConfig().isChat() ? "": "") + "actif");
                        break;
                    case "give":
                        if (GameUtils.isGameStarted()) {
                            if (arguments.length == 3) {
                                Material itemType = Material.BEDROCK;
                                switch (arguments[1]) {
                                    case "log":
                                        itemType = Material.LOG;
                                        break;
                                    case "anvil":
                                        itemType = Material.ANVIL;
                                        break;
                                    case "xp":
                                        itemType = Material.EXP_BOTTLE;
                                        break;
                                    case "cobblestone":
                                        itemType = Material.COBBLESTONE;
                                        break;
                                    case "apple":
                                        itemType = Material.APPLE;
                                        break;
                                    case "arrow":
                                        itemType = Material.ARROW;
                                        break;
                                    case "book":
                                        itemType = Material.BOOK;
                                        break;
                                    case "cooked_beef":
                                        itemType = Material.COOKED_BEEF;
                                        break;
                                }
                                if (itemType.equals(Material.BEDROCK)) {
                                    player.sendMessage("invalide.");
                                    return false;
                                }
                                int quantity = 1;
                                try {
                                    quantity = Integer.parseInt(arguments[2]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage("invalide.");
                                }
                                ItemStack giveItem = new ItemStack(itemType, quantity);
                                for (UUID uuid : this.gameManager.getInGamePlayers()) {
                                    Player players = Bukkit.getPlayer(uuid);
                                    if (players == null)
                                        continue;
                                    players.getInventory().addItem(new ItemStack[] { giveItem });
                                }
                                player.sendMessage("donntous les joueurs.");
                                break;
                            }
                            player.sendMessage("give un ou plusieurs objets tous les joueurs de la partie.");
                            break;
                        }
                        player.sendMessage("partie n'a pas commenc");
                        break;
                    case "heal":
                        if (GameUtils.isGameStarted()) {
                            if (arguments.length == 1) {
                                player.sendMessage("heal tous les joueurs ou un joueur cibl");
                                break;
                            }
                            if (arguments[1].equalsIgnoreCase("all")) {
                                Bukkit.getOnlinePlayers().forEach(players -> {
                                    players.setHealth(players.getMaxHealth());
                                    players.setFoodLevel(20);
                                });
                                player.sendMessage("avez soigntous les joueurs.");
                                break;
                            }
                            Player player1 = Bukkit.getPlayer(arguments[1]);
                            if (player1 != null) {
                                player1.setHealth(player1.getMaxHealth());
                                player1.setFoodLevel(20);
                                player.sendMessage("avez soign+ player1.getName() + ");
                                break;
                            }
                            player.sendMessage("joueur avec le pseudo '" + arguments[1] + "' n'a trouv");
                            break;
                        }
                        player.sendMessage("partie n'a pas commenc");
                        break;
                    case "config":
                        if (player.isOp() || (this.gameManager.getGameHost() != null && this.gameManager.getGameHost().equals(player.getUniqueId())))
                            this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
                        break;
                    case "set":
                        if (GameUtils.isGameStarted()) {
                            player.sendMessage("partie a dcommencaction impossible.");
                            return false;
                        }
                        if (player.isOp()) {
                            if (arguments.length == 1) {
                                player.sendMessage("set l'host la partie.");
                                break;
                            }
                            Player player1 = Bukkit.getPlayer(arguments[1]);
                            if (player1 != null) {
                                Player oldHost = Bukkit.getPlayer(this.gameManager.getGameHost());
                                if (oldHost != null) {
                                    oldHost.closeInventory();
                                    oldHost.getInventory().remove(Material.REDSTONE_COMPARATOR);
                                    GamePlayer.getPlayer(oldHost.getUniqueId()).setEditing(false);
                                    oldHost.setGameMode(GameMode.ADVENTURE);
                                    PlayerUtils.giveDefaultItems(oldHost);
                                }
                                this.gameManager.setGameHost(player1.getUniqueId());
                                PlayerUtils.giveDefaultItems(player1);
                                UHCInfos.hostName = player1.getName();
                                player.sendMessage("nouvel la partie est d+ player1.getName() + ");
                                player1.sendMessage("le nouvel la partie !");
                                break;
                            }
                            player.sendMessage("joueur avec le pseudo '" + arguments[1] + "' n'a trouv");
                            break;
                        }
                        sendHelp(player);
                        break;
                    case "add":
                        if (GameUtils.isGameStarted()) {
                            player.sendMessage("partie a dcommencaction impossible.");
                            return false;
                        }
                        if (this.gameManager.getGameHost().equals(player.getUniqueId()) || player.isOp()) {
                            if (arguments.length == 1) {
                                player.sendMessage("add un co-host.");
                                break;
                            }
                            Player player1 = Bukkit.getPlayer(arguments[1]);
                            if (player1 != null) {
                                if (player.getUniqueId().equals(player1.getUniqueId())) {
                                    player.sendMessage("ne pouvez pas vous ajouter aux hosts vous-m");
                                    return true;
                                }
                                if (this.gameManager.getHosts().contains(player1.getUniqueId())) {
                                    player.sendMessage("joueur est dco-host.");
                                    return true;
                                }
                                this.gameManager.getHosts().add(player1.getUniqueId());
                                PlayerUtils.giveHostItems(player1);
                                player.sendMessage("avez ajout"+ player1.getName() + "en tant que co-host.");
                                player1.sendMessage("faites partie des co-hosts.");
                                break;
                            }
                            player.sendMessage("joueur avec le pseudo '" + arguments[1] + "' n'a trouv");
                            break;
                        }
                        sendHelp(player);
                        break;
                    case "remove":
                        if (GameUtils.isGameStarted()) {
                            player.sendMessage("partie a dcommencaction impossible.");
                            return false;
                        }
                        if (this.gameManager.getGameHost().equals(player.getUniqueId()) || player.isOp()) {
                            if (arguments.length == 1) {
                                player.sendMessage("remove Retirer un co-host.");
                                break;
                            }
                            Player player1 = Bukkit.getPlayer(arguments[1]);
                            if (player1 != null) {
                                if (player.getUniqueId().equals(player1.getUniqueId())) {
                                    player.sendMessage("ne pouvez pas vous ajouter aux co-hosts");
                                    return true;
                                }
                                if (!this.gameManager.getHosts().contains(player1.getUniqueId())) {
                                    player.sendMessage("joueur n'est pas co-host.");
                                    return true;
                                }
                                this.gameManager.getHosts().remove(player1.getUniqueId());
                                player1.getInventory().remove(Material.REDSTONE_COMPARATOR);
                                player.sendMessage("avez retir"+ player1.getName() + "des co-hosts.");
                                player1.sendMessage("n'plus co-host.");
                                break;
                            }
                            player.sendMessage("joueur avec le pseudo '" + arguments[1] + "' n'a trouv");
                            break;
                        }
                        sendHelp(player);
                        break;
                    case "liste":
                    case "list":
                        if (GameUtils.isGameStarted()) {
                            player.sendMessage("partie a dcommencaction impossible.");
                            return false;
                        }
                        if (this.gameManager.getHosts().size() == 0) {
                            player.sendMessage("n'y a pas de co-host.");
                            return true;
                        }
                        hosts = new ArrayList<>();
                        msg = "  des co-hosts ";
                        for (UUID uuid : this.gameManager.getHosts())
                            hosts.add(Bukkit.getPlayer(uuid).getName());
                        player.sendMessage(CommonString.BAR.getMessage());
                        if (hosts.size() == 1) {
                            player.sendMessage("  des co-hosts "+ (String)hosts.get(0));
                        } else {
                            player.sendMessage("  des co-hosts " + Joiner.on("").join(hosts.subList(0, hosts.size() - 1)).concat("" ).concat(hosts.get(hosts.size() - 1)));
                        }
                        player.sendMessage(CommonString.BAR.getMessage());
                        break;
                    case "force":
                        if (GameUtils.isGameStarted()) {
                            if (arguments.length == 2) {
                                if (arguments[1].equalsIgnoreCase("pvp")) {
                                    if (Rules.pvp.isActive()) {
                                        player.sendMessage("Le PvP est dactiv");
                                        break;
                                    }
                                    Rules.pvp.setActive(true);
                                    break;
                                }
                                if (arguments[1].equalsIgnoreCase("bordure") || arguments[1].equalsIgnoreCase("border")) {
                                    if (this.gameManager.getBorder().isStart()) {
                                        player.sendMessage("La bordure est dactiv");
                                        break;
                                    }
                                    this.gameManager.getBorder().startReduce((this.gameManager.getGameConfig().getBorderEndSize() * 2), this.gameManager.getGameConfig().getBorderBlocksPerSecond());
                                }
                                break;
                            }
                            player.sendMessage("force permet d'activer le PvP ou la bordure de force.");
                            break;
                        }
                        player.sendMessage("partie n'a pas commenc");
                        break;
                    case "killoffline":
                        if (arguments.length == 1) {
                            if (this.gameManager.getOfflinePlayers().size() == 0) {
                                player.sendMessage("Il n'y a aucun joueur d");
                                return false;
                            }
                            for (UUID uuid : this.gameManager.getOfflinePlayers())
                                this.gameManager.getApi().getModules().onPlayerDieByDisconnect(uuid);
                            this.gameManager.getOfflinePlayers().clear();
                            player.sendMessage("les joueurs dont !");
                            break;
                        }
                        target = arguments[1];
                        gamePlayer = GamePlayer.getPlayer(target);
                        if (gamePlayer != null) {
                            if (this.gameManager.getOfflinePlayers().contains(gamePlayer.getUuid())) {
                                this.gameManager.getApi().getModules().onPlayerDieByDisconnect(gamePlayer.getUuid());
                                player.sendMessage("+ target + " );
                                this.gameManager.getOfflinePlayers().remove(gamePlayer.getUuid());
                                break;
                            }
                            player.sendMessage("joueur n'est pas d");
                            break;
                        }
                        player.sendMessage("joueur n'est pas dou n'existe pas.");
                        break;
                    case "revive":
                        if (arguments.length == 2) {
                            Player player1 = Bukkit.getPlayer(arguments[1]);
                            if (player1 != null) {
                                if (this.gameManager.getGameConfig().getRoleTime() != 0 && this.gameManager.getGlobalTask().getGlobalTime() < this.gameManager.getGameConfig().getRoleTime()) {
                                    Bukkit.dispatchCommand((CommandSender)player, "revive " + player1.getName());
                                    player.sendMessage(""+ player1.getName() + " a ressuscit!");
                                    break;
                                }
                                player.sendMessage("ne pouvez pas ressusciter un joueur aprl'annonce des r");
                            }
                            break;
                        }
                        player.sendMessage("revive le joueur cibl");
                        break;
                    case "kick":
                        if (this.gameManager.getGameHost().equals(player.getUniqueId()) || player.isOp()) {
                            if (arguments.length == 1) {
                                player.sendMessage("kick le joueur cibl");
                                break;
                            }
                            Player player1 = Bukkit.getPlayer(arguments[1]);
                            if (player1 == null) {
                                player.sendMessage("joueur avec le pseudo '" + arguments[1] + "' n'a trouv");
                                return true;
                            }
                            if (player.getUniqueId().equals(player1.getUniqueId())) {
                                player.sendMessage("ne pouvez pas le faire sur vous-m");
                                return true;
                            }
                            if (isStaff(player1.getName())) {
                                player.sendMessage("ne pouvez pas kick un staff.");
                                return true;
                            }
                            player.sendMessage("avez expuls"+ player1.getName() + "de la partie.");
                            player1.kickPlayer("explusde la partie !");
                            break;
                        }
                        sendHelp(player);
                        break;
                }
            }
        }
        return false;
    }

    private void sendHelp(Player player) {
        player.sendMessage("give un ou plusieurs objets tous les joueurs de la partie.");
        player.sendMessage("heal tous les joueurs ou un joueur cibl");
        player.sendMessage("chat le chat de la partie.");
        player.sendMessage("force permet d'activer le PvP ou la bordure de force.");
        player.sendMessage("killoffline tous les joueurs dou le joueur cibl");
        player.sendMessage("revive le joueur cibl");
        player.sendMessage("des co-hosts.");
        player.sendMessage("say un message d'annonce.");
        player.sendMessage("name le nom de l'host.");
        player.sendMessage("kick le joueur cibl");
        if (player.isOp())
            player.sendMessage("set l'host de la partie.");
    }

    private boolean isStaff(String username) {
        Player player = Bukkit.getPlayer(username);
        if (player == null)
            return false;
        return player.hasPermission("uhc.staff");
    }
}
