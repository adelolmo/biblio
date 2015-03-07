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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * @author sMeet, 30.01.15
 */
public class InstallPresenter implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstallPresenter.class);
    private static final String UPDATE_FILE_NAME = "update.zip";
    private static final File UPDATE_FILE = new File(UPDATE_FILE_NAME);
    private static final File TEMP_DIRECTORY = new File(FileUtils.getTempDirectory(), "biblio-update");
    private static final File USER_DIRECTORY = new File(System.getProperty("user.dir"));

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
        unzipService = new UnzipService(UPDATE_FILE, TEMP_DIRECTORY);
        unzipService.setOnRunning(event -> {
            LOGGER.info("on running");
            labelStepTwo.setText("2. Extracting update file");
        });
        unzipService.setOnSucceeded(event -> {
            LOGGER.info("on succeeded");
            labelStepThree.setText("3. Installation process successfully finished.");

            cleanDirectories();

            copyUpdateFiles(TEMP_DIRECTORY, USER_DIRECTORY);
            FileUtils.deleteQuietly(TEMP_DIRECTORY);

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

    private void cleanDirectories() {
        FileUtils.deleteQuietly(UPDATE_FILE);
        FileUtils.deleteQuietly(TEMP_DIRECTORY);
        cleanDirectory(USER_DIRECTORY);
    }

    private void cleanDirectory(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot clean directory \"%s\".", directory), e);
        }
    }

    private void copyUpdateFiles(File originDirectory, File destinationDirectory) {
        try {
            FileUtils.copyDirectory(originDirectory, destinationDirectory);
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot copy directory \"%s\" to directory \"%s\"",
                    originDirectory.getAbsolutePath(), destinationDirectory.getAbsolutePath()), e);
            e.printStackTrace();
        }
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}