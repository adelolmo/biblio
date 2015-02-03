package org.ado.biblio.desktop.update;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ado.biblio.desktop.util.FileSizeConverter;
import org.ado.biblio.update.Artifact;
import org.ado.biblio.update.ComponentEnum;
import org.ado.biblio.update.Release;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @since 26.01.15
 */
public class UpdatePresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(UpdatePresenter.class);

    @FXML
    private Label labelVersion;

    @FXML
    private Label labelSize;

    @FXML
    private TextArea textAreaReleaseNotes;

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
        labelSize.setText(getArtifactSize(release));
        textAreaReleaseNotes.setText(release.getReleaseNotes());
    }

    public void download() throws IOException {
        LOGGER.info("download");
        final Artifact artifact = release.getArtifactUrl().get(ComponentEnum.DESKTOP_CLIENT);

        final Stage stage = new Stage();
        final DownloadView updateView = new DownloadView();
        final DownloadPresenter presenter = (DownloadPresenter) updateView.getPresenter();
        presenter.process(stage, artifact);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(updateView.getView()));
        stage.setTitle("Update");
        stage.show();

        this.stage.close();
    }

    public void close() {
        LOGGER.info("close");
        stage.close();
    }

    private String getArtifactSize(Release release) {
        final long artifactSize = release.getArtifactUrl().get(ComponentEnum.DESKTOP_CLIENT).getSize();
        return FileSizeConverter.getSize(artifactSize, getSizeUnit(artifactSize));
    }

    private FileSizeConverter.SizeUnit getSizeUnit(long artifactSize) {
        if (artifactSize > FileUtils.ONE_MB) {
            return FileSizeConverter.SizeUnit.MB;
        } else {
            return FileSizeConverter.SizeUnit.KB;
        }
    }
}