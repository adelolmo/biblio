package org.ado.biblio.desktop;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.NoAccountDropboxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ado.biblio.desktop.AppConfiguration.DATABASE_FILE;

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
 * @since 25.10.2014
 */
public class App extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private DropboxManager dropboxManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        dropboxManager = new DropboxManager();

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
        try {
            dropboxManager.uploadSync(DATABASE_FILE.getName(), DATABASE_FILE);
        } catch (DropboxException e) {
            LOGGER.error("Unable to upload database", e);
        } catch (NoAccountDropboxException e) {
            LOGGER.debug("Cannot upload database. {}.", e.getMessage());
        }
        Injector.forgetAll();
    }
}
