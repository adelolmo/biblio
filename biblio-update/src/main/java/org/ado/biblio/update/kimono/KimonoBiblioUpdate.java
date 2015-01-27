package org.ado.biblio.update.kimono;

import org.ado.biblio.update.BiblioUpdate;
import org.ado.biblio.update.ComponentEnum;
import org.ado.biblio.update.Release;
import org.ado.biblio.update.kimono.json.KimonoRelease;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoBiblioUpdate implements BiblioUpdate {

    private KimonoBiblioApi kimonoBiblioApi;

    public KimonoBiblioUpdate() {
        kimonoBiblioApi = new KimonoBiblioApi();
    }

    @Override
    public List<Release> getReleases(ComponentEnum component) {
        return kimonoBiblioApi.getReleases().stream()
                .filter(r -> r.getArtifact().getText().startsWith(component.getCodeName()))
                .map(this::getRelease)
                .collect(Collectors.toList());
    }

    @Override
    public List<Release> getReleases() {
        return kimonoBiblioApi.getReleases().stream()
                .map(this::getRelease)
                .collect(Collectors.toList());
    }

    private Release getRelease(KimonoRelease kimonoRelease) {
        final Release release = new Release();
        release.setArtifactUrl(kimonoRelease.getArtifact().getHref());
        release.setComponent(getComponent(kimonoRelease.getArtifact().getText()));
        release.setName(kimonoRelease.getReleaseName().getText());
        final String versionName = getVersion(kimonoRelease.getReleaseName().getHref());
        release.setVersionName(versionName);
        release.setVersionMayor(getVersionMayor(versionName));
        release.setVersionMinor(getVersionMinor(versionName));
        return release;
    }

    private int getVersionMayor(String versionName) {
        return Integer.valueOf(versionName.substring(0, versionName.indexOf(".")));
    }

    private int getVersionMinor(String versionName) {
        return Integer.valueOf(versionName.substring(versionName.indexOf(".") + 1));
    }

    private ComponentEnum getComponent(String text) {
        for (ComponentEnum componentEnum : ComponentEnum.values()) {
            if (text.startsWith(componentEnum.getCodeName())) {
                return componentEnum;
            }
        }
        throw new IllegalStateException(String.format("Component not found \"%s\".", text));
    }

    private String getVersion(String href) {
        return href.substring(href.lastIndexOf("-") + 1);
    }
}