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

import java.util.List;

/**
 * @author Andoni del Olmo,
 * @since 26.01.15
 */
public class UpdateManager implements EventHandler<WorkerStateEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(UpdateManager.class);

    @Override
    public void handle(WorkerStateEvent event) {
        final String applicationVersion = AppConfiguration.getApplicationProperty("project.version");
        final int currentVersionMayor = getVersionMayor(applicationVersion);
        final int currentVersionMinor = getVersionMinor(applicationVersion);

        final List<Release> releaseList = (List<Release>) event.getSource().getValue();
        final Release latestRelease = ReleaseVersionUtils.getLatestRelease(releaseList);

        if (ReleaseVersionUtils.updateAvailable(latestRelease, currentVersionMayor, currentVersionMinor)) {
            LOGGER.info("Update to version {} available.", latestRelease.getVersionName());
            openUpdateDialog(latestRelease);
        } else {
            LOGGER.info("Updated to latest version.");
        }
    }

    public void openUpdateDialog(Release release) {
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