package org.ado.biblio.desktop.install;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.desktop.util.ZipUtils;

import java.io.File;

/**
 * @author Andoni del Olmo,
 * @since 31.01.15
 */
public class UnzipService extends Service<Void> {

    private File zipFile;
    private File destDirectory;

    public UnzipService(File zipFile, File destDirectory) {
        this.zipFile = zipFile;
        this.destDirectory = destDirectory;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ZipUtils.extractFolder(zipFile, destDirectory);
                pause(5000);
                return null;
            }
        };
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}