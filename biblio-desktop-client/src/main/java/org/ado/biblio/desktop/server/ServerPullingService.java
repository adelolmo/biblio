package org.ado.biblio.desktop.server;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.desktop.AppConfiguration;
import org.ado.biblio.domain.BookMessageDTO;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ServerPullingService extends Service<BookMessageDTO[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerPullingService.class);
    private static final String SERVER_PULL_URL = "%s/books/%s";
    private String clientId;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    protected Task<BookMessageDTO[]> createTask() {

        return new Task<BookMessageDTO[]>() {
            @Override
            protected BookMessageDTO[] call() throws Exception {

                final String requestUrl = String.format(SERVER_PULL_URL, AppConfiguration.getConfigurationProperty("server.url"), clientId);

                try {
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(requestUrl);
                    HttpResponse response = client.execute(request);

                    LOGGER.info("Response code [{}]", response.getStatusLine().getStatusCode());

                    String responseContent = IOUtils.toString(response.getEntity().getContent());
                    final BookMessageDTO[] bookMessageDTOs = new Gson().fromJson(responseContent, BookMessageDTO[].class);
                    return bookMessageDTOs;

                } catch (Exception e) {
                    LOGGER.error(String.format("Unable to pull server %s", requestUrl), e);
                    pause(15);
                    throw e;
                }
            }
        };
    }

    private void pause(int seconds) {
        try {
            Thread.currentThread().sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
