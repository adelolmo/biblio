package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andoni del Olmo,
 * @since 28.01.15
 */
public class KimonoDescription {

    @SerializedName("name")
    private Link name;

    @SerializedName("release-notes")
    private String releaseNotes;

    public Link getName() {
        return name;
    }

    public void setName(Link name) {
        this.name = name;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KimonoDescription that = (KimonoDescription) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (releaseNotes != null ? !releaseNotes.equals(that.releaseNotes) : that.releaseNotes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (releaseNotes != null ? releaseNotes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KimonoDescription{");
        sb.append("name=").append(name);
        sb.append(", releaseNotes='").append(releaseNotes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}