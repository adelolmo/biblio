package org.ado.biblio.domain;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class BookMessageDTO {

    private String format;
    private String code;

    public BookMessageDTO(String format, String code) {
        this.format = format;
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BookMessageDTO{");
        sb.append("format='").append(format).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
