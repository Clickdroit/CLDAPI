package fr.clickdroit.api.utils.particles;

public class VersionIncompatibleException extends RuntimeException {
    public VersionIncompatibleException(String message) {
        super(message);
    }

    public VersionIncompatibleException(String message, Throwable cause) {
        super(message, cause);
    }
}