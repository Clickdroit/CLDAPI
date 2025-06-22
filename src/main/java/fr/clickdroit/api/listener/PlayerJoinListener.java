package fr.clickdroit.api.listener;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.UHCInfos;
import fr.clickdroit.api.common.player.PlayerUtils;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.TabHandler;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.msg.InteractiveMessage;
import fr.clickdroit.api.utils.msg.TextComponentBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final API api;

    public PlayerJoinListener(API api) {
        this.api = api;
    }

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getWorld().getName().equals("Lobby"))
            event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!GameUtils.isGameStarted() &&
                GameUtils.getPlayerAmount() >= this.api.getGameManager().getGameConfig().getGameSlot() && !player.isOp())
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "serveur est plein.");
        if (this.api.getGameManager().getGameState().equals(GameState.TELEPORTATION))
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "ne pouvez pas rejoindre la partie maintenant.");
        if ((this.api.getGameManager().getGameState().equals(GameState.PLAYING) || this.api.getGameManager().getGameState().equals(GameState.FINISH)) &&
                !this.api.getGameManager().getGameConfig().isSpectators() && !this.api.getGameManager().getInGamePlayers().contains(player.getUniqueId()) &&
                !player.isOp())
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "spectateurs sont ddurant la partie.");
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Random random;
        List<Player> list;
        Player randomPlayer, player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ModuleType moduleType = this.api.getGameManager().getModuleManager().getCurrentModule();
        if (!GamePlayer.havePlayer(uuid))
            new GamePlayer(player);
        if (this.api.getGameManager().getBanList().contains(player.getName().toLowerCase())) {
            player.kickPlayer("ne pouvez pas rejoindre car vous banni !");
            event.setJoinMessage(null);
            return;
        }
        switch (this.api.getGameManager().getGameState()) {
            case WAITING:
            case STARTING:
                if (this.api.getGameManager().getGameHost() == null)
                    this.api.getGameManager().setGameHost(uuid);
                event.setJoinMessage(null);
                player.teleport(this.api.getLobbyPopulator().getLobbyLocation());
                player.sendMessage("");
                player.sendMessage("   avez rejoint serveur de "+ ((UHCInfos.hostName == null) ? "Aucun" : UHCInfos.hostName) + "");
                player.sendMessage("");
                player.sendMessage("  " + moduleType.getColor() + moduleType.getName());
                if (this.api.getGameManager().getGameHost() != null && (this.api.getGameManager().getGameHost().equals(uuid) || player.isOp() || this.api.getGameManager().getHosts().contains(uuid))) {
                    player.sendMessage("  d'un mumble ?");
                    (new InteractiveMessage())
                            .add((new TextComponentBuilder("  un "))
                                    .setHoverMessage(new String[] { "pour gun "}).setClickAction(ClickEvent.Action.RUN_COMMAND, "/mumble create").build())
                                            .sendMessage(new Player[] { player });
                    player.sendMessage("");
        } else {
                        BaseComponent[] components = TextComponent.fromLegacyText("  le mumble avec ");
                        for (BaseComponent component : components)
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mumble"));
                        player.spigot().sendMessage(components);
                        player.sendMessage("");
                    }
                    player.sendMessage("  toutes questions appelez un ");
                            player.sendMessage("");
                    (new InteractiveMessage())
                            .add(" des ")
                            .add((new TextComponentBuilder(""))
                                    .setHoverMessage(new String[] { ""}).setClickAction(ClickEvent.Action.SUGGEST_COMMAND, "/h add").build())
                                            .add(" ")
                                            .sendMessage(new Player[] { player });
                    if (!this.api.getGameManager().getVanishList().contains(uuid))
                        Bukkit.getOnlinePlayers().forEach(players -> Title.sendActionBar(players, ""+ player.getName() + " a rejoint la partie "+ GameUtils.getPlayerAmount() + ""+ this.api.getGameManager().getGameConfig().getGameSlot() + ""));
                    GameUtils.startPlayer(player, GameMode.ADVENTURE);
                    PlayerUtils.giveDefaultItems(player);
                    break;
                    case PLAYING:
                        if (this.api.getGameManager().getInGamePlayers().contains(uuid)) {
                            this.api.getGameManager().getApi().getModules().onPlayerReconnect(player);
                            event.setJoinMessage(""+ player.getName() + " reconnect");
                            this.api.getGameManager().getOfflinePlayers().remove(uuid);
                            TabHandler.removePrefixFor(player);
                            break;
                        }
                        event.setJoinMessage(null);
                        player.sendMessage("partie a dcommencvous spectateur.");
                        GameUtils.startPlayer(player, GameMode.SPECTATOR);
                        random = new Random();
                        list = new ArrayList<>(Bukkit.getOnlinePlayers());
                        randomPlayer = list.get(random.nextInt(list.size()));
                        if (randomPlayer == null) {
                            player.teleport(this.api.getGameManager().getApi().getLobbyPopulator().getCenter());
                            break;
                        }
                        player.teleport((Entity)randomPlayer);
                        break;
                    case FINISH:
                    case TELEPORTATION:
                        event.setJoinMessage(null);
                        break;
    }
                    if (this.api.getGameManager().getVanishList().contains(uuid))
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!this.api.getGameManager().getVanishList().contains(p.getUniqueId()))
                                p.hidePlayer(player);
                        }
                }

                @EventHandler
                private void PlayerQuitEvent(PlayerQuitEvent event) {
                Player player = event.getPlayer();
                final UUID uuid = player.getUniqueId();
                if (this.api.getGameManager().getBanList().contains(player.getName().toLowerCase())) {
                    event.setQuitMessage("");
                    return;
                }
                switch (this.api.getGameManager().getGameState()) {
                    case WAITING:
                    case STARTING:
                        event.setQuitMessage("");
                        if (!this.api.getGameManager().getVanishList().contains(uuid))
                            Bukkit.getOnlinePlayers().forEach(players -> Title.sendActionBar(players, ""+ player.getName() + " quittla partie "+ (GameUtils.getPlayerAmount() - 1) + "+ this.api.getGameManager().getGameConfig().getGameSlot() + "));
                        break;
                    case PLAYING:
                        if (this.api.getGameManager().getInGamePlayers().contains(uuid)) {
                            GamePlayer.getPlayer(uuid).setLastLocation(player.getLocation());
                            this.api.getGameManager().getApi().getModules().onPlayerDisconnect(player);
                            event.setQuitMessage(""+ player.getName() + " dil dispose de "+ this.api
                                    .getGameManager().getGameConfig().getDisconnectMinute() + " minute(s) se reconnecter ou alors il sera" );
                            this.api.getGameManager().getOfflinePlayers().add(uuid);
                            TabHandler.removePrefixFor(player);
                            (new BukkitRunnable() {
                                int time = 0;

                                public void run() {
                                    this.time++;
                                    if (!PlayerJoinListener.this.api.getGameManager().getOfflinePlayers().contains(uuid)) {
                                        cancel();
                                        return;
                                    }
                                    if (this.time >= PlayerJoinListener.this.api.getGameManager().getGameConfig().getDisconnectMinute()) {
                                        PlayerJoinListener.this.api.getGameManager().getApi().getModules().onPlayerDieByDisconnect(uuid);
                                        cancel();
                                    }
                                }
                            }).runTaskTimer((Plugin)API.getAPI(), 1200L, 1200L);
                            break;
                        }
                        event.setQuitMessage("");
                        break;
                    case FINISH:
                    case TELEPORTATION:
                        event.setQuitMessage("");
                        break;
                }
            }
        }
