package org.ado.biblio.desktop.booksapi;

import org.ado.biblio.domain.BookMessageDTO;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class NoBookInfoFoundException extends Exception {

    public NoBookInfoFoundException(BookMessageDTO bookMessage) {
        super(String.format("No book was found for code \"%s\" \"%s\".", bookMessage.getFormat(), bookMessage.getCode()));
    }
}
