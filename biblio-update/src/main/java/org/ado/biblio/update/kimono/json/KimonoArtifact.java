package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andoni del Olmo,
 * @since 28.01.15
 */
public class KimonoArtifact {

    @SerializedName("artifact")
    private ArtifactDetails artifact;

    public ArtifactDetails getArtifact() {
        return artifact;
    }

    public void setArtifact(ArtifactDetails artifact) {
        this.artifact = artifact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KimonoArtifact that = (KimonoArtifact) o;

        if (artifact != null ? !artifact.equals(that.artifact) : that.artifact != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return artifact != null ? artifact.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KimonoArtifact{");
        sb.append("artifact=").append(artifact);
        sb.append('}');
        return sb.toString();
    }
}