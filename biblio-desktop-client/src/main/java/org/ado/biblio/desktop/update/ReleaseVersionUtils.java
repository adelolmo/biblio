package org.ado.biblio.desktop.update;

import org.ado.biblio.update.Release;

import java.util.List;

/**
 * @author Andoni del Olmo,
 * @since 27.01.15
 */
public class ReleaseVersionUtils {

    public static Release getLatestRelease(List<Release> releaseList) {
        Release tempRelease = new Release();
        tempRelease.setVersionMayor(0);
        tempRelease.setVersionMinor(0);

        for (Release release : releaseList) {
            if (release.getVersionMayor() > tempRelease.getVersionMayor()
                    || (release.getVersionMayor() == tempRelease.getVersionMayor() && release.getVersionMinor() > tempRelease.getVersionMinor())) {
                tempRelease = release;
            }
        }
        return tempRelease;
    }

    public static boolean updateAvailable(Release release, int currentVersionMayor, int currentVersionMinor) {
        return release.getVersionMayor() > currentVersionMayor
                || (release.getVersionMayor() == currentVersionMayor && release.getVersionMinor() > currentVersionMinor);

    }
}