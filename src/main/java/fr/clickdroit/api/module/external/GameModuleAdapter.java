package fr.clickdroit.api.module.external;

import fr.clickdroit.api.API;
import fr.clickdroit.api.module.Modules;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Adaptateur qui fait le pont entre un GameModule externe et le système Modules
 * interne.
 * Permet aux modules externes de s'intégrer dans le système de jeu existant.
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameModuleAdapter extends Modules {

    private final GameModule externalModule;

    /**
     * Crée un nouvel adaptateur pour un module externe.
     * 
     * @param externalModule le module externe à adapter
     */
    public GameModuleAdapter(GameModule externalModule) {
        if (externalModule == null) {
            throw new IllegalArgumentException("Le module externe ne peut pas être null");
        }
        this.externalModule = externalModule;
    }

    /**
     * Récupère le module externe encapsulé.
     * 
     * @return le module externe
     */
    public GameModule getExternalModule() {
        return externalModule;
    }

    @Override
    public void onLoad() {
        externalModule.onLoad();
    }

    @Override
    public void init() {
        externalModule.init();
    }

    @Override
    public void onStart(API api) {
        super.onStart(api);
        externalModule.onGameStart(api);
    }

    @Override
    public void onPlayerDeath(Player player, Player killer) {
        super.onPlayerDeath(player, killer);
        externalModule.onPlayerDeath(player, killer);
    }

    @Override
    public void onPlayerDieByDisconnect(UUID uuid) {
        super.onPlayerDieByDisconnect(uuid);
        externalModule.onPlayerDisconnect(uuid);
    }

    @Override
    public void onClockUpdate(int gameTime) {
        externalModule.onClockUpdate(gameTime);
    }

    @Override
    public void onDay(boolean sendMessage) {
        super.onDay(sendMessage);
        externalModule.onDay(sendMessage);
    }

    @Override
    public void onNight(boolean sendMessage) {
        super.onNight(sendMessage);
        externalModule.onNight(sendMessage);
    }

    @Override
    public void onEpisodeSwitch() {
        externalModule.onEpisodeSwitch();
    }

    @Override
    public void onPlayerReconnect(Player player) {
        externalModule.onPlayerReconnect(player);
    }

    @Override
    public void onPlayerDisconnect(Player player) {
        // Le module externe peut gérer cela si nécessaire
    }

    @Override
    public void onPlayerChat(Player player, String message) {
        externalModule.onPlayerChat(player, message);
    }

    @Override
    public void openConfig(Player player) {
        externalModule.openConfig(player);
    }
}
