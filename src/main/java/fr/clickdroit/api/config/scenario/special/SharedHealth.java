package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.game.team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.Plugin;

public class SharedHealth extends ScenarioManager implements Listener {
    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (!event.isCancelled() &&
                API.getAPI().getGameManager().getGameState().equals(GameState.PLAYING) && Rules.pvp.isActive() && event
                .getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Teams teams = (Teams)API.getAPI().getGameManager().getTeamManager().getPlayerTeam().get(player.getUniqueId());
            if (teams != null)
                for (Player players : API.getAPI().getGameManager().getTeamManager().getPlayersInTeam(teams)) {
                    if (player.getUniqueId() != players.getUniqueId())
                        players.setHealth(player.getHealth() - event.getDamage() / 2.0D);
                }
        }
    }

    @EventHandler
    private void onDamage(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Teams teams = (Teams)API.getAPI().getGameManager().getTeamManager().getPlayerTeam().get(player.getUniqueId());
            if (teams != null)
                for (Player players : API.getAPI().getGameManager().getTeamManager().getPlayersInTeam(teams))
                    players.setHealth(player.getHealth() + event.getAmount());
        }
    }

    public void configure() {
        this.scenario = Scenario.SHAREDHEALTH;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}

