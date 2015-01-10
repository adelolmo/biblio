package org.ado.biblio.desktop.about;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.ado.biblio.desktop.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Andoni del Olmo,
 * @since 10.01.15
 */
public class AboutPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(AboutPresenter.class);

    @FXML
    private Label labelApplicationVersion;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String applicationVersion = AppConfiguration.getApplicationProperty("project.version");
        labelApplicationVersion.setText(applicationVersion);
    }

    public void close() {
        stage.close();
    }
}