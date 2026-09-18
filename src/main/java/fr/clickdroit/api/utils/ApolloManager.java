package fr.clickdroit.api.utils;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.location.ApolloBlockLocation;
import com.lunarclient.apollo.common.location.ApolloLocation;
import com.lunarclient.apollo.module.glow.GlowModule;
import com.lunarclient.apollo.module.notification.Notification;
import com.lunarclient.apollo.module.notification.NotificationModule;
import com.lunarclient.apollo.module.team.TeamMember;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.module.title.Title;
import com.lunarclient.apollo.module.title.TitleModule;
import com.lunarclient.apollo.module.title.TitleType;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import com.lunarclient.apollo.module.waypoint.WaypointModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import fr.clickdroit.api.game.team.TeamManager;
import fr.clickdroit.api.game.team.Teams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.Color;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ApolloManager {

    private static boolean isApolloEnabled() {
        return Bukkit.getPluginManager().getPlugin("Apollo-Bukkit") != null
                && Bukkit.getPluginManager().getPlugin("Apollo-Bukkit").isEnabled();
    }

    public static void sendPvPTitle() {
        if (!isApolloEnabled())
            return;

        TitleModule titleModule = Apollo.getModuleManager().getModule(TitleModule.class);
        if (titleModule == null)
            return;

        Title title = Title.builder()
                .type(TitleType.TITLE)
                .message(Component.text()
                        .content("⚔ P V P ⚔")
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.BOLD)
                        .build())
                .scale(0.1f)
                .interpolationScale(1.0f)
                .interpolationRate(0.01f)
                .displayTime(Duration.ofMillis(3000L))
                .fadeInTime(Duration.ofMillis(250))
                .fadeOutTime(Duration.ofMillis(300))
                .build();

        Title subtitle = Title.builder()
                .type(TitleType.SUBTITLE)
                .message(Component.text()
                        .content("Le PvP est désormais activé!")
                        .color(NamedTextColor.YELLOW)
                        .build())
                .scale(1.0f)
                .displayTime(Duration.ofMillis(3000L))
                .fadeInTime(Duration.ofMillis(250))
                .fadeOutTime(Duration.ofMillis(300))
                .build();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(p.getUniqueId());
            apolloPlayerOpt.ifPresent(apolloPlayer -> {
                titleModule.displayTitle(apolloPlayer, title);
                titleModule.displayTitle(apolloPlayer, subtitle);
            });
        }
    }

    public static void sendBorderNotification() {
        if (!isApolloEnabled())
            return;

        NotificationModule notifModule = Apollo.getModuleManager().getModule(NotificationModule.class);
        if (notifModule == null)
            return;

        Notification notification = Notification.builder()
                .titleComponent(Component.text("⚠ Bordure en mouvement ⚠", NamedTextColor.RED))
                .descriptionComponent(Component.text("La bordure commence à rétrécir !", NamedTextColor.YELLOW))
                .displayTime(Duration.ofSeconds(5))
                .build();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(p.getUniqueId());
            apolloPlayerOpt.ifPresent(apolloPlayer -> notifModule.displayNotification(apolloPlayer, notification));
        }
    }

    public static void updateTeam(Teams team, Set<UUID> members) {
        if (!isApolloEnabled())
            return;

        TeamModule teamModule = Apollo.getModuleManager().getModule(TeamModule.class);
        GlowModule glowModule = Apollo.getModuleManager().getModule(GlowModule.class);

        if (teamModule == null || glowModule == null)
            return;

        Color awtColor = getTeamColor(team);

        List<TeamMember> teammates = members.stream()
                .map(uuid -> Bukkit.getPlayer(uuid))
                .filter(p -> p != null && p.isOnline())
                .map(member -> {
                    Location location = member.getLocation();
                    return (TeamMember) TeamMember.builder()
                            .playerUuid(member.getUniqueId())
                            .displayName(Component.text()
                                    .content(member.getName())
                                    .color(NamedTextColor.WHITE)
                                    .build())
                            .markerColor(awtColor)
                            .location(ApolloLocation.builder()
                                    .world(location.getWorld().getName())
                                    .x(location.getX())
                                    .y(location.getY())
                                    .z(location.getZ())
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());

        for (UUID uuid : members) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(p.getUniqueId());
                apolloPlayerOpt.ifPresent(apolloPlayer -> {
                    teamModule.updateTeamMembers(apolloPlayer, teammates);

                    for (TeamMember tm : teammates) {
                        if (!tm.getPlayerUuid().equals(p.getUniqueId())) {
                            glowModule.overrideGlow(apolloPlayer, tm.getPlayerUuid(), awtColor);
                        }
                    }
                });
            }
        }
    }

    public static void removePlayerFromTeam(UUID playerUuid, Set<UUID> remainingMembers) {
        if (!isApolloEnabled())
            return;

        TeamModule teamModule = Apollo.getModuleManager().getModule(TeamModule.class);
        GlowModule glowModule = Apollo.getModuleManager().getModule(GlowModule.class);

        if (teamModule == null || glowModule == null)
            return;

        Optional<ApolloPlayer> disconnectedApolloPlayerOpt = Apollo.getPlayerManager().getPlayer(playerUuid);

        disconnectedApolloPlayerOpt.ifPresent(apolloPlayer -> {
            teamModule.resetTeamMembers(apolloPlayer);
        });

        for (UUID remainingId : remainingMembers) {
            Player p = Bukkit.getPlayer(remainingId);
            if (p != null && p.isOnline()) {
                Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(p.getUniqueId());
                apolloPlayerOpt.ifPresent(apolloPlayer -> {
                    glowModule.resetGlow(apolloPlayer, playerUuid);
                });
            }
        }
    }

    private static Color getTeamColor(Teams team) {
        switch (team.getName().toLowerCase()) {
            case "bleu":
                return Color.BLUE;
            case "rouge":
                return Color.RED;
            case "orange":
                return Color.ORANGE;
            case "jaune":
                return Color.YELLOW;
            case "vert":
                return Color.GREEN;
            case "gris":
                return Color.GRAY;
            case "rose":
                return Color.PINK;
            default:
                return Color.WHITE;
        }
    }

    /**
     * Met à jour périodiquement les positions des coéquipiers pour tous les
     * joueurs.
     * Doit être appelé chaque seconde depuis onClockUpdate.
     */
    public static void updateAllTeams(TeamManager teamManager) {
        if (!isApolloEnabled())
            return;

        TeamModule teamModule = Apollo.getModuleManager().getModule(TeamModule.class);
        GlowModule glowModule = Apollo.getModuleManager().getModule(GlowModule.class);

        if (teamModule == null || glowModule == null)
            return;

        for (Teams team : Teams.values()) {
            List<Player> playersInTeam = teamManager.getPlayersInTeam(team);
            if (playersInTeam.size() <= 1)
                continue;

            Color awtColor = getTeamColor(team);

            List<TeamMember> teammates = playersInTeam.stream()
                    .filter(p -> p != null && p.isOnline())
                    .map(member -> {
                        Location location = member.getLocation();
                        return (TeamMember) TeamMember.builder()
                                .playerUuid(member.getUniqueId())
                                .displayName(Component.text()
                                        .content(member.getName())
                                        .color(NamedTextColor.WHITE)
                                        .build())
                                .markerColor(awtColor)
                                .location(ApolloLocation.builder()
                                        .world(location.getWorld().getName())
                                        .x(location.getX())
                                        .y(location.getY())
                                        .z(location.getZ())
                                        .build())
                                .build();
                    })
                    .collect(Collectors.toList());

            for (Player p : playersInTeam) {
                if (p == null || !p.isOnline())
                    continue;
                Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(p.getUniqueId());
                apolloPlayerOpt.ifPresent(apolloPlayer -> {
                    teamModule.updateTeamMembers(apolloPlayer, teammates);

                    for (TeamMember tm : teammates) {
                        if (!tm.getPlayerUuid().equals(p.getUniqueId())) {
                            glowModule.overrideGlow(apolloPlayer, tm.getPlayerUuid(), awtColor);
                        }
                    }
                });
            }
        }
    }

    /**
     * Affiche un waypoint "Centre (0,0)" pour un joueur sur Lunar Client.
     */
    public static void displayCenterWaypoint(Player player) {
        if (!isApolloEnabled())
            return;

        WaypointModule waypointModule = Apollo.getModuleManager().getModule(WaypointModule.class);
        if (waypointModule == null)
            return;

        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(player.getUniqueId());
        apolloPlayerOpt.ifPresent(apolloPlayer -> {
            waypointModule.displayWaypoint(apolloPlayer, Waypoint.builder()
                    .name("Centre (0,0)")
                    .location(ApolloBlockLocation.builder()
                            .world("world")
                            .x(0)
                            .y(64)
                            .z(0)
                            .build())
                    .color(Color.RED)
                    .preventRemoval(false)
                    .hidden(false)
                    .build());
        });
    }

    /**
     * Supprime le waypoint "Centre (0,0)" pour un joueur.
     */
    public static void removeCenterWaypoint(Player player) {
        if (!isApolloEnabled())
            return;

        WaypointModule waypointModule = Apollo.getModuleManager().getModule(WaypointModule.class);
        if (waypointModule == null)
            return;

        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(player.getUniqueId());
        apolloPlayerOpt.ifPresent(apolloPlayer -> waypointModule.removeWaypoint(apolloPlayer, "Centre (0,0)"));
    }
}
