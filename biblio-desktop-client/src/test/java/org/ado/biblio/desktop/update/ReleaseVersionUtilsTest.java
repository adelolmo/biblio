package org.ado.biblio.desktop.update;

import org.ado.biblio.update.Release;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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