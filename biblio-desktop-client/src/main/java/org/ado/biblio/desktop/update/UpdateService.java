package org.ado.biblio.desktop.update;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.update.BiblioUpdate;
import org.ado.biblio.update.ComponentEnum;
import org.ado.biblio.update.Release;
import org.ado.biblio.update.kimono.KimonoBiblioUpdate;

import java.util.List;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class UpdateService extends Service<List<Release>> {

    private BiblioUpdate biblioUpdate;

    public UpdateService() {
        biblioUpdate = new KimonoBiblioUpdate();
    }

    @Override
    protected Task<List<Release>> createTask() {
        return new Task<List<Release>>() {
            @Override
            protected List<Release> call() throws Exception {
                return biblioUpdate.getReleases(ComponentEnum.DESKTOP_CLIENT);
            }
        };
    }
}