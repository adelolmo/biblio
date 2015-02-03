package org.ado.biblio.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ado.biblio.desktop.install.InstallPresenter;
import org.ado.biblio.desktop.install.InstallView;

import java.io.File;

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