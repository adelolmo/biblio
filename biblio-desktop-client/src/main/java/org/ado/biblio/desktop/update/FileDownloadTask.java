package org.ado.biblio.desktop.update;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
 * https://code.google.com/p/jfxee/source/browse/trunk/jfxee8/src/main/java/com/zenjava/download/FileDownloadTask.java
 */
public class FileDownloadTask extends Task<File> {

    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Logger LOGGER = LoggerFactory.getLogger(FileDownloadTask.class);
    private HttpClient httpClient;
    private String remoteUrl;
    private File localFile;
    private int bufferSize;

    public FileDownloadTask(String remoteUrl, File localFile) {
        this(new DefaultHttpClient(), remoteUrl, localFile, DEFAULT_BUFFER_SIZE);
    }

    public FileDownloadTask(HttpClient httpClient, String remoteUrl, File localFile, int bufferSize) {
        this.httpClient = httpClient;
        this.remoteUrl = remoteUrl;
        this.localFile = localFile;
        this.bufferSize = bufferSize;

        stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue<? extends State> source, State oldState, State newState) {
                if (newState.equals(State.SUCCEEDED)) {
                    onSuccess();
                } else if (newState.equals(State.FAILED)) {
                    onFailed();
                }
            }
        });
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public File getLocalFile() {
        return localFile;
    }

    protected File call() throws Exception {
        LOGGER.info(String.format("Downloading file %s to %s", remoteUrl, localFile.getAbsolutePath()));

        HttpGet httpGet = new HttpGet(this.remoteUrl);
        HttpResponse response = httpClient.execute(httpGet);
        InputStream remoteContentStream = response.getEntity().getContent();
        OutputStream localFileStream = null;
        try {
            long fileSize = response.getEntity().getContentLength();
            LOGGER.debug(String.format("Size of file to download is %s", fileSize));

            localFileStream = new FileOutputStream(localFile);
            byte[] buffer = new byte[bufferSize];
            int sizeOfChunk;
            int amountComplete = 0;
            while ((sizeOfChunk = remoteContentStream.read(buffer)) != -1) {
                localFileStream.write(buffer, 0, sizeOfChunk);
                amountComplete += sizeOfChunk;
                updateProgress(amountComplete, fileSize);
                LOGGER.info(String.format("Downloaded %s of %s bytes (%d) for file",
                        amountComplete, fileSize, (int) ((double) amountComplete / (double) fileSize * 100.0)));
            }
            LOGGER.info(String.format("Downloading of file %s to %s completed successfully", remoteUrl, localFile.getAbsolutePath()));
            return localFile;
        } finally {
            remoteContentStream.close();
            if (localFileStream != null) {
                localFileStream.close();
            }
        }
    }

    private void onFailed() {
        LOGGER.error("File download failed: ", getException());
    }

    private void onSuccess() {
    }
}