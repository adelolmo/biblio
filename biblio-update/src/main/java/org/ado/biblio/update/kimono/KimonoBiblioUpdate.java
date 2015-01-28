package org.ado.biblio.update.kimono;

import org.ado.biblio.update.BiblioUpdate;
import org.ado.biblio.update.ComponentEnum;
import org.ado.biblio.update.Release;
import org.ado.biblio.update.kimono.json.KimonoArtifact;
import org.ado.biblio.update.kimono.json.KimonoRelease;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoBiblioUpdate implements BiblioUpdate {

    private KimonoBiblioApi kimonoBiblioApi;

    public KimonoBiblioUpdate() {
        kimonoBiblioApi = new KimonoBiblioApi();
    }

    public Release getLatestRelease() throws IOException {
        return getRelease(kimonoBiblioApi.getLatestRelease());
    }

    private Release getRelease(KimonoRelease kimonoRelease) {
        final Release release = new Release();
        release.setName(kimonoRelease.getDescription().get(0).getName().getText());
        final String versionName = getVersion(kimonoRelease.getDescription().get(0).getName().getHref());
        release.setVersionName(versionName);
        release.setVersionMayor(getVersionMayor(versionName));
        release.setVersionMinor(getVersionMinor(versionName));
        release.setArtifactUrl(getArtifactMap(kimonoRelease.getArtifactList()));
        release.setReleaseNotes(kimonoRelease.getDescription().get(0).getReleaseNotes());
        return release;
    }

    private Map<ComponentEnum, String> getArtifactMap(List<KimonoArtifact> artifactList) {
        final Map<ComponentEnum, String> artifactsMap = new HashMap<>();
        for (KimonoArtifact kimonoArtifact : artifactList) {
            artifactsMap.put(getComponent(kimonoArtifact.getArtifact().getHref()), kimonoArtifact.getArtifact().getHref());
        }
        return artifactsMap;
    }

    private int getVersionMayor(String versionName) {
        return Integer.valueOf(versionName.substring(0, versionName.indexOf(".")));
    }

    private int getVersionMinor(String versionName) {
        return Integer.valueOf(versionName.substring(versionName.indexOf(".") + 1));
    }

    private ComponentEnum getComponent(String text) {
        for (ComponentEnum componentEnum : ComponentEnum.values()) {
            if (text.substring(text.lastIndexOf("/") + 1).startsWith(componentEnum.getCodeName())) {
                return componentEnum;
            }
        }
        throw new IllegalStateException(String.format("Component not found \"%s\".", text));
    }

    private String getVersion(String href) {
        return href.substring(href.lastIndexOf("-") + 1);
    }
}