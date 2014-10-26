package org.ado.biblio;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class BookMessage {

    private String format;
    private String code;

    public BookMessage(String format, String code) {
        this.format = format;
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BookMessage{");
        sb.append("format='").append(format).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
