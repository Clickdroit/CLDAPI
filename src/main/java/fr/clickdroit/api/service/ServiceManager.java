package fr.clickdroit.api.service;

import fr.clickdroit.api.API;
import fr.clickdroit.api.exception.ServiceAlreadyRegisteredException;
import fr.clickdroit.api.exception.ServiceNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestionnaire central des services de l'API UHC.
 *
 * @author Clickdroit
 * @version 1.1
 * @see GameService
 */
public class ServiceManager {

    private final API api;
    private final Logger logger;
    private final Map<Class<? extends GameService>, GameService> services;
    private final List<GameService> orderedServices;
    private boolean initialized;

    public ServiceManager(API api) {
        this.api = api;
        this.logger = api.getLogger();
        this.services = new ConcurrentHashMap<>();
        this.orderedServices = new ArrayList<>();
        this.initialized = false;
    }

    public <T extends GameService> void registerService(Class<T> serviceClass, T service) {
        if (services.containsKey(serviceClass)) {
            throw new ServiceAlreadyRegisteredException(serviceClass);
        }

        services.put(serviceClass, service);
        orderedServices.add(service);
        orderedServices.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        logger.info("Service enregistré: " + service.getServiceName());

        if (initialized && !service.isInitialized()) {
            try {
                service.initialize(api);
                logger.info("Service initialisé: " + service.getServiceName());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erreur lors de l'initialisation du service: " + service.getServiceName(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends GameService> T getService(Class<T> serviceClass) {
        GameService service = services.get(serviceClass);
        if (service == null) {
            throw new ServiceNotFoundException(serviceClass);
        }
        return (T) service;
    }

    @SuppressWarnings("unchecked")
    public <T extends GameService> T getServiceOrNull(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    public boolean hasService(Class<? extends GameService> serviceClass) {
        return services.containsKey(serviceClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends GameService> T unregisterService(Class<T> serviceClass) {
        GameService service = services.remove(serviceClass);
        if (service != null) {
            orderedServices.remove(service);
            if (service.isInitialized()) {
                try {
                    service.shutdown();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Erreur lors de l'arrêt du service: " + service.getServiceName(), e);
                }
            }
            logger.info("Service désenregistré: " + service.getServiceName());
        }
        return (T) service;
    }

    public void initializeAll() {
        if (initialized) {
            logger.warning("ServiceManager déjà initialisé!");
            return;
        }

        logger.info("Initialisation de " + orderedServices.size() + " services...");

        for (GameService service : orderedServices) {
            if (!service.isInitialized()) {
                try {
                    service.initialize(api);
                    logger.info("Service " + service.getServiceName() + " initialisé");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Erreur lors de l'initialisation de " + service.getServiceName(), e);
                }
            }
        }

        initialized = true;
        logger.info("Tous les services ont été initialisés.");
    }

    public void shutdownAll() {
        if (!initialized) return;

        logger.info("Arrêt de " + orderedServices.size() + " services...");

        for (int i = orderedServices.size() - 1; i >= 0; i--) {
            GameService service = orderedServices.get(i);
            if (service.isInitialized()) {
                try {
                    service.shutdown();
                    logger.info("Service " + service.getServiceName() + " arrêté");
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Erreur lors de l'arrêt de " + service.getServiceName(), e);
                }
            }
        }

        initialized = false;
    }

    public Collection<GameService> getAllServices() {
        return Collections.unmodifiableCollection(services.values());
    }

    public int getServiceCount() {
        return services.size();
    }

    public boolean isInitialized() {
        return initialized;
    }
}

