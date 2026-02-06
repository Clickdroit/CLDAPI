package fr.clickdroit.api.module;

import fr.clickdroit.api.API;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Adaptateur qui permet d'utiliser un {@link GameModule} externe comme un {@link Modules} interne.
 * <p>
 * Cette classe fait le pont entre les modules externes enregistrés par d'autres plugins
 * et le système de modules interne de l'API.
 * </p>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameModuleAdapter extends Modules {
    private final GameModule gameModule;
    private final API api;

    /**
     * Crée un nouvel adaptateur pour un module externe.
     * 
     * @param gameModule le module externe à adapter
     * @param api l'instance de l'API
     */
    public GameModuleAdapter(GameModule gameModule, API api) {
        this.gameModule = gameModule;
        this.api = api;
    }

    /**
     * Récupère le module externe sous-jacent.
     * 
     * @return le module externe
     */
    public GameModule getGameModule() {
        return gameModule;
    }

    @Override
    public void onLoad() {
        // onLoad est déjà appelé lors de l'enregistrement
    }

    @Override
    public void onStart(API api) {
        super.onStart(api);
        gameModule.onGameStart(api);
    }

    @Override
    public void onPlayerDeath(Player player, Player killer) {
        super.onPlayerDeath(player, killer);
        gameModule.onPlayerDeath(player, killer);
    }

    @Override
    public void onPlayerDieByDisconnect(UUID uuid) {
        super.onPlayerDieByDisconnect(uuid);
        gameModule.onPlayerDeathByDisconnect(uuid);
    }

    @Override
    public void onEpisodeSwitch() {
        gameModule.onEpisodeSwitch();
    }

    @Override
    public void init() {
        // Initialization is handled by onEnable
    }

    @Override
    public void onClockUpdate(int gameTime) {
        gameModule.onClockUpdate(gameTime);
    }

    @Override
    public void onPlayerReconnect(Player player) {
        gameModule.onPlayerReconnect(player);
    }

    @Override
    public void onPlayerDisconnect(Player player) {
        gameModule.onPlayerDisconnect(player);
    }

    @Override
    public void onPlayerChat(Player player, String message) {
        gameModule.onPlayerChat(player, message);
    }

    @Override
    public void onDay(boolean sendMessage) {
        super.onDay(sendMessage);
        gameModule.onDay(sendMessage);
    }

    @Override
    public void onNight(boolean sendMessage) {
        super.onNight(sendMessage);
        gameModule.onNight(sendMessage);
    }

    @Override
    public void openConfig(Player player) {
        gameModule.openConfig(player);
    }
}
