package org.ado.biblio.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ado.biblio.desktop.install.InstallPresenter;
import org.ado.biblio.desktop.install.InstallView;

/**
 * @author sMeet, 30.01.15
 */
public class InstallUpdate extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        final InstallView installView = new InstallView();
        final Scene scene = new Scene(installView.getView());
        final InstallPresenter presenter = (InstallPresenter) installView.getPresenter();
        presenter.process(primaryStage);
        primaryStage.setTitle("Install update");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(Double.MAX_VALUE);
        primaryStage.setMaxWidth(Double.MAX_VALUE);
        primaryStage.show();
    }
}