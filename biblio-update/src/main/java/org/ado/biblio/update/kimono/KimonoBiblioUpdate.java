package org.ado.biblio.update.kimono;

import org.ado.biblio.update.*;
import org.ado.biblio.update.kimono.json.ArtifactDetails;
import org.ado.biblio.update.kimono.json.KimonoArtifact;
import org.ado.biblio.update.kimono.json.KimonoRelease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.FileUtils.ONE_KB;
import static org.apache.commons.io.FileUtils.ONE_MB;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoBiblioUpdate implements BiblioUpdate {

    private KimonoBiblioApi kimonoBiblioApi;

    public KimonoBiblioUpdate() {
        kimonoBiblioApi = new KimonoBiblioApi();
    }

    public Release getLatestRelease() throws BiblioUpdateException {
        return getRelease(kimonoBiblioApi.getLatestRelease());
    }

    private Release getRelease(KimonoRelease kimonoRelease) throws BiblioUpdateException {
        try {
            final Release release = new Release();
            release.setName(kimonoRelease.getDescription().get(0).getName());
            final String versionName = getVersion(kimonoRelease.getDescription().get(0).getName());
            release.setVersionName(versionName);
            release.setVersionMayor(getVersionMayor(versionName));
            release.setVersionMinor(getVersionMinor(versionName));
            release.setArtifactUrl(getArtifactMap(kimonoRelease.getArtifactList()));
            release.setReleaseNotes(kimonoRelease.getDescription().get(0).getReleaseNotes());
            return release;
        } catch (Exception e) {
            throw new BiblioUpdateException("Cannot create release details from the Kimonolabs response", e);
        }
    }

    private Map<ComponentEnum, Artifact> getArtifactMap(List<KimonoArtifact> artifactList) {
        final Map<ComponentEnum, Artifact> artifactsMap = new HashMap<>();
        for (KimonoArtifact kimonoArtifact : artifactList) {
            artifactsMap.put(getComponent(kimonoArtifact.getArtifact().getHref()), getArtifact(kimonoArtifact.getArtifact()));
        }
        return artifactsMap;
    }

    private Artifact getArtifact(ArtifactDetails artifactDetails) {
        final Artifact artifact = new Artifact();
        artifact.setUrl(artifactDetails.getHref());
        artifact.setSize(getSize(artifactDetails.getSizeString()));
        return artifact;
    }

    private long getSize(String sizeString) {
        final String[] split = sizeString.split(" ");
        final Double size = Double.valueOf(split[0]);
        final String units = split[1];

        long format = 0;
        if ("KB".equalsIgnoreCase(units)) {
            format = ONE_KB;
        } else if ("MB".equalsIgnoreCase(units)) {
            format = ONE_MB;
        }

        return (long) (size * format);
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
        return href.substring(href.lastIndexOf(" ") + 1);
    }
}