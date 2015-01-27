package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class Result {

    @SerializedName("releases")
    private List<KimonoRelease> releaseList;

    public List<KimonoRelease> getReleaseList() {
        return releaseList;
    }

    public void setReleaseList(List<KimonoRelease> releaseList) {
        this.releaseList = releaseList;
    }
}