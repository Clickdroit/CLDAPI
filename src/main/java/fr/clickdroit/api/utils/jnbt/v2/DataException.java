package fr.clickdroit.api.utils.jnbt.v2;

public class DataException extends RuntimeException {
    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
