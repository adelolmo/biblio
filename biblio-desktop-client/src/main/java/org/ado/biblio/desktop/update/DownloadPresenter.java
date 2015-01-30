package org.ado.biblio.desktop.update;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.ado.biblio.update.Artifact;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Andoni del Olmo,
 * @since 29.01.15
 */
public class DownloadPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(DownloadPresenter.class);

    @FXML
    private ProgressBar progressBarDownload;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void process(Stage stage, Artifact artifact) throws IOException {
        final File updateFile = new File("update.zip");
        final FileDownloadTask fileDownloadTask = new FileDownloadTask(artifact.getUrl(), updateFile);

        progressBarDownload.progressProperty().bind(fileDownloadTask.progressProperty());
        progressBarDownload.visibleProperty().bind(fileDownloadTask.runningProperty());

        new Thread(fileDownloadTask).start();

        fileDownloadTask.setOnSucceeded(event -> {
            Dialogs.create()
                    .title("Restart required")
                    .message("To complete the installation please restart Biblio.")
                    .showInformation();
            stage.close();
        });
    }
}