package org.ado.biblio.desktop.dropbox;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

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
 * @since 16.11.2014
 */
public class DropboxPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(DropboxPresenter.class);

    @FXML
    public Button buttonUnlink;

    @FXML
    public Button buttonLink;

    @FXML
    public Label labelDropboxLinkedTo;

    @FXML
    public Label labelUserId;

    @FXML
    public Label labelCountry;

    @Inject
    private DropboxManager dropboxManager;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final LoadAccountInfo loadAccountInfo = new LoadAccountInfo();
        loadAccountInfo.setOnSucceeded(event -> {
            final AccountInfo accountInfo = (AccountInfo) event.getSource().getValue();
            if (accountInfo != null) {
                buttonLink.setDisable(true);
                buttonUnlink.setDisable(false);
                populateAccountInfoFields(accountInfo);
            } else {
                buttonLink.setDisable(false);
                buttonUnlink.setDisable(true);
            }
        });
        loadAccountInfo.start();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void link() throws DropboxException {
        dropboxManager.link(accountInfo -> {
            buttonLink.setDisable(true);
            buttonUnlink.setDisable(false);
            populateAccountInfoFields(accountInfo);
        });
    }

    public void unlink() throws DropboxException {
        dropboxManager.unlink();
        labelDropboxLinkedTo.setText("");
        labelUserId.setText("");
        labelCountry.setText("");
    }

    public void close() {
        stage.close();
    }

    private void populateAccountInfoFields(AccountInfo accountInfo) {
        labelDropboxLinkedTo.setText(accountInfo.getDisplayName());
        labelUserId.setText(String.valueOf(accountInfo.getUserId()));
        labelCountry.setText(accountInfo.getCountry());
    }

    class LoadAccountInfo extends Service<AccountInfo> {
        @Override
        protected Task<AccountInfo> createTask() {
            return new Task<AccountInfo>() {
                @Override
                protected AccountInfo call() throws Exception {
                    if (dropboxManager.isAccountLinked()) {
                        return dropboxManager.getAccountInfo();
                    }
                    return null;
                }
            };
        }
    }
}
