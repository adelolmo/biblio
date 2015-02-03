package org.ado.biblio.desktop;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;

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
 * Class description here.
 *
 * @author andoni
 * @since 15.11.2014
 */
public class CreateDialog extends Application {

    private static final String APP_KEY = "j802mbeefn9yuep";
    private static final String APP_SECRET = "g7405v56q0u1wj9";
    private static final File ACCESS_TOKEN_FILE = new File("/tmp/accessToken");
    private static DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
    private static DbxRequestConfig config = new DbxRequestConfig("Biblio Data/1.0",
            Locale.getDefault().toString());

    private static DbxWebAuthNoRedirect getDbxWebAuthNoRedirect() {
        return new DbxWebAuthNoRedirect(config, appInfo);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    public void openLinkAndroidAppDialog() {
        System.out.println("Link Android App");
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Dialog");

        StackPane sp = new StackPane();
        Button btnOpen = new Button("Open Dialog");
        sp.getChildren().add(btnOpen);

        // Add action to open a new dialog
        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Creating a new Stage and showing it
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene page2 = new Scene(new Group(new Text(20, 20, "This is a new dialog!")));
                stage.setScene(page2);
                stage.show();
            }
        });

        // Adding StackPane to the scene
        Scene scene = new Scene(sp, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
