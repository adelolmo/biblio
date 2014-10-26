package org.ado.biblio.desktop.booksapi;

import java.util.List;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class Volumes {

    private String kind;
    private int totalItems;
    private List<Item> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Volumes{");
        sb.append("kind='").append(kind).append('\'');
        sb.append(", totalItems=").append(totalItems);
        sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}
