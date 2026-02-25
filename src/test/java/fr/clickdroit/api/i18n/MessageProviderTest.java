package fr.clickdroit.api.i18n;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.clickdroit.api.API;

import java.util.logging.Logger;

/**
 * Tests unitaires pour le système de messages.
 */
public class MessageProviderTest {

    private API mockApi;
    private MessageProvider provider;

    @Before
    public void setUp() throws Exception {
        mockApi = mock(API.class);

        provider = new MessageProvider(mockApi);
        provider.loadLocale("fr");

        // Inject messages via reflection because we cannot mock final methods
        // getDataFolder/getResource
        java.lang.reflect.Field messagesField = MessageProvider.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> messages = (java.util.Map<String, String>) messagesField.get(provider);
        messages.put("prefix", "§8[§6UHC§8] §f");
        messages.put("player_not_found", "§cJoueur introuvable: {player}");
        messages.put("death.killed_by_player", "§c{player} §fa été tué par §c{killer}§f.");
    }

    @Test
    public void testGetMessage() {
        String message = provider.get(MessageKey.PREFIX);
        assertNotNull(message);
        assertFalse(message.isEmpty());
    }

    @Test
    public void testGetMessageWithPlaceholders() {
        String message = provider.get(MessageKey.PLAYER_NOT_FOUND, "player", "TestPlayer");
        assertTrue(message.contains("TestPlayer"));
        assertFalse(message.contains("{player}"));
    }

    @Test
    public void testMissingMessage() {
        String message = provider.get("non_existent_key");
        assertTrue(message.contains("Message manquant"));
    }

    @Test
    public void testCurrentLocale() {
        assertEquals("fr", provider.getCurrentLocale());
    }

    @Test
    public void testMultiplePlaceholders() {
        // Test avec le message death.killed_by_player qui a deux placeholders
        String message = provider.get("death.killed_by_player",
                "player", "Victim",
                "killer", "Killer");
        assertTrue(message.contains("Victim"));
        assertTrue(message.contains("Killer"));
    }
}
