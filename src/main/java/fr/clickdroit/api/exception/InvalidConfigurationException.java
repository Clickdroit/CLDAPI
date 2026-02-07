package fr.clickdroit.api.exception;

/**
 * Exception levée lors d'une erreur de configuration.
 */
public class InvalidConfigurationException extends GameException {

    private final String configKey;
    private final Object invalidValue;

    public InvalidConfigurationException(String message) {
        super(message);
        this.configKey = null;
        this.invalidValue = null;
    }

    public InvalidConfigurationException(String configKey, Object invalidValue, String message) {
        super("Configuration invalide pour '" + configKey + "': " + message);
        this.configKey = configKey;
        this.invalidValue = invalidValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }
}

