package org.ado.biblio.desktop.install;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author sMeet, 30.01.15
 */
public class InstallPresenter implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstallPresenter.class);
    private static final String UPDATE_FILE_NAME = "update.zip";
    private static final File UPDATE_FILE = new File(UPDATE_FILE_NAME);

    @FXML
    private Label labelStepOne;

    @FXML
    private Label labelStepTwo;

    @FXML
    private Label labelStepThree;

    private UnzipService unzipService;

    @PostConstruct
    public void init() {
        LOGGER.info("PostConstruct");
        unzipService = new UnzipService(UPDATE_FILE, new File(System.getProperty("user.dir")));
        unzipService.setOnRunning(event -> {
            LOGGER.info("on running");
            labelStepTwo.setText("2. Extracting update file");
        });
        unzipService.setOnSucceeded(event -> {
            LOGGER.info("on succeeded");
            labelStepThree.setText("3. Installation process successfully finished.");
            FileUtils.deleteQuietly(UPDATE_FILE);

            final Task<Void> voidTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    pause(2000);
                    System.exit(0);
                    return null;
                }
            };
            new Thread(voidTask).start();
        });
        unzipService.setOnFailed(event -> {
            LOGGER.info("on failed");
            labelStepThree.setText("3. Error on installation process!");
            final Task<Void> voidTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    pause(2000);
                    System.exit(1);
                    return null;
                }
            };
            new Thread(voidTask).start();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("initialize");
    }

    public void execute() {
        LOGGER.info("installing update...");
        labelStepOne.setText("1. Application update found");
        unzipService.start();
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}