package org.ado.biblio.desktop.dropbox;

/**
 * Class description here.
 *
 * @author andoni
 * @since 05.11.2014
 */
public class DropboxException extends Exception {
    public DropboxException() {
        super();
    }

    public DropboxException(String message) {
        super(message);
    }

    public DropboxException(String message, Throwable cause) {
        super(message, cause);
    }

    public DropboxException(Throwable cause) {
        super(cause);
    }
}
