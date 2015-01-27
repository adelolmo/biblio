package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoRelease {

    private Link artifact;

    @SerializedName("release-name")
    private Link releaseName;

    public Link getArtifact() {
        return artifact;
    }

    public void setArtifact(Link artifact) {
        this.artifact = artifact;
    }

    public Link getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(Link releaseName) {
        this.releaseName = releaseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KimonoRelease that = (KimonoRelease) o;

        if (artifact != null ? !artifact.equals(that.artifact) : that.artifact != null) return false;
        if (releaseName != null ? !releaseName.equals(that.releaseName) : that.releaseName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = artifact != null ? artifact.hashCode() : 0;
        result = 31 * result + (releaseName != null ? releaseName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KimonoRelease{");
        sb.append("artifact=").append(artifact);
        sb.append(", releaseName=").append(releaseName);
        sb.append('}');
        return sb.toString();
    }
}