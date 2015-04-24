package org.ado.biblio.desktop.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.Locale;

import static org.ado.biblio.desktop.AppConfiguration.APP_CONFIG_DIRECTORY;

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
 * Class to provide access to dropbox.
 *
 * @author andoni
 * @since 05.11.2014
 */
public class DropboxManager {

    private static final File ACCESS_TOKEN_FILE = new File(APP_CONFIG_DIRECTORY, ".dropbox_access_token");
    private static final String COVER_PATH = "/covers/%s.%s";
    private static final DbxRequestConfig DROPBOX_CONFIG = new DbxRequestConfig("Biblio Data/1.0", Locale.getDefault().toString());
    private final Logger LOGGER = LoggerFactory.getLogger(DropboxManager.class);
    private DbxClient dbxClient;

    @PostConstruct
    public void init() throws DropboxException {
        LOGGER.info("Initializing");
    }

    @PreDestroy
    public void cleanup() {
        LOGGER.info("Cleaning up!");
    }

    public void uploadCover(String isbn, File file) throws DropboxException, NoAccountDropboxException {
        uploadAsync(String.format(COVER_PATH, isbn, "jpeg"), file);
    }

    public void deleteCover(String isbn) throws DropboxException, NoAccountDropboxException {
        deleteAsync(String.format(COVER_PATH, isbn, "jpeg"));
    }

    public void uploadAsync(String remotePath, File file) throws DropboxException, NoAccountDropboxException {
        if (isAccountLinked()) {
            new UploadAsync(remotePath, file).start();
        } else {
            throw new NoAccountDropboxException();
        }
    }

    public void uploadSync(String remotePath, File file) throws DropboxException, NoAccountDropboxException {
        if (isAccountLinked()) {
            try {
                getClient().uploadFile(getRemotePath(remotePath), DbxWriteMode.force(), file.length(), new FileInputStream(file));
                LOGGER.debug("File uploaded {}", remotePath);
            } catch (Exception e) {
                throw new DropboxException(String.format("Cannot upload file \"%s\"", remotePath), e);
            }
        } else {
            throw new NoAccountDropboxException();
        }
    }

    private void deleteAsync(String remotePath) throws NoAccountDropboxException {
        if (isAccountLinked()) {
            new DeleteAsync(remotePath).start();
        } else {
            throw new NoAccountDropboxException();
        }
    }

    public File downloadSync(String remotePath, File localFile) throws DropboxException, NoAccountDropboxException {
        if (isAccountLinked()) {
            try {
                final OutputStream out = new FileOutputStream(localFile);
                final DbxEntry.File dbxFile = getClient().getFile(getRemotePath(remotePath), null, out);
                return localFile;
            } catch (Exception e) {
                throw new DropboxException(String.format("Cannot download file \"%s\"", remotePath), e);
            }

        } else {
            throw new NoAccountDropboxException();
        }
    }

    public void downloadAsync(String remotePath, File localFile, EventHandler<WorkerStateEvent> onSucceeded) throws DropboxException {
        final DownloadAsync downloadAsync = new DownloadAsync(getRemotePath(remotePath), localFile);
        downloadAsync.start();
        downloadAsync.setOnSucceeded(onSucceeded);
    }

    public boolean isConnected() {
        try {
            return isAccountLinked() && getClient() != null;
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

    public boolean isAccountLinked() {
        return ACCESS_TOKEN_FILE.exists() && ACCESS_TOKEN_FILE.isFile() && ACCESS_TOKEN_FILE.length() > 0;
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

    private DbxClient getClient() throws JsonReader.FileLoadException {
        if (dbxClient == null) {
            DbxAuthInfo authInfo = DbxAuthInfo.Reader.readFromFile(ACCESS_TOKEN_FILE);
            return new DbxClient(DROPBOX_CONFIG, authInfo.accessToken, authInfo.host);
        } else {
            return dbxClient;
        }
    }

    private String getRemotePath(String remotePath) {
        return remotePath.startsWith("/") ? remotePath : "/" + remotePath;
    }

    public interface DropboxAccountLinkListener {
        void accountLinked(AccountInfo accountInfo);
    }

    class UploadAsync extends Service<Void> {

        private String remotePath;
        private File file;

        public UploadAsync(String remotePath, File file) {
            this.remotePath = remotePath;
            this.file = file;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        getClient().uploadFile(remotePath, DbxWriteMode.force(), file.length(), new FileInputStream(file));
                        LOGGER.debug("File uploaded {}", remotePath);
                    } catch (DbxException | IOException | JsonReader.FileLoadException e) {
                        throw new DropboxException(String.format("Unable to upload \"%s\" to localFile remotePath \"%s\".", file.getAbsolutePath(), remotePath), e);
                    }
                    return null;
                }
            };
        }
    }

    class DeleteAsync extends Service<Void> {

        private String remotePath;

        public DeleteAsync(String remotePath) {
            this.remotePath = remotePath;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        getClient().delete(remotePath);
                        LOGGER.debug("File deleted {}", remotePath);
                    } catch (DbxException | JsonReader.FileLoadException e) {
                        throw new DropboxException(String.format("Unable to delete file in remotePath \"%s\".", remotePath), e);
                    }
                    return null;
                }
            };
        }
    }

    private class DownloadAsync extends Service<File> {

        private String remotePath;
        private File localFile;

        public DownloadAsync(String remotePath, File localFile) {
            this.remotePath = remotePath;
            this.localFile = localFile;
        }

        @Override
        protected Task<File> createTask() {
            return new Task<File>() {
                @Override
                protected File call() throws Exception {
                    try {
                        final OutputStream out = new FileOutputStream(localFile);
                        final DbxEntry.File dbxFile = getClient().getFile(remotePath, null, out);
                        return localFile;
                    } catch (DbxException | IOException | JsonReader.FileLoadException e) {
                        throw new DropboxException(
                                String.format("Unable to download remote file \"%s\" into localFile \"%s\".", remotePath, localFile.getAbsolutePath()), e);
                    }

                }
            };
        }
    }
}
