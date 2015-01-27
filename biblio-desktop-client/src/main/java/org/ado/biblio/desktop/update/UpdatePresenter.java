package org.ado.biblio.desktop.update;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.ado.biblio.update.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Andoni del Olmo,
 * @since 26.01.15
 */
public class UpdatePresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(UpdatePresenter.class);

    @FXML
    private Label labelVersion;

    private Stage stage;
    private Release release;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRelease(Release release) {
        this.release = release;
        labelVersion.setText(release.getName());
    }

    public void download() {
        LOGGER.info("download");
        HostServicesDelegate.getInstance(null).showDocument(release.getArtifactUrl());
        stage.close();
    }

    public void close() {
        LOGGER.info("close");
        stage.close();
    }
}