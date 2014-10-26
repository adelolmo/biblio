package org.ado.biblio.desktop.booksapi;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class IndustryIdentifier {
    private IndustryIdentifierTypeEnum type;
    private String identifier;

    public IndustryIdentifierTypeEnum getType() {
        return type;
    }

    public void setType(IndustryIdentifierTypeEnum type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndustryIdentifier{");
        sb.append("type=").append(type);
        sb.append(", identifier='").append(identifier).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
