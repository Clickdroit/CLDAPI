package fr.clickdroit.api.i18n;

import fr.clickdroit.api.API;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Gestionnaire des messages internationalisés.
 */
public class MessageProvider {

    private final API api;
    private final Logger logger;
    private final Map<String, String> messages = new HashMap<>();
    private String currentLocale = "fr";

    public MessageProvider(API api) {
        this.api = api;
        this.logger = api.getLogger();
    }

    /**
     * Charge les messages pour une locale donnée.
     *
     * @param locale la locale (fr, en, etc.)
     */
    public void loadLocale(String locale) {
        this.currentLocale = locale;
        messages.clear();

        // Charger les messages par défaut
        loadDefaultMessages();

        // Charger depuis le fichier externe si présent
        File langFile = new File(api.getDataFolder(), "lang/messages_" + locale + ".yml");
        if (langFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(langFile);
            for (String key : config.getKeys(true)) {
                if (!config.isConfigurationSection(key)) {
                    messages.put(key, config.getString(key));
                }
            }
            logger.info("Messages chargés depuis: " + langFile.getName());
        }

        // Charger depuis les ressources internes
        InputStream stream = api.getResource("lang/messages_" + locale + ".yml");
        if (stream != null) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(stream, StandardCharsets.UTF_8));
            for (String key : config.getKeys(true)) {
                if (!config.isConfigurationSection(key) && !messages.containsKey(key)) {
                    messages.put(key, config.getString(key));
                }
            }
        }

        logger.info("Locale chargée: " + locale + " (" + messages.size() + " messages)");
    }

    private void loadDefaultMessages() {
        // Messages par défaut en français
        messages.put("prefix", "§8[§6UHC§8] §f");
        messages.put("error_prefix", "§c");
        messages.put("success_prefix", "§a");

        messages.put("no_permission", "§cVous n'avez pas la permission d'exécuter cette commande.");
        messages.put("player_only", "§cCette commande ne peut être exécutée que par un joueur.");
        messages.put("player_not_found", "§cJoueur introuvable: {player}");
        messages.put("invalid_arguments", "§cArguments invalides. Usage: {usage}");

        messages.put("game.starting", "§eLa partie démarre dans {time} secondes!");
        messages.put("game.started", "§a§lLA PARTIE A COMMENCÉ!");
        messages.put("game.ended", "§6§lFIN DE LA PARTIE!");
        messages.put("game.already_started", "§cLa partie a déjà commencé.");
        messages.put("game.not_started", "§cLa partie n'a pas encore commencé.");

        messages.put("pvp.enabled", "§c§l⚔ LE PVP EST DÉSORMAIS ACTIVÉ! ⚔");
        messages.put("pvp.disabled", "§ePvP désactivé.");
        messages.put("pvp.time_remaining", "§ePvP dans {time}");

        messages.put("border.shrinking", "§c§lLA BORDURE COMMENCE À RÉTRÉCIR!");
        messages.put("border.stopped", "§eLa bordure s'est arrêtée à {size} blocs.");
        messages.put("border.size", "§eTaille de la bordure: {size} blocs");

        messages.put("episode.change", "§6§l═══ ÉPISODE {episode} ═══");
        messages.put("episode.current", "§eÉpisode actuel: {episode}");

        messages.put("death.killed_by_player", "§c{player} §fa été tué par §c{killer}§f.");
        messages.put("death.killed_by_mob", "§c{player} §fa été tué par un mob.");
        messages.put("death.environment", "§c{player} §fest mort.");
        messages.put("death.disconnect", "§c{player} §fs'est déconnecté trop longtemps et a été éliminé.");

        messages.put("team.joined", "§aVous avez rejoint l'équipe {team}.");
        messages.put("team.left", "§eVous avez quitté votre équipe.");
        messages.put("team.full", "§cCette équipe est complète.");
        messages.put("team.eliminated", "§cL'équipe {team} a été éliminée!");

        messages.put("scenario.enabled", "§aScénario {scenario} activé.");
        messages.put("scenario.disabled", "§eScénario {scenario} désactivé.");

        messages.put("config.saved", "§aConfiguration sauvegardée.");
        messages.put("config.loaded", "§aConfiguration chargée.");
        messages.put("config.invalid", "§cConfiguration invalide: {error}");

        messages.put("admin.host_required", "§cVous devez être host pour exécuter cette commande.");
        messages.put("admin.host_added", "§a{player} est maintenant host.");
        messages.put("admin.host_removed", "§e{player} n'est plus host.");

        messages.put("cycle.day_start", "§6§l☀ LE SOLEIL SE LÈVE ☀");
        messages.put("cycle.night_start", "§9§l☾ LA NUIT TOMBE ☾");
    }

    /**
     * Récupère un message par sa clé.
     *
     * @param key la clé du message
     * @return le message formaté
     */
    public String get(MessageKey key) {
        return get(key.getKey());
    }

    /**
     * Récupère un message par sa clé string.
     *
     * @param key la clé du message
     * @return le message formaté
     */
    public String get(String key) {
        String message = messages.getOrDefault(key, "§cMessage manquant: " + key);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Récupère un message avec des placeholders.
     *
     * @param key la clé du message
     * @param placeholders les placeholders (clé, valeur, clé, valeur, ...)
     * @return le message formaté
     */
    public String get(MessageKey key, Object... placeholders) {
        return get(key.getKey(), placeholders);
    }

    /**
     * Récupère un message avec des placeholders.
     *
     * @param key la clé du message
     * @param placeholders les placeholders (clé, valeur, clé, valeur, ...)
     * @return le message formaté
     */
    public String get(String key, Object... placeholders) {
        String message = get(key);

        for (int i = 0; i < placeholders.length - 1; i += 2) {
            String placeholder = "{" + placeholders[i] + "}";
            String value = String.valueOf(placeholders[i + 1]);
            message = message.replace(placeholder, value);
        }

        return message;
    }

    /**
     * Envoie un message à un joueur.
     *
     * @param player le joueur
     * @param key la clé du message
     * @param placeholders les placeholders
     */
    public void send(Player player, MessageKey key, Object... placeholders) {
        player.sendMessage(get(key, placeholders));
    }

    /**
     * Envoie un message avec préfixe à un joueur.
     *
     * @param player le joueur
     * @param key la clé du message
     * @param placeholders les placeholders
     */
    public void sendPrefixed(Player player, MessageKey key, Object... placeholders) {
        player.sendMessage(get(MessageKey.PREFIX) + get(key, placeholders));
    }

    /**
     * Retourne la locale actuelle.
     *
     * @return la locale
     */
    public String getCurrentLocale() {
        return currentLocale;
    }
}

