package org.ado.biblio.desktop.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import static org.ado.biblio.desktop.AppConfiguration.APP_CONFIG_DIRECTORY;

/**
 * Class description here.
 *
 * @author andoni
 * @since 05.11.2014
 */
public class DropboxManager {

    private final Logger LOGGER = LoggerFactory.getLogger(DropboxManager.class);
    private static DbxRequestConfig DROPBOX_CONFIG = new DbxRequestConfig("Biblio Data/1.0", Locale.getDefault().toString());
    private static final File ACCESS_TOKEN_FILE = new File(APP_CONFIG_DIRECTORY, ".dropbox_access_token");
    private static final String COVER_PATH = "/covers/%s.%s";

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

    public void uploadCover(String isbn, File file) throws DropboxException {
        upload(String.format(COVER_PATH, isbn, "jpeg"), file);
    }

    public void upload(String targetPath, File file) throws DropboxException {
        new UploadAsync(targetPath, file).start();
    }

    public void deleteCover(String isbn) throws DropboxException {
        new DeleteAsync(String.format(COVER_PATH, isbn, "jpeg")).start();
    }

    public boolean isConnected() {
        try {
            return ACCESS_TOKEN_FILE.exists() && getClient() != null;
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

    public boolean hasLinkedAccount() {
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

    public AccountInfo getAccountInfo() throws DropboxException {
        try {
            return AccountInfoFactory.getAccountInfo(getClient().getAccountInfo());
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

    class UploadAsync extends Service<Void> {

        private String targetPath;
        private File file;

        public UploadAsync(String targetPath, File file) {
            this.targetPath = targetPath;
            this.file = file;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        getClient().uploadFile(targetPath, DbxWriteMode.force(), file.length(), new FileInputStream(file));
                        LOGGER.debug("File uploaded {}", targetPath);
                    } catch (DbxException | IOException | JsonReader.FileLoadException e) {
                        throw new DropboxException(String.format("Unable to upload \"%s\" to target path \"%s\".", file.getAbsolutePath(), targetPath), e);
                    }
                    return null;
                }
            };
        }
    }

    class DeleteAsync extends Service<Void> {

        private String targetPath;

        public DeleteAsync(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        getClient().delete(targetPath);
                        LOGGER.debug("File deleted {}", targetPath);
                    } catch (DbxException | JsonReader.FileLoadException e) {
                        throw new DropboxException(String.format("Unable to delete file to target path \"%s\".", targetPath), e);
                    }
                    return null;
                }
            };
        }
    }
}
