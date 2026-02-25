package fr.clickdroit.api.module.standard.listener;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.module.standard.UHCStandard;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import fr.clickdroit.api.event.custom.UHCPlayerKillEvent;

import java.util.HashMap;
import java.util.Map;

public class UHCStandardListener implements Listener {
    private final UHCStandard uhcStandard;

    private static Map<Player, Player> attackerMap;

    public UHCStandardListener(UHCStandard uhcStandard) {
        this.uhcStandard = uhcStandard;
        attackerMap = new HashMap<>();
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
        gamePlayer.setPlayerInv(player.getInventory().getContents());
        gamePlayer.setPlayerArmor(player.getInventory().getArmorContents());
        gamePlayer.setPlayerExp(player.getLevel());
        gamePlayer.setLastLocation(player.getLocation());
        event.setDeathMessage("");

        // Trigger generic API player kill event for other plugins
        Player killer = getAttacker(player);
        UHCPlayerKillEvent killEvent = new UHCPlayerKillEvent(killer, player, event.getDrops());
        Bukkit.getPluginManager().callEvent(killEvent);

        if (this.uhcStandard.getGameManager().getModuleManager().getCurrentModule().equals(ModuleType.UHC))
            this.uhcStandard.getGameManager().getApi().getModules().onPlayerDeath(player, getAttacker(player));
    }

    private void addAttacker(final Player victim, Player attacker) {
        if (!attackerMap.containsKey(victim) || !((Player) attackerMap.get(victim)).equals(attacker))
            attackerMap.put(victim, attacker);
        (new BukkitRunnable() {
            public void run() {
                UHCStandardListener.attackerMap.remove(victim);
            }
        }).runTaskLater((Plugin) this.uhcStandard.getGameManager().getApi(), 160L);
    }

    public static Player getAttacker(Player victim) {
        return attackerMap.get(victim);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        Arrow arrow;
        if (!(event.getEntity() instanceof Player))
            return;
        Player victim = (Player) event.getEntity();
        Player attacker = null;
        switch (event.getDamager().getType()) {
            case PLAYER:
                attacker = (Player) event.getDamager();
                addAttacker(victim, attacker);
                break;
            case ARROW:
                arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                    addAttacker(victim, attacker);
                }
                break;
        }
    }
}
