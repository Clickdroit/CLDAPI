package fr.clickdroit.api.exception;

import fr.clickdroit.api.service.GameService;

/**
 * Exception levée lorsqu'on tente d'enregistrer un service déjà existant.
 */
public class ServiceAlreadyRegisteredException extends GameException {

    private final Class<? extends GameService> serviceClass;

    public ServiceAlreadyRegisteredException(Class<? extends GameService> serviceClass) {
        super("Service déjà enregistré: " + serviceClass.getSimpleName());
        this.serviceClass = serviceClass;
    }

    public Class<? extends GameService> getServiceClass() {
        return serviceClass;
    }
}

