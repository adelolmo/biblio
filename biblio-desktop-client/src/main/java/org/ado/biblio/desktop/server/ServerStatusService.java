package org.ado.biblio.desktop.server;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.desktop.AppConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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