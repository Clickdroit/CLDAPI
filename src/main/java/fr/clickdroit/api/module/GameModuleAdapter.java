package fr.clickdroit.api.module;

import fr.clickdroit.api.API;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Adaptateur faisant le pont entre un GameModule (System A) et le cycle de vie Modules interne.
 * Permet aux plugins externes de s'intégrer au moteur de jeu CLDAPI.
 *
 * @author Clickdroit
 * @version 1.0
 */
public class GameModuleAdapter extends Modules {

    private final GameModule module;

    public GameModuleAdapter(GameModule module) {
        if (module == null) {
            throw new IllegalArgumentException("Le module externe ne peut pas être null");
        }
        this.module = module;
    }

    public GameModule getModule() {
        return module;
    }

    @Override
    public void onLoad() {
        module.onLoad();
    }

    @Override
    public void init() {
        module.onEnable(API.getAPI());
    }

    @Override
    public void onStart(API api) {
        super.onStart(api);
        module.onGameStart(api);
    }

    @Override
    public void onPlayerDeath(Player player, Player killer) {
        super.onPlayerDeath(player, killer);
        module.onPlayerDeath(player, killer);
    }

    @Override
    public void onPlayerDieByDisconnect(UUID uuid) {
        super.onPlayerDieByDisconnect(uuid);
        module.onPlayerDeathByDisconnect(uuid);
    }

    @Override
    public void onClockUpdate(int gameTime) {
        module.onClockUpdate(gameTime);
    }

    @Override
    public void onDay(boolean sendMessage) {
        super.onDay(sendMessage);
        module.onDay(sendMessage);
    }

    @Override
    public void onNight(boolean sendMessage) {
        super.onNight(sendMessage);
        module.onNight(sendMessage);
    }

    @Override
    public void onEpisodeSwitch() {
        module.onEpisodeSwitch();
    }

    @Override
    public void onPlayerReconnect(Player player) {
        module.onPlayerReconnect(player);
    }

    @Override
    public void onPlayerDisconnect(Player player) {
        module.onPlayerDisconnect(player);
    }

    @Override
    public void onPlayerChat(Player player, String message) {
        module.onPlayerChat(player, message);
    }

    @Override
    public void openConfig(Player player) {
        module.openConfig(player);
    }
}
