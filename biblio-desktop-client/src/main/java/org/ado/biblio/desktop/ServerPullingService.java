package org.ado.biblio.desktop;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ado.biblio.domain.BookMessageDTO;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class ServerPullingService extends Service<BookMessageDTO[]> {

    private static final String SERVER_PULL_URL = "http://localhost:8080/pull?id=%s";
    private final Logger LOGGER = LoggerFactory.getLogger(ServerPullingService.class);
    private String hostId;

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    @Override
    protected Task<BookMessageDTO[]> createTask() {

        return new Task<BookMessageDTO[]>() {
            @Override
            protected BookMessageDTO[] call() throws Exception {
                try {

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(String.format(SERVER_PULL_URL, hostId));
                    HttpResponse response = client.execute(request);

                    LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());

                    String responseContent = IOUtils.toString(response.getEntity().getContent());
                    final BookMessageDTO[] bookMessageDTOs = new Gson().fromJson(responseContent, BookMessageDTO[].class);
                    return bookMessageDTOs;

                } catch (Exception e) {
                    LOGGER.error("Unable to pull server {}", SERVER_PULL_URL, e);
                    pause(15);
                }
                return null;
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
