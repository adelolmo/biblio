package org.ado.biblio.update;

/**
 * @author Andoni del Olmo,
 * @since 29.01.15
 */
public class Artifact {

    private String url;
    private long size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artifact artifact = (Artifact) o;

        if (size != artifact.size) return false;
        if (url != null ? !url.equals(artifact.url) : artifact.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Artifact{");
        sb.append("url='").append(url).append('\'');
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}