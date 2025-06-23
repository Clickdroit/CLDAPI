package fr.clickdroit.api.listener;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.UHCInfos;
import fr.clickdroit.api.common.player.PlayerUtils;
import fr.clickdroit.api.config.common.GameAccess;
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
import org.bukkit.event.EventPriority;
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

    /**
     * Méthode simplifiée - la plupart des vérifications sont maintenant gérées par GameAccessListener
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Si l'événement est déjà annulé par GameAccessListener, ne rien faire
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        // Vérifier seulement le ban list ici (spécifique à ce listener)
        if (this.api.getGameManager().getBanList().contains(player.getName().toLowerCase())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                    "§c§lVous êtes banni !\n\n" +
                            "§fVous ne pouvez pas rejoindre ce serveur.\n" +
                            "§fContactez un administrateur si vous pensez\n" +
                            "§fque c'est une erreur.");
            return;
        }

        // Les autres vérifications (slots pleins, spectateurs, téléportation, etc.)
        // sont maintenant gérées par GameAccessListener pour éviter les doublons
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ModuleType moduleType = this.api.getGameManager().getModuleManager().getCurrentModule();

        // Initialiser le GamePlayer si nécessaire
        if (!GamePlayer.havePlayer(uuid))
            new GamePlayer(player);

        // Double vérification ban list au join
        if (this.api.getGameManager().getBanList().contains(player.getName().toLowerCase())) {
            player.kickPlayer("§cVous ne pouvez pas rejoindre car vous êtes banni !");
            event.setJoinMessage(null);
            return;
        }

        // Gérer selon l'état du jeu
        switch (this.api.getGameManager().getGameState()) {
            case WAITING:
            case STARTING:
                handleWaitingJoin(player, uuid, moduleType, event);
                break;

            case PLAYING:
                handlePlayingJoin(player, uuid, event);
                break;

            case FINISH:
            case TELEPORTATION:
                event.setJoinMessage(null);
                break;
        }

        // Gestion du vanish
        handleVanishLogic(player, uuid);
    }

    /**
     * Gère l'arrivée d'un joueur pendant l'attente/démarrage
     */
    private void handleWaitingJoin(Player player, UUID uuid, ModuleType moduleType, PlayerJoinEvent event) {
        // Définir le host si nécessaire
        if (this.api.getGameManager().getGameHost() == null) {
            this.api.getGameManager().setGameHost(uuid);
        }

        event.setJoinMessage(null);
        player.teleport(this.api.getLobbyPopulator().getLobbyLocation());

        // Message d'accueil personnalisé
        sendWelcomeMessage(player, moduleType, uuid);

        // Gestion du mumble pour les hosts
        handleHostMumbleMessage(player, uuid);

        // Donner les items par défaut et configurer le joueur
        GameUtils.startPlayer(player, GameMode.ADVENTURE);
        PlayerUtils.giveDefaultItems(player);

        // Message dans l'action bar pour tous les joueurs
        if (!this.api.getGameManager().getVanishList().contains(uuid)) {
            int currentPlayers = GameUtils.getPlayerAmount();
            int maxSlots = this.api.getGameManager().getGameConfig().getGameSlot();

            Bukkit.getOnlinePlayers().forEach(players ->
                    Title.sendActionBar(players,
                            "§a" + player.getName() + " §fa rejoint la partie §8(§e" +
                                    currentPlayers + "§8/§e" + maxSlots + "§8)")
            );
        }
    }

    /**
     * Gère l'arrivée d'un joueur pendant le jeu
     */
    private void handlePlayingJoin(Player player, UUID uuid, PlayerJoinEvent event) {
        if (this.api.getGameManager().getInGamePlayers().contains(uuid)) {
            // Joueur qui se reconnecte
            event.setJoinMessage("§f[§a§l+§f] §a" + player.getName() + " §fs'est reconnecté.");
            this.api.getGameManager().getOfflinePlayers().remove(uuid);
            this.api.getGameManager().getApi().getModules().onPlayerReconnect(player);
            TabHandler.removePrefixFor(player);
        } else {
            // Spectateur
            event.setJoinMessage(null);
            player.sendMessage("§f[§eSpectateurs§f] §fLa partie a déjà commencé, vous êtes spectateur.");
            GameUtils.startPlayer(player, GameMode.SPECTATOR);

            // Téléporter vers un joueur aléatoire
            teleportToRandomPlayer(player);
        }
    }

    /**
     * Envoie un message d'accueil personnalisé
     */
    private void sendWelcomeMessage(Player player, ModuleType moduleType, UUID uuid) {
        GameAccess access = this.api.getGameManager().getGameConfig().getGameAccess();
        String hostName = (UHCInfos.hostName == null) ? "Aucun" : UHCInfos.hostName;

        player.sendMessage("");
        player.sendMessage("   §f(§c!§f) §fVous avez rejoint le serveur de §c§l§n" + hostName + "§f.");
        player.sendMessage("");
        player.sendMessage("  §8• §fJeu §f" + moduleType.getColor() + moduleType.getName());
        player.sendMessage("  §8• §fStatut §f: " + access.getMessage());

        // Message spécial selon le type d'accès
        if (access == GameAccess.CLOSE) {
            if (player.isOp()) {
                player.sendMessage("  §8• §6Vous avez rejoint en tant qu'§ladministrateur");
            } else if (this.api.getGameManager().hasHostAccess(player)) {
                player.sendMessage("  §8• §6Vous avez rejoint en tant qu'§lhost");
            } else if (this.api.getGameManager().getWhitelistedPlayers().contains(player.getName())) {
                player.sendMessage("  §8• §aVous êtes §lwhitelisté §asur ce serveur");
            }
        } else {
            player.sendMessage("  §8• §aLa partie est ouverte à tous !");
        }

        // Informations sur les slots
        int currentPlayers = Bukkit.getOnlinePlayers().size();
        int maxSlots = this.api.getGameManager().getGameConfig().getGameSlot();
        player.sendMessage("  §8• §fJoueurs §f: §b" + currentPlayers + "§f/§b" + maxSlots);

        player.sendMessage("");
    }

    /**
     * Gère le message mumble pour les hosts
     */
    private void handleHostMumbleMessage(Player player, UUID uuid) {
        if (this.api.getGameManager().getGameHost() != null &&
                (this.api.getGameManager().getGameHost().equals(uuid) ||
                        player.isOp() ||
                        this.api.getGameManager().getHosts().contains(uuid))) {

            player.sendMessage("  §8• §fBesoin d'un mumble ?");

            // Utiliser la syntaxe correcte selon votre TextComponentBuilder
            (new InteractiveMessage())
                    .add((new TextComponentBuilder("  §8• §f[§aAvoir un §lmumble§f]"))
                            .setHoverMessage(new String[] { "§8§l> §fCliquez ici pour avoir un." })
                            .setClickAction(ClickEvent.Action.OPEN_URL, "https://goo.gl/z8Whs2")
                            .build())
                    .sendMessage(new Player[] { player });

            player.sendMessage("");

            // Message alternatif pour les non-hosts
        } else {
            BaseComponent[] components = TextComponent.fromLegacyText("  §8• §fRejoignez le mumble avec §c/mumble");
            for (BaseComponent component : components) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mumble"));
            }
            player.spigot().sendMessage(components);
            player.sendMessage("");
        }

        player.sendMessage("  §8• §fPour toutes questions appelez un §9§lModérateur§f.");
        player.sendMessage("");

        // Message pour la gestion des bans/kicks
        (new InteractiveMessage())
                .add(" §f[§6NOUVEAU§f] §8 > §eGestion des ")
                .add((new TextComponentBuilder("§e§l§nbans/kick"))
                        .setHoverMessage(new String[] { "§8§l> §f/h §8[§7kick§8/§7ban§8/§7unban§8/§7banlist§8]" })
                        .setClickAction(ClickEvent.Action.SUGGEST_COMMAND, "/h ")
                        .build())
                .add(" §e!")
                .sendMessage(new Player[] { player });
    }

    /**
     * Téléporte le spectateur vers un joueur aléatoire
     */
    private void teleportToRandomPlayer(Player player) {
        Random random = new Random();
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

        // Filtrer pour ne garder que les joueurs en jeu
        onlinePlayers.removeIf(p -> !this.api.getGameManager().getInGamePlayers().contains(p.getUniqueId()));

        if (!onlinePlayers.isEmpty()) {
            Player randomPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
            player.teleport(randomPlayer);
            player.sendMessage("§eTéléporté vers §f" + randomPlayer.getName() + "§e.");
        } else {
            // Aucun joueur en jeu, téléporter au centre
            player.teleport(this.api.getGameManager().getApi().getLobbyPopulator().getCenter());
            player.sendMessage("§eAucun joueur en jeu, téléporté au centre.");
        }
    }

    /**
     * Gère la logique de vanish
     */
    private void handleVanishLogic(Player player, UUID uuid) {
        if (this.api.getGameManager().getVanishList().contains(uuid)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!this.api.getGameManager().getVanishList().contains(p.getUniqueId())) {
                    p.hidePlayer(player);
                }
            }
        }
    }

    @EventHandler
    private void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        // Vérifier la ban list
        if (this.api.getGameManager().getBanList().contains(player.getName().toLowerCase())) {
            event.setQuitMessage("");
            return;
        }

        switch (this.api.getGameManager().getGameState()) {
            case WAITING:
            case STARTING:
                event.setQuitMessage("");
                // Message de départ amélioré
                if (!this.api.getGameManager().getVanishList().contains(uuid)) {
                    int remainingPlayers = GameUtils.getPlayerAmount() - 1;
                    int maxSlots = this.api.getGameManager().getGameConfig().getGameSlot();

                    Bukkit.getOnlinePlayers().forEach(players ->
                            Title.sendActionBar(players,
                                    "§c" + player.getName() + " §fa quitté la partie §8(§f" +
                                            remainingPlayers + "§8/§f" + maxSlots + "§8)")
                    );
                }
                break;

            case PLAYING:
                if (this.api.getGameManager().getInGamePlayers().contains(uuid)) {
                    // Sauvegarder la position du joueur
                    GamePlayer.getPlayer(uuid).setLastLocation(player.getLocation());
                    this.api.getGameManager().getApi().getModules().onPlayerDisconnect(player);

                    // Message de déconnexion
                    int disconnectTime = this.api.getGameManager().getGameConfig().getDisconnectMinute();
                    event.setQuitMessage(
                            "§f[§c§l?§f] §c" + player.getName() + " §fs'est déconnecté, il dispose de §b" +
                                    disconnectTime + " minute(s) §fpour se reconnecter ou il sera éliminé."
                    );

                    // Ajouter à la liste des joueurs hors ligne
                    this.api.getGameManager().getOfflinePlayers().add(uuid);
                    TabHandler.removePrefixFor(player);

                    // Timer de déconnexion
                    startDisconnectTimer(uuid, disconnectTime);
                } else {
                    // Spectateur qui quitte
                    event.setQuitMessage("");
                }
                break;

            case FINISH:
            case TELEPORTATION:
                event.setQuitMessage("");
                break;
        }
    }

    /**
     * Démarre le timer de déconnexion pour un joueur
     */
    private void startDisconnectTimer(UUID uuid, int disconnectMinutes) {
        (new BukkitRunnable() {
            int time = 0;

            public void run() {
                this.time++;

                // Si le joueur s'est reconnecté, annuler le timer
                if (!PlayerJoinListener.this.api.getGameManager().getOfflinePlayers().contains(uuid)) {
                    cancel();
                    return;
                }

                // Si le temps est écoulé, éliminer le joueur
                if (this.time >= disconnectMinutes) {
                    PlayerJoinListener.this.api.getGameManager().getApi().getModules().onPlayerDieByDisconnect(uuid);
                    cancel();
                }
            }
        }).runTaskTimer(API.getAPI(), 1200L, 1200L); // 1200 ticks = 1 minute
    }
}