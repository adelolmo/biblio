package org.ado.biblio.desktop;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initializes HostServicesDelegate
        getHostServices();

        final AppView appView = new AppView();
        final Scene scene = new Scene(appView.getView());
        final AppPresenter presenter = (AppPresenter) appView.getPresenter();
        presenter.setStage(primaryStage);
        primaryStage.setTitle("Biblio");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(Double.MAX_VALUE);
        primaryStage.setMaxWidth(Double.MAX_VALUE);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }
}
