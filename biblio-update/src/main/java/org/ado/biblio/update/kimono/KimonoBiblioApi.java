package org.ado.biblio.update.kimono;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ado.biblio.update.kimono.json.KimonoRelease;
import org.ado.biblio.update.kimono.json.KimonoResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoBiblioApi {

    private static final String KIMONO_URL = "https://www.kimonolabs.com/api/2fwithpm?apikey=a506e75ca96092b0f73b0ff59c15abe6";

    public List<KimonoRelease> getReleases() {
        try {
            final KimonoResponse kimonoResponse = getKimonoResponse();
            return kimonoResponse.getResult().getReleaseList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private KimonoResponse getKimonoResponse() throws IOException {
        final Gson gson = new GsonBuilder().create();
        return gson.fromJson(IOUtils.toString(new URL(KIMONO_URL).openStream()), KimonoResponse.class);
    }
}