package fr.clickdroit.api.commands;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.team.Teams;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class ReviveCommand implements CommandExecutor {
    private final API api;

    public ReviveCommand(API api) {
        this.api = api;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        if (arguments.length > 0) {
            if (GameUtils.isGameStarted()) {
                if (this.api.getGameManager().hasHostAccess(player)) {
                    Player target = Bukkit.getPlayer(arguments[0]);
                    if (target == null || !target.isOnline()) {
                        player.sendMessage("joueur avec le pseudo '" + arguments[0] + "' n'a trouv");
                        return false;
                    }
                    final GamePlayer gameTarget = GamePlayer.getPlayer(target.getUniqueId());
                    if (gameTarget.isAlive()) {
                        player.sendMessage("joueur n'est pas mort.");
                        return false;
                    }
                    player.sendMessage("avez ressucit" + target.getName() + ".");
                    if (!this.api.getGameManager().getInGamePlayers().contains(target.getUniqueId()))
                        this.api.getGameManager().getInGamePlayers().add(target.getUniqueId());
                    gameTarget.setAlive(true);
                    gameTarget.setInvincible(true);
                    int size = (int)this.api.getGameManager().getBorder().getWorldBorder().getSize() / 2;
                    target.teleport(new Location(Bukkit.getWorlds().get(0), ThreadLocalRandom.current().nextInt(size), 150.0D, ThreadLocalRandom.current().nextInt(size)));
                    target.setGameMode(GameMode.SURVIVAL);
                    target.setHealth(target.getMaxHealth());
                    target.setFoodLevel(20);
                    for (ItemStack itemStack : gameTarget.getPlayerInv()) {
                        if (itemStack != null && itemStack.getType() != Material.AIR)
                            target.getInventory().addItem(new ItemStack[] { itemStack });
                    }
                    if (gameTarget.getPotionEffects().size() > 0)
                        for (PotionEffect potionEffect : gameTarget.getPotionEffects())
                            target.addPotionEffect(potionEffect);
                    if (!GameUtils.isSoloMode()) {
                        Teams teams = gameTarget.getTeams();
                        GameManager gameManager = this.api.getGameManager();
                        gameManager.getTeamManager().getPlayerTeam().put(target.getUniqueId(), teams);
                        gameManager.getApi().getCommon().getScoreboard().getTeam(teams.getName()).addPlayer((OfflinePlayer)target);
                        if (!gameManager.getAliveTeams().contains(teams))
                            gameManager.getAliveTeams().add(teams);
                    }
                    target.getInventory().setHelmet(gameTarget.getPlayerArmor()[3]);
                    target.getInventory().setChestplate(gameTarget.getPlayerArmor()[2]);
                    target.getInventory().setLeggings(gameTarget.getPlayerArmor()[1]);
                    target.getInventory().setBoots(gameTarget.getPlayerArmor()[0]);
                    target.setLevel(gameTarget.getPlayerExp());
                    target.playSound(target.getLocation(), Sound.ORB_PICKUP, 5.0F, 0.0F);
                    target.sendMessage("avez ressuscitpar un organisateur de la partie !");
                    (new BukkitRunnable() {
                        int current = 0;

                        public void run() {
                            if (this.current >= 15) {
                                gameTarget.setInvincible(false);
                                cancel();
                            }
                            this.current++;
                        }
                    }).runTaskTimer((Plugin)API.getAPI(), 0L, 20L);
                } else {
                    player.sendMessage("insuffisante.");
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0F, 0.0F);
                }
            } else {
                player.sendMessage("partie n'a pas commenc");
            }
        } else {
            player.sendMessage("fournir le nom d'un joueur.");
        }
        return false;
    }
}
