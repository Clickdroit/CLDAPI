package fr.clickdroit.api.game.listeners;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PvPListener implements Listener {
    private final GameManager gameManager;
    private final World world;
    private final Random random;

    public PvPListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.world = gameManager.getWorldPopulator().getGameWorld();
        this.random = new Random();
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getGameMode().equals(GameMode.SPECTATOR))
                event.setCancelled(true);
            if (GamePlayer.getPlayer(player.getUniqueId()).isInvincible())
                event.setCancelled(true);
            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID) && player.getLocation().getY() < 0.0D &&
                    !GameUtils.isGameStarted())
                player.teleport(this.gameManager.getApi().getLobbyPopulator().getLobbyLocation());
            if (GameUtils.isGameStarted()) {
                GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
                if (gamePlayer == null)
                    return;
                if (gamePlayer.getInvincibilityCount() > 0)
                    event.setCancelled(true);
                if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && gamePlayer
                        .getInvincibilityNoFallCount() > 0)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Cow) {
            if (DropItemRate.LEATHER.getAmount() != 0) {
                int r = this.random.nextInt(100);
                if (r <= DropItemRate.LEATHER.getAmount())
                    this.world.dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.LEATHER, 1));
            }
        } else if (event.getEntity() instanceof org.bukkit.entity.Enderman) {
            if (DropItemRate.ENDERPEARL.getAmount() != 0) {
                int r = this.random.nextInt(100);
                if (r <= DropItemRate.ENDERPEARL.getAmount())
                    this.world.dropItemNaturally(event.getEntity().getLocation(),
                            new ItemStack(Material.ENDER_PEARL, 1));
            }
        } else if (event.getEntity() instanceof org.bukkit.entity.Skeleton && DropItemRate.ARROW
                .getAmount() != 0) {
            int r = this.random.nextInt(100);
            if (r <= DropItemRate.ARROW.getAmount())
                this.world.dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.ARROW, 1));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player &&
                !GameUtils.isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    private void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player attacker = (Player) event.getDamager();
                if (this.gameManager.getInGamePlayers().contains(attacker.getUniqueId())
                        && this.gameManager.getInGamePlayers().contains(victim.getUniqueId()) && this.gameManager
                                .getTeamManager().getPlayerTeam().containsKey(victim.getUniqueId())
                        && this.gameManager.getTeamManager().getPlayerTeam().containsKey(attacker.getUniqueId())
                        && ((Teams) this.gameManager
                                .getTeamManager().getPlayerTeam().get(victim.getUniqueId()))
                                .equals(this.gameManager.getTeamManager().getPlayerTeam().get(attacker.getUniqueId()))
                        &&
                        !this.gameManager.getGameConfig().isFriendlyfire())
                    event.setCancelled(true);
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player attacker = (Player) projectile.getShooter();
                    if (this.gameManager.getInGamePlayers().contains(attacker.getUniqueId())
                            && this.gameManager.getInGamePlayers().contains(victim.getUniqueId()) && this.gameManager
                                    .getTeamManager().getPlayerTeam().containsKey(victim.getUniqueId())
                            && this.gameManager.getTeamManager().getPlayerTeam().containsKey(attacker.getUniqueId())
                            && ((Teams) this.gameManager
                                    .getTeamManager().getPlayerTeam().get(victim.getUniqueId()))
                                    .equals(this.gameManager.getTeamManager().getPlayerTeam()
                                            .get(attacker.getUniqueId()))
                            &&
                            !this.gameManager.getGameConfig().isFriendlyfire())
                        event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING
                || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }
}
