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
        labelDropboxLinkedTo.setText("Not linked");
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
                    if (dropboxManager.hasLinkedAccount()) {
                        return dropboxManager.getAccountInfo();
                    }
                    return null;
                }
            };
        }
    }
}
