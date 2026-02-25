package fr.clickdroit.api.service;

import fr.clickdroit.api.exception.ServiceAlreadyRegisteredException;
import fr.clickdroit.api.exception.ServiceNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.clickdroit.api.API;

import java.util.logging.Logger;

/**
 * Tests unitaires pour le ServiceManager.
 */
public class ServiceManagerTest {
    private API mockApi;
    private ServiceManager serviceManager;

    @Before
    public void setUp() throws Exception {
        mockApi = mock(API.class);
        serviceManager = new ServiceManager(mockApi);

        // Inject a real logger to avoid NPE during tests since getLogger is final
        java.lang.reflect.Field loggerField = ServiceManager.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(serviceManager, Logger.getGlobal());
    }

    @Test
    public void testRegisterService() {
        TestService service = new TestService();
        serviceManager.registerService(TestService.class, service);

        assertTrue(serviceManager.hasService(TestService.class));
        assertEquals(1, serviceManager.getServiceCount());
    }

    @Test(expected = ServiceAlreadyRegisteredException.class)
    public void testRegisterDuplicateService() {
        TestService service1 = new TestService();
        TestService service2 = new TestService();

        serviceManager.registerService(TestService.class, service1);
        serviceManager.registerService(TestService.class, service2);
    }

    @Test
    public void testGetService() {
        TestService service = new TestService();
        serviceManager.registerService(TestService.class, service);

        TestService retrieved = serviceManager.getService(TestService.class);
        assertSame(service, retrieved);
    }

    @Test(expected = ServiceNotFoundException.class)
    public void testGetNonExistentService() {
        serviceManager.getService(TestService.class);
    }

    @Test
    public void testGetServiceOrNull() {
        assertNull(serviceManager.getServiceOrNull(TestService.class));

        TestService service = new TestService();
        serviceManager.registerService(TestService.class, service);

        assertNotNull(serviceManager.getServiceOrNull(TestService.class));
    }

    @Test
    public void testUnregisterService() {
        TestService service = new TestService();
        serviceManager.registerService(TestService.class, service);

        TestService unregistered = serviceManager.unregisterService(TestService.class);

        assertSame(service, unregistered);
        assertFalse(serviceManager.hasService(TestService.class));
    }

    @Test
    public void testInitializeAll() {
        TestService service = new TestService();
        serviceManager.registerService(TestService.class, service);

        assertFalse(service.isInitialized());

        serviceManager.initializeAll();

        assertTrue(service.isInitialized());
        assertTrue(serviceManager.isInitialized());
    }

    @Test
    public void testShutdownAll() {
        TestService service = new TestService();
        serviceManager.registerService(TestService.class, service);
        serviceManager.initializeAll();

        assertTrue(service.isInitialized());

        serviceManager.shutdownAll();

        assertFalse(service.isInitialized());
        assertFalse(serviceManager.isInitialized());
    }

    @Test
    public void testServicePriority() {
        LowPriorityService lowService = new LowPriorityService();
        HighPriorityService highService = new HighPriorityService();

        // Enregistrer dans l'ordre inverse
        serviceManager.registerService(LowPriorityService.class, lowService);
        serviceManager.registerService(HighPriorityService.class, highService);

        serviceManager.initializeAll();

        // Le service haute priorité doit être initialisé en premier
        assertTrue(highService.initOrder < lowService.initOrder);
    }

    // Classes de test

    private static int initCounter = 0;

    private static class TestService implements GameService {
        private boolean initialized = false;

        @Override
        public void initialize(API api) {
            initialized = true;
        }

        @Override
        public void shutdown() {
            initialized = false;
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }

    private static class HighPriorityService implements GameService {
        private boolean initialized = false;
        int initOrder;

        @Override
        public void initialize(API api) {
            initialized = true;
            initOrder = initCounter++;
        }

        @Override
        public void shutdown() {
            initialized = false;
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }

        @Override
        public int getPriority() {
            return 100;
        }
    }

    private static class LowPriorityService implements GameService {
        private boolean initialized = false;
        int initOrder;

        @Override
        public void initialize(API api) {
            initialized = true;
            initOrder = initCounter++;
        }

        @Override
        public void shutdown() {
            initialized = false;
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }

        @Override
        public int getPriority() {
            return 10;
        }
    }
}
