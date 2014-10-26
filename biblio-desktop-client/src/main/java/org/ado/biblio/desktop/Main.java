package org.ado.biblio.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Main.fxml"));
      /*  Parent root = (Parent)loader.load();
        MyController controller = (MyController)loader.getController();
        controller.setStageAndSetupListeners(stage); // or what you want to do*/


        Scene scene = new Scene(root);

        primaryStage.setTitle("Biblio");
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
