package org.ado.biblio.update;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class Release {

    private ComponentEnum component; // desktop-client
    private String versionName; // 1.2
    private int versionMayor; // 1
    private int versionMinor; // 2
    private String artifactUrl; // https://github.com/adelolmo/biblio/releases/download/biblio-1.0/desktop-client-1.0-dist.zip
    private String name; // Desktop Client Release 1.0

    public ComponentEnum getComponent() {
        return component;
    }

    public void setComponent(ComponentEnum component) {
        this.component = component;
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

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Release release = (Release) o;

        if (versionMayor != release.versionMayor) return false;
        if (versionMinor != release.versionMinor) return false;
        if (artifactUrl != null ? !artifactUrl.equals(release.artifactUrl) : release.artifactUrl != null) return false;
        if (component != release.component) return false;
        if (name != null ? !name.equals(release.name) : release.name != null) return false;
        if (versionName != null ? !versionName.equals(release.versionName) : release.versionName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = component != null ? component.hashCode() : 0;
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        result = 31 * result + versionMayor;
        result = 31 * result + versionMinor;
        result = 31 * result + (artifactUrl != null ? artifactUrl.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Release{");
        sb.append("component=").append(component);
        sb.append(", versionName='").append(versionName).append('\'');
        sb.append(", versionMayor=").append(versionMayor);
        sb.append(", versionMinor=").append(versionMinor);
        sb.append(", artifactUrl='").append(artifactUrl).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}