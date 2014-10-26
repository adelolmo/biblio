package org.ado.biblio.desktop;

import org.ado.googleapis.AbstractBookInfoLoader;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Class description here.
 *
 * @author andoni
 * @since 26.10.2014
 */
public class BookInfoLoader extends AbstractBookInfoLoader {

    @Override
    public HttpClient getHttpClient() {
        return HttpClientBuilder.create().build();
    }
}
