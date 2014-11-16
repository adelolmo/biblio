package org.ado.biblio.desktop.dropbox;

import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.json.JsonReader;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Class description here.
 *
 * @author andoni
 * @since 05.11.2014
 */
public class DropboxManager {

    private final Logger LOGGER = LoggerFactory.getLogger(DropboxManager.class);
    private static DbxRequestConfig DROPBOX_CONFIG = new DbxRequestConfig("Biblio Data/1.0", Locale.getDefault().toString());
    private static final File ACCESS_TOKEN_FILE = new File(FileUtils.getUserDirectoryPath(), ".dropbox_access_token");

    private DbxClient dbxClient;

    public interface DropboxAccountLinkListener {
        void accountLinked(AccountInfo accountInfo);
    }

    @PostConstruct
    public void init() throws DropboxException {
        LOGGER.info("Initializing");
    }

    @PreDestroy
    public void cleanup() {
        LOGGER.info("Cleaning up!");
    }

    public boolean isConnected() {
        try {
            return ACCESS_TOKEN_FILE.exists() && getClient() == null;
        } catch (Exception e) {
            return false;
        }
    }

    public void connect() {
        if (!ACCESS_TOKEN_FILE.exists()) {
            try {
                getClient();
            } catch (JsonReader.FileLoadException e) {


            }
        }
    }

    public void disconnect() {
        dbxClient = null;
    }

    public boolean isLinked() {
        return ACCESS_TOKEN_FILE.exists();
    }

    public void link(DropboxAccountLinkListener dropboxAccountLinkListener) throws DropboxException {
        try {
            new DropboxLinkDialog(DROPBOX_CONFIG, ACCESS_TOKEN_FILE).open(dropboxAccountLinkListener);
        } catch (IOException e) {
            throw new DropboxException("Unable to link dropbox account", e);
        }
    }

    public void unlink() throws DropboxException {
        try {
            getClient().disableAccessToken();
        } catch (DbxException | JsonReader.FileLoadException e) {
            throw new DropboxException("Cannot unlink account", e);
        }
        disconnect();

        FileUtils.deleteQuietly(ACCESS_TOKEN_FILE);
    }

    public String getLinkedAccountName() throws DropboxException {
        try {
            final DbxClient dbxClient = getClient();
            return dbxClient.getAccountInfo().displayName;
        } catch (JsonReader.FileLoadException | DbxException e) {
            throw new DropboxException("Unable to get Account name", e);
        }

    }

    //                             DbxAuthInfo.Writer.writeToFile(dbxAuthInfo, ACCESS_TOKEN_FILE);


    private DbxClient getClient() throws JsonReader.FileLoadException {
        if (dbxClient == null) {
            DbxAuthInfo authInfo = DbxAuthInfo.Reader.readFromFile(ACCESS_TOKEN_FILE);
            return new DbxClient(DROPBOX_CONFIG, authInfo.accessToken, authInfo.host);
        } else {
            return dbxClient;
        }
    }
}
