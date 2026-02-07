package fr.clickdroit.api.i18n;

import fr.clickdroit.api.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Classe utilitaire pour accéder rapidement aux messages.
 */
public final class Messages {

    private static MessageProvider provider;

    private Messages() {}

    /**
     * Initialise le système de messages.
     *
     * @param api l'instance de l'API
     */
    public static void init(API api) {
        provider = new MessageProvider(api);
        provider.loadLocale("fr");
    }

    /**
     * Récupère le provider de messages.
     *
     * @return le MessageProvider
     */
    public static MessageProvider getProvider() {
        return provider;
    }

    /**
     * Récupère un message.
     *
     * @param key la clé du message
     * @return le message formaté
     */
    public static String get(MessageKey key) {
        return provider.get(key);
    }

    /**
     * Récupère un message avec des placeholders.
     *
     * @param key la clé du message
     * @param placeholders les placeholders
     * @return le message formaté
     */
    public static String get(MessageKey key, Object... placeholders) {
        return provider.get(key, placeholders);
    }

    /**
     * Envoie un message à un joueur.
     *
     * @param player le joueur
     * @param key la clé du message
     * @param placeholders les placeholders
     */
    public static void send(Player player, MessageKey key, Object... placeholders) {
        provider.send(player, key, placeholders);
    }

    /**
     * Envoie un message avec préfixe à un joueur.
     *
     * @param player le joueur
     * @param key la clé du message
     * @param placeholders les placeholders
     */
    public static void sendPrefixed(Player player, MessageKey key, Object... placeholders) {
        provider.sendPrefixed(player, key, placeholders);
    }

    /**
     * Diffuse un message à tous les joueurs.
     *
     * @param key la clé du message
     * @param placeholders les placeholders
     */
    public static void broadcast(MessageKey key, Object... placeholders) {
        String message = get(key, placeholders);
        Bukkit.broadcastMessage(message);
    }

    /**
     * Diffuse un message avec préfixe à tous les joueurs.
     *
     * @param key la clé du message
     * @param placeholders les placeholders
     */
    public static void broadcastPrefixed(MessageKey key, Object... placeholders) {
        String message = get(MessageKey.PREFIX) + get(key, placeholders);
        Bukkit.broadcastMessage(message);
    }

    /**
     * Charge une nouvelle locale.
     *
     * @param locale la locale (fr, en, etc.)
     */
    public static void loadLocale(String locale) {
        provider.loadLocale(locale);
    }
}

