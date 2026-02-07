package fr.clickdroit.api.service.impl;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.exception.PlayerNotFoundException;
import fr.clickdroit.api.service.GameService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service de gestion des joueurs UHC.
 * Centralise toutes les opérations liées aux joueurs en partie.
 */
public class PlayerService implements GameService {

    private API api;
    private boolean initialized = false;
    private final Map<UUID, GamePlayer> players = new ConcurrentHashMap<>();
    private final Set<UUID> alivePlayers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> offlinePlayers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> spectators = ConcurrentHashMap.newKeySet();

    @Override
    public void initialize(API api) {
        this.api = api;
        this.initialized = true;
    }

    @Override
    public void shutdown() {
        players.clear();
        alivePlayers.clear();
        offlinePlayers.clear();
        spectators.clear();
        initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getPriority() {
        return 100; // Haute priorité - chargé en premier
    }

    // ===== Gestion des joueurs =====

    public void registerPlayer(GamePlayer player) {
        players.put(player.getUuid(), player);
        alivePlayers.add(player.getUuid());
    }

    public void unregisterPlayer(UUID uuid) {
        players.remove(uuid);
        alivePlayers.remove(uuid);
        offlinePlayers.remove(uuid);
        spectators.remove(uuid);
    }

    public GamePlayer getPlayer(UUID uuid) {
        GamePlayer player = players.get(uuid);
        if (player == null) {
            throw new PlayerNotFoundException(uuid);
        }
        return player;
    }

    public Optional<GamePlayer> getPlayerOptional(UUID uuid) {
        return Optional.ofNullable(players.get(uuid));
    }

    public boolean isRegistered(UUID uuid) {
        return players.containsKey(uuid);
    }

    // ===== État des joueurs =====

    public void setPlayerAlive(UUID uuid, boolean alive) {
        if (alive) {
            alivePlayers.add(uuid);
            spectators.remove(uuid);
        } else {
            alivePlayers.remove(uuid);
        }
    }

    public void setPlayerOffline(UUID uuid, boolean offline) {
        if (offline) {
            offlinePlayers.add(uuid);
        } else {
            offlinePlayers.remove(uuid);
        }
    }

    public void setPlayerSpectator(UUID uuid) {
        alivePlayers.remove(uuid);
        spectators.add(uuid);
    }

    public boolean isAlive(UUID uuid) {
        return alivePlayers.contains(uuid);
    }

    public boolean isOffline(UUID uuid) {
        return offlinePlayers.contains(uuid);
    }

    public boolean isSpectator(UUID uuid) {
        return spectators.contains(uuid);
    }

    // ===== Collections =====

    public Collection<GamePlayer> getAllPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    public Set<UUID> getAlivePlayers() {
        return Collections.unmodifiableSet(alivePlayers);
    }

    public Set<UUID> getOfflinePlayers() {
        return Collections.unmodifiableSet(offlinePlayers);
    }

    public Set<UUID> getSpectators() {
        return Collections.unmodifiableSet(spectators);
    }

    public int getAliveCount() {
        return alivePlayers.size();
    }

    public int getTotalCount() {
        return players.size();
    }

    public List<GamePlayer> getAlivePlayersList() {
        return alivePlayers.stream()
                .map(players::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ===== Statistiques =====

    public int getTotalKills() {
        return players.values().stream()
                .mapToInt(GamePlayer::getKills)
                .sum();
    }

    public Optional<GamePlayer> getTopKiller() {
        return players.values().stream()
                .max(Comparator.comparingInt(GamePlayer::getKills));
    }

    public void resetAllPlayers() {
        players.clear();
        alivePlayers.clear();
        offlinePlayers.clear();
        spectators.clear();
    }
}

