package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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