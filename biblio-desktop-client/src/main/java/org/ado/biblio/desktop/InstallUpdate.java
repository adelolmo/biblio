package org.ado.biblio.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ado.biblio.desktop.install.InstallPresenter;
import org.ado.biblio.desktop.install.InstallView;

import java.io.File;

/**
 * @author sMeet, 30.01.15
 */
public class InstallUpdate extends Application {

    private static final String UPDATE_FILE_NAME = "update.zip";
    private static final File UPDATE_FILE = new File(UPDATE_FILE_NAME);

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (UPDATE_FILE.exists()) {
            showStage(primaryStage);
        } else {
            System.exit(0);
        }
    }

    private void showStage(Stage primaryStage) {
        final InstallView installView = new InstallView();
        final Scene scene = new Scene(installView.getView());
        final InstallPresenter presenter = (InstallPresenter) installView.getPresenter();
        primaryStage.setTitle("Biblio update");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(Double.MAX_VALUE);
        primaryStage.setMaxWidth(Double.MAX_VALUE);
        primaryStage.show();

        presenter.execute();
    }
}