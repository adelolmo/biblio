package org.ado.biblio.desktop;

import com.sun.istack.internal.NotNull;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class HttpImageLoader {

    public InputStream getImage(@NotNull String imageUrl) throws IOException {
        return HttpClientBuilder.create().build()
                .execute(new HttpGet(imageUrl)).getEntity().getContent();
    }
}
