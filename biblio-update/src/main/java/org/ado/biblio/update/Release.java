package org.ado.biblio.update;

import java.util.Map;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class Release {

    private String name; // Desktop Client Release 1.0
    private String versionName; // 1.2
    private int versionMayor; // 1
    private int versionMinor; // 2
    private Map<ComponentEnum, Artifact> artifactUrl; // https://github.com/adelolmo/biblio/releases/download/biblio-1.0/desktop-client-1.0-dist.zip
    private String releaseNotes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionMayor() {
        return versionMayor;
    }

    public void setVersionMayor(int versionMayor) {
        this.versionMayor = versionMayor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }

    public Map<ComponentEnum, Artifact> getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(Map<ComponentEnum, Artifact> artifactUrl) {
        this.artifactUrl = artifactUrl;
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

        Release release = (Release) o;

        if (versionMayor != release.versionMayor) return false;
        if (versionMinor != release.versionMinor) return false;
        if (artifactUrl != null ? !artifactUrl.equals(release.artifactUrl) : release.artifactUrl != null) return false;
        if (name != null ? !name.equals(release.name) : release.name != null) return false;
        if (releaseNotes != null ? !releaseNotes.equals(release.releaseNotes) : release.releaseNotes != null)
            return false;
        if (versionName != null ? !versionName.equals(release.versionName) : release.versionName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        result = 31 * result + versionMayor;
        result = 31 * result + versionMinor;
        result = 31 * result + (artifactUrl != null ? artifactUrl.hashCode() : 0);
        result = 31 * result + (releaseNotes != null ? releaseNotes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Release{");
        sb.append("name='").append(name).append('\'');
        sb.append(", versionName='").append(versionName).append('\'');
        sb.append(", versionMayor=").append(versionMayor);
        sb.append(", versionMinor=").append(versionMinor);
        sb.append(", artifactUrl=").append(artifactUrl);
        sb.append(", releaseNotes='").append(releaseNotes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}