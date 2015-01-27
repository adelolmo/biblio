package org.ado.biblio.desktop.update;

import org.ado.biblio.update.Release;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ReleaseVersionUtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetLatestRelease() throws Exception {
        final List<Release> releaseList = new ArrayList<>();
        releaseList.addAll(Arrays.asList(
                createRelease("1.2", 1, 2),
                createRelease("2.0", 2, 0),
                createRelease("1.3", 1, 3)));

        final Release release = ReleaseVersionUtils.getLatestRelease(releaseList);

        assertEquals("version name", "2.0", release.getVersionName());
        assertEquals("version mayor", 2, release.getVersionMayor());
        assertEquals("version minor", 0, release.getVersionMinor());
    }

    @Test
    public void testUpdateAvailable_false() {
        assertFalse("no update available", ReleaseVersionUtils.updateAvailable(createRelease("1.2", 1, 2), 1, 2));
    }

    @Test
    public void testUpdateAvailable_true() {
        assertTrue("update available", ReleaseVersionUtils.updateAvailable(createRelease("1.2", 1, 2), 1, 1));
    }

    private Release createRelease(String versionName, int versionMayor, int versionMinor) {
        final Release release = new Release();
        release.setVersionName(versionName);
        release.setVersionMayor(versionMayor);
        release.setVersionMinor(versionMinor);
        return release;
    }
}