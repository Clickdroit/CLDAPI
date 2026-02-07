package fr.clickdroit.api.exception;

import fr.clickdroit.api.service.GameService;

/**
 * Exception levée lorsqu'un service demandé n'est pas trouvé.
 */
public class ServiceNotFoundException extends GameException {

    private final Class<? extends GameService> serviceClass;

    public ServiceNotFoundException(Class<? extends GameService> serviceClass) {
        super("Service non trouvé: " + serviceClass.getSimpleName());
        this.serviceClass = serviceClass;
    }

    public Class<? extends GameService> getServiceClass() {
        return serviceClass;
    }
}

