package org.ado.biblio.update;

import java.util.List;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public interface BiblioUpdate {

    List<Release> getReleases(ComponentEnum component);

    List<Release> getReleases();
}
