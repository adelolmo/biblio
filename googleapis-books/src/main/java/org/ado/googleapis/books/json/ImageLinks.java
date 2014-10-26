package org.ado.googleapis.books.json;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("org.ado.googleapis.books.json.ImageLinks{");
        sb.append("smallThumbnail='").append(smallThumbnail).append('\'');
        sb.append(", thumbnail='").append(thumbnail).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
