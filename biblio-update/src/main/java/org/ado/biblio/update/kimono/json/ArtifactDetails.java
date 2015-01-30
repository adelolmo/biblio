package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class ArtifactDetails {

    private String href;
    private String text;
    @SerializedName("size")
    private String sizeString;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSizeString() {
        return sizeString;
    }

    public void setSizeString(String sizeString) {
        this.sizeString = sizeString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtifactDetails that = (ArtifactDetails) o;

        if (href != null ? !href.equals(that.href) : that.href != null) return false;
        if (sizeString != null ? !sizeString.equals(that.sizeString) : that.sizeString != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = href != null ? href.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (sizeString != null ? sizeString.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArtifactDetails{");
        sb.append("href='").append(href).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", sizeString='").append(sizeString).append('\'');
        sb.append('}');
        return sb.toString();
    }
}