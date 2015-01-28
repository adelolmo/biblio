package org.ado.biblio.desktop.update;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.update.BiblioUpdate;
import org.ado.biblio.update.Release;
import org.ado.biblio.update.kimono.KimonoBiblioUpdate;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class UpdateService extends Service<Release> {

    private BiblioUpdate biblioUpdate;

    public UpdateService() {
        biblioUpdate = new KimonoBiblioUpdate();
    }

    @Override
    protected Task<Release> createTask() {
        return new Task<Release>() {
            @Override
            protected Release call() throws Exception {
                return biblioUpdate.getLatestRelease();
            }
        };
    }
}