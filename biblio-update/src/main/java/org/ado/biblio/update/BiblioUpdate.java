package org.ado.biblio.update;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public interface BiblioUpdate {

    Release getLatestRelease() throws BiblioUpdateException;

}
