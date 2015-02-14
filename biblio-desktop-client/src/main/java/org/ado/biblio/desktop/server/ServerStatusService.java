package org.ado.biblio.desktop.server;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.desktop.AppConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
 * @author Andoni del Olmo,
 * @since 06.01.15
 */
public class ServerStatusService extends Service<ServerStatusEnum> {

    private static final String SERVER_URL = "%s/server/%s";
    private final Logger LOGGER = LoggerFactory.getLogger(ServerStatusService.class);

    @Override
    protected Task<ServerStatusEnum> createTask() {
        return new Task<ServerStatusEnum>() {
            @Override
            protected ServerStatusEnum call() throws Exception {
                final String requestUrl = String.format(SERVER_URL, AppConfiguration.getConfigurationProperty("server.url"), "status");
                try {
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(requestUrl);
                    HttpResponse response = client.execute(request);

                    LOGGER.info("Response code [{}]", response.getStatusLine().getStatusCode());
                    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        return ServerStatusEnum.OFFLINE;
                    }
                    return getServerStatus(IOUtils.toString(response.getEntity().getContent()));
                } catch (IOException | IllegalStateException e) {
                    return ServerStatusEnum.OFFLINE;
                }
            }

            private ServerStatusEnum getServerStatus(String responseContent) {
                return new Gson().fromJson(responseContent, ServerStatusMessage.class).getServerStatusEnum();
            }
        };
    }
}