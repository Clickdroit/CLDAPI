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
    public void setUp() {
        mockApi = mock(API.class);
        when(mockApi.getLogger()).thenReturn(Logger.getLogger("TestLogger"));
        when(mockApi.getDataFolder()).thenReturn(new java.io.File("test-data"));
        when(mockApi.getResource(anyString())).thenReturn(null);

        provider = new MessageProvider(mockApi);
        provider.loadLocale("fr");
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

