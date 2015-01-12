package org.ado.googleapis.books.json;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class Item {

    private String kind;
    private String id;
    private String etag;
    private String selfLink;
    private VolumeInfo volumeInfo;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("org.ado.googleapis.books.json.Item{");
        sb.append("kind='").append(kind).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", etag='").append(etag).append('\'');
        sb.append(", selfLink='").append(selfLink).append('\'');
        sb.append(", volumeInfo=").append(volumeInfo);
        sb.append('}');
        return sb.toString();
    }
}
