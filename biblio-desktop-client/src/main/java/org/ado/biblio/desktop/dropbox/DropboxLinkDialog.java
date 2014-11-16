package org.ado.biblio.desktop.dropbox;

import com.dropbox.core.*;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ado.biblio.desktop.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Class description here.
 *
 * @author andoni
 * @since 15.11.2014
 */
public class DropboxLinkDialog {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxLinkDialog.class);

    private static final String APP_KEY = "j802mbeefn9yuep";
    private static final String APP_SECRET = "g7405v56q0u1wj9";
    private static DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

    private Stage stage;

    private DbxRequestConfig dropboxConfig;
    private File accessTokenFile;

    public DropboxLinkDialog(DbxRequestConfig dropboxConfig, File accessTokenFile) throws IOException {
        this.dropboxConfig = dropboxConfig;
        this.accessTokenFile = accessTokenFile;
        stage = new Stage();
    }

    private DbxWebAuthNoRedirect getDbxWebAuthNoRedirect() {
        return new DbxWebAuthNoRedirect(dropboxConfig, appInfo);
    }

    public void open(DropboxManager.DropboxAccountLinkListener dropboxAccountLinkListener) throws IOException {

        if (!accessTokenFile.exists()) {

            final DbxWebAuthNoRedirect webAuth = getDbxWebAuthNoRedirect();

            final WebView browser = new WebView();
            final WebEngine engine = browser.getEngine();
            Stage stage = new Stage();
            engine.getLoadWorker().stateProperty().addListener(getListener(dropboxAccountLinkListener, webAuth, engine, stage));
            String url = webAuth.start();
            LOGGER.debug(url);
            engine.load(url);

            StackPane sp = new StackPane();
            sp.getChildren().add(browser);

            stage.initModality(Modality.APPLICATION_MODAL);
            Scene page2 = new Scene(sp);
            stage.setScene(page2);
            stage.show();
        } else {

            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/DropboxUnlinkDialog.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    private ChangeListener<Worker.State> getListener(final DropboxManager.DropboxAccountLinkListener dropboxAccountLinkListener,
                                                     final DbxWebAuthNoRedirect webAuth, final WebEngine engine, final Stage stage) {
        return (observable, oldValue, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                try {
                    final String authCode = (String) engine.executeScript("document.getElementById('auth-code').textContent");
                    LOGGER.debug("Authorization Code [{}]", authCode);
                    DbxAuthFinish authFinish = webAuth.finish(authCode);

                    String accessToken = authFinish.accessToken;
                    final DbxAuthInfo dbxAuthInfo = new DbxAuthInfo(accessToken, DbxHost.Default);
                    DbxAuthInfo.Writer.writeToFile(dbxAuthInfo, accessTokenFile);

                    DbxClient client = new DbxClient(dropboxConfig, dbxAuthInfo.accessToken, dbxAuthInfo.host);

                    final String displayName = client.getAccountInfo().displayName;
                    LOGGER.info("Linked account [{}]", displayName);
                    dropboxAccountLinkListener.accountLinked(getAccountInfo(client.getAccountInfo()));
                    stage.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        };
    }

    private AccountInfo getAccountInfo(DbxAccountInfo dropboxAccountInfo) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCountry(dropboxAccountInfo.country);
        accountInfo.setDisplayName(dropboxAccountInfo.displayName);
        accountInfo.setReferralLink(dropboxAccountInfo.referralLink);
        accountInfo.setUserId(dropboxAccountInfo.userId);
        return accountInfo;
    }

    public void unlink() {
        LOGGER.debug("unlink from dropbox");
        stage.close();
    }
}
