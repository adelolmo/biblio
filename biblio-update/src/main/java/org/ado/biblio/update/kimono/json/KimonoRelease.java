package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

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
 * @since 25.01.15
 */
public class KimonoRelease {

    @SerializedName("artifacts")
    private List<KimonoArtifact> artifactList;

    @SerializedName("description")
    private List<KimonoDescription> description;

    public List<KimonoArtifact> getArtifactList() {
        return artifactList;
    }

    public void setArtifactList(List<KimonoArtifact> artifactList) {
        this.artifactList = artifactList;
    }

    public List<KimonoDescription> getDescription() {
        return description;
    }

    public void setDescription(List<KimonoDescription> description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KimonoRelease kimonoRelease = (KimonoRelease) o;

        if (description != null ? !description.equals(kimonoRelease.description) : kimonoRelease.description != null)
            return false;
        if (artifactList != null ? !artifactList.equals(kimonoRelease.artifactList) : kimonoRelease.artifactList != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = artifactList != null ? artifactList.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("artifactList=").append(artifactList);
        sb.append(", description=").append(description);
        sb.append('}');
        return sb.toString();
    }
}