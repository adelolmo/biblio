package org.ado.biblio.update.kimono;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.ado.biblio.update.BiblioUpdateException;
import org.ado.biblio.update.kimono.json.KimonoRelease;
import org.ado.biblio.update.kimono.json.KimonoResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoBiblioApi {

    private static final String KIMONO_URL = "https://www.kimonolabs.com/api/2fwithpm?apikey=a506e75ca96092b0f73b0ff59c15abe6&kimmodify=1";

    public KimonoRelease getLatestRelease() throws BiblioUpdateException {
        return getKimonoResponse().getKimonoRelease();
    }

    private KimonoResponse getKimonoResponse() throws BiblioUpdateException {
        String jsonResponse = null;
        try {
            final Gson gson = new GsonBuilder().create();
            jsonResponse = IOUtils.toString(new URL(KIMONO_URL).openStream());
            return gson.fromJson(jsonResponse, KimonoResponse.class);

        } catch (IOException e) {
            throw new BiblioUpdateException("Cannot establish connection to Kimonolabs server", e);
        } catch (JsonSyntaxException e) {
            throw new BiblioUpdateException(String.format("Unable to parse latest release details from Kimonolabs response: %s", jsonResponse), e);
        }
    }
}