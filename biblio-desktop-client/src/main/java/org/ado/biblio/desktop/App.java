package org.ado.biblio.desktop;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        final AppView appView = new AppView();
        final Scene scene = new Scene(appView.getView());
        primaryStage.setTitle("Biblio");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
