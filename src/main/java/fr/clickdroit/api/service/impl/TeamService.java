package fr.clickdroit.api.service.impl;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.service.GameService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service de gestion des équipes UHC.
 */
public class TeamService implements GameService {

    private API api;
    private boolean initialized = false;
    private final Map<UUID, Teams> playerTeams = new ConcurrentHashMap<>();
    private final Map<Teams, Set<UUID>> teamMembers = new ConcurrentHashMap<>();
    private final Set<Teams> aliveTeams = ConcurrentHashMap.newKeySet();

    @Override
    public void initialize(API api) {
        this.api = api;
        for (Teams team : Teams.values()) {
            teamMembers.put(team, ConcurrentHashMap.newKeySet());
        }
        this.initialized = true;
    }

    @Override
    public void shutdown() {
        playerTeams.clear();
        teamMembers.values().forEach(Set::clear);
        aliveTeams.clear();
        initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getPriority() {
        return 90;
    }

    // ===== Gestion des équipes =====

    public void setPlayerTeam(UUID playerUuid, Teams team) {
        Teams oldTeam = playerTeams.get(playerUuid);
        if (oldTeam != null) {
            teamMembers.get(oldTeam).remove(playerUuid);
        }

        playerTeams.put(playerUuid, team);
        teamMembers.get(team).add(playerUuid);
        aliveTeams.add(team);
    }

    public void removePlayerFromTeam(UUID playerUuid) {
        Teams team = playerTeams.remove(playerUuid);
        if (team != null) {
            teamMembers.get(team).remove(playerUuid);
            updateTeamAliveStatus(team);
        }
    }

    public Optional<Teams> getPlayerTeam(UUID playerUuid) {
        return Optional.ofNullable(playerTeams.get(playerUuid));
    }

    public boolean hasTeam(UUID playerUuid) {
        return playerTeams.containsKey(playerUuid);
    }

    public boolean areTeammates(UUID player1, UUID player2) {
        Teams team1 = playerTeams.get(player1);
        Teams team2 = playerTeams.get(player2);
        return team1 != null && team1.equals(team2);
    }

    // ===== Membres d'équipe =====

    public Set<UUID> getTeamMembers(Teams team) {
        return Collections.unmodifiableSet(teamMembers.getOrDefault(team, Collections.emptySet()));
    }

    public int getTeamSize(Teams team) {
        return teamMembers.getOrDefault(team, Collections.emptySet()).size();
    }

    public boolean isTeamEmpty(Teams team) {
        return getTeamSize(team) == 0;
    }

    // ===== Équipes vivantes =====

    public void killTeam(Teams team) {
        aliveTeams.remove(team);
    }

    public void updateTeamAliveStatus(Teams team) {
        if (isTeamEmpty(team)) {
            aliveTeams.remove(team);
        }
    }

    public Set<Teams> getAliveTeams() {
        return Collections.unmodifiableSet(aliveTeams);
    }

    public int getAliveTeamsCount() {
        return aliveTeams.size();
    }

    public boolean isTeamAlive(Teams team) {
        return aliveTeams.contains(team);
    }

    // ===== Utilitaires =====

    public List<Teams> getTeamsWithPlayers() {
        List<Teams> result = new ArrayList<>();
        for (Teams team : Teams.values()) {
            if (!isTeamEmpty(team)) {
                result.add(team);
            }
        }
        return result;
    }

    public void resetAllTeams() {
        playerTeams.clear();
        teamMembers.values().forEach(Set::clear);
        aliveTeams.clear();
    }

    public Map<UUID, Teams> getPlayerTeamsMap() {
        return Collections.unmodifiableMap(playerTeams);
    }
}

