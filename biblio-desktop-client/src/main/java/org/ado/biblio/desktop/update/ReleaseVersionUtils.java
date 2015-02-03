package org.ado.biblio.desktop.update;

import org.ado.biblio.update.Release;

import java.util.List;

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