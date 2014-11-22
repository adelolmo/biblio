package org.ado.biblio.desktop.dropbox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("initialize...");
        try {
            if (dropboxManager.hasLinkedAccount()) {
                buttonLink.setDisable(true);
                buttonUnlink.setDisable(false);
                final AccountInfo accountInfo = dropboxManager.getAccountInfo();
                populateAccountInfoFields(accountInfo);

            } else {
                buttonLink.setDisable(false);
                buttonUnlink.setDisable(true);
            }
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    public void link() throws DropboxException {
        dropboxManager.link(new DropboxManager.DropboxAccountLinkListener() {
            @Override
            public void accountLinked(AccountInfo accountInfo) {
                buttonLink.setDisable(true);
                buttonUnlink.setDisable(false);
                populateAccountInfoFields(accountInfo);
            }
        });
    }

    public void unlink() throws DropboxException {
        dropboxManager.unlink();
        labelDropboxLinkedTo.setText("Not linked");
    }

    public void close() {

    }

    private void populateAccountInfoFields(AccountInfo accountInfo) {
        labelDropboxLinkedTo.setText(accountInfo.getDisplayName());
        labelUserId.setText(String.valueOf(accountInfo.getUserId()));
        labelCountry.setText(accountInfo.getCountry());
    }
}
