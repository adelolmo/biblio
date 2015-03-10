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
        fileDownloadTask.setOnFailed(event -> {
            Dialogs.create()
                    .title("Download failed")
                    .message("It was not possible to download the update. Please try again later.")
                    .showError();
            stage.close();
        });
    }
}