package fr.clickdroit.api.module;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public abstract class Modules {
    public void onStart(API api) {
    }

    public void onPlayerDeath(Player player, Player killer) {
        API api = API.getAPI();
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
        if (api.getGameManager().getModuleManager().getCurrentModule() == ModuleType.LG) {
            Bukkit.getOnlinePlayers()
                    .forEach(players -> players.playSound(players.getLocation(), "entity.wolf.howl", 3.0F, 0.0F));
        } else {
            Bukkit.getOnlinePlayers()
                    .forEach(players -> players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 3.0F, 0.0F));
        }
        api.getGameManager().getInGamePlayers().remove(player.getUniqueId());
        gamePlayer.setAlive(false);
        if (killer != null)
            GamePlayer.getPlayer(killer.getUniqueId()).addKill();
        if (!Scenario.TIMEBOMB.isEnabled()) {
            for (ItemStack item : gamePlayer.getPlayerInv()) {
                if (item != null && item.getType() != Material.AIR)
                    gamePlayer.getLastLocation().getWorld().dropItemNaturally(gamePlayer.getLastLocation(), item);
            }
            for (ItemStack item : gamePlayer.getPlayerArmor()) {
                if (item != null && item.getType() != Material.AIR)
                    gamePlayer.getLastLocation().getWorld().dropItemNaturally(gamePlayer.getLastLocation(), item);
            }
        }
        if (player.getActivePotionEffects().size() > 0)
            for (PotionEffect potionEffect : player.getActivePotionEffects())
                gamePlayer.getPotionEffects().add(potionEffect);
        if (!GameUtils.isSoloMode() && api
                .getGameManager().getTeamManager().getPlayerTeam().containsKey(player.getUniqueId())) {
            Teams teams = (Teams) api.getGameManager().getTeamManager().getPlayerTeam().get(player.getUniqueId());
            api.getGameManager().getTeamManager().killTeam(teams);
        }
        GameUtils.setSpectator(player);
    }

    public void onPlayerDieByDisconnect(UUID uuid) {
        API api = API.getAPI();
        GamePlayer gamePlayer = GamePlayer.getPlayer(uuid);
        Bukkit.getOnlinePlayers()
                .forEach(players -> players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 3.0F, 0.0F));
        api.getGameManager().getInGamePlayers().remove(uuid);
        gamePlayer.setAlive(false);
        if (!GameUtils.isSoloMode() && api
                .getGameManager().getTeamManager().getPlayerTeam().containsKey(uuid))
            api.getGameManager().getTeamManager()
                    .killTeam((Teams) api.getGameManager().getTeamManager().getPlayerTeam().get(uuid));
        for (ItemStack item : gamePlayer.getPlayerInv()) {
            if (item != null && item.getType() != Material.AIR)
                gamePlayer.getLastLocation().getWorld().dropItemNaturally(gamePlayer.getLastLocation(), item);
        }
        for (ItemStack item : gamePlayer.getPlayerArmor()) {
            if (item != null && item.getType() != Material.AIR)
                gamePlayer.getLastLocation().getWorld().dropItemNaturally(gamePlayer.getLastLocation(), item);
        }
        int disconnectMinutes = api.getGameManager().getGameConfig().getDisconnectSeconds() / 60;
        Bukkit.broadcastMessage("§f[§c§l!§f] §a" + gamePlayer.getName() + "§fs'est §bdéconnecté pendant plus de "
                + disconnectMinutes + " minute(s) §fet a été §céliminé§f.");
    }

    public void onDay(boolean sendMessage) {
        if (sendMessage)
            Bukkit.broadcastMessage("§6§l☀ LE SOLEIL SE LEVE ☀");
    }

    public void onNight(boolean sendMessage) {
        if (sendMessage)
            Bukkit.broadcastMessage("§9§l☾ LA LUNE COMMENCE A S'ECLAIRCIR ☾");
    }

    public abstract void onLoad();

    public abstract void onEpisodeSwitch();

    public abstract void init();

    public abstract void onClockUpdate(int paramInt);

    public abstract void onPlayerReconnect(Player paramPlayer);

    public abstract void onPlayerDisconnect(Player paramPlayer);

    public abstract void onPlayerChat(Player paramPlayer, String paramString);

    public void openConfig(Player player) {
        player.sendMessage("§cAucune configuration disponible pour ce mode.");
    }
}
