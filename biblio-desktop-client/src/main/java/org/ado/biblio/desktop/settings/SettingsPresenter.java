package org.ado.biblio.desktop.settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ado.biblio.desktop.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Andoni del Olmo,
 * @since 20.12.14
 */
public class SettingsPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(SettingsPresenter.class);

    @FXML
    private TextField textFieldServerUrl;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldServerUrl.setText(AppConfiguration.getConfigurationProperty("server.url"));
    }

    public void save() {
        AppConfiguration.setConfigurationProperty("server.url", textFieldServerUrl.getText());
        stage.close();
    }

    public void close() {
        stage.close();
    }
}