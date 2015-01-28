package org.ado.biblio.update;

import java.io.IOException;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public interface BiblioUpdate {

    Release getLatestRelease() throws IOException;

}
