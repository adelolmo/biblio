package org.ado.biblio.desktop.update;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ado.biblio.desktop.AppConfiguration;
import org.ado.biblio.update.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class UpdateManager {

    private final Logger LOGGER = LoggerFactory.getLogger(UpdateManager.class);

    public EventHandler<WorkerStateEvent> getOnSucceeded() {
        return event -> {
            final String applicationVersion = AppConfiguration.getApplicationProperty("project.version");
            final int currentVersionMayor = getVersionMayor(applicationVersion);
            final int currentVersionMinor = getVersionMinor(applicationVersion);

            final Release latestRelease = (Release) event.getSource().getValue();

            if (ReleaseVersionUtils.updateAvailable(latestRelease, currentVersionMayor, currentVersionMinor)) {
                LOGGER.info("Update to version {} available.", latestRelease.getVersionName());
                openUpdateDialog(latestRelease);
            } else {
                LOGGER.info("Updated to latest version.");
            }
        };
    }

    public EventHandler<WorkerStateEvent> getOnFailed() {
        return event -> LOGGER.error("Unable to retrieve latest version details", event.getSource().getException());
    }

    private void openUpdateDialog(Release release) {
        Stage stage = new Stage();
        final UpdateView updateView = new UpdateView();
        final UpdatePresenter presenter = (UpdatePresenter) updateView.getPresenter();
        presenter.setStage(stage);
        presenter.setRelease(release);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(updateView.getView()));
        stage.setTitle("Update available");
        stage.show();
    }

    private int getVersionMayor(String versionName) {
        versionName = versionName.replace("-SNAPSHOT", "");
        return Integer.valueOf(versionName.substring(0, versionName.indexOf(".")));
    }

    private int getVersionMinor(String versionName) {
        versionName = versionName.replace("-SNAPSHOT", "");
        return Integer.valueOf(versionName.substring(versionName.indexOf(".") + 1));
    }
}