package org.ado.biblio.update;

/**
 * @author Andoni del Olmo,
 * @since 01.02.15
 */
public class BiblioUpdateException extends Exception {

    public BiblioUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BiblioUpdateException(String message) {
        super(message);
    }
}