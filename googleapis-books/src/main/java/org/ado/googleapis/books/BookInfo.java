package org.ado.googleapis.books;

import java.io.InputStream;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class BookInfo {

    private String author;
    private String title;
    private boolean hasThumbnail;
    private InputStream thumbnail;
    private boolean hasSmallThumbnail;
    private InputStream smallThumbnail;
    private String isbn;

    public BookInfo() {
        hasThumbnail = false;
        hasSmallThumbnail = false;
    }

    public BookInfo(String title, String author) {
        this();
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasThumbnail() {
        return hasThumbnail;
    }

    public InputStream getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(InputStream inputStream) {
        this.thumbnail = inputStream;
        if (inputStream != null) {
            hasThumbnail = true;
        }
    }

    public boolean hasSmallThumbnail() {
        return hasSmallThumbnail;
    }

    public InputStream getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(InputStream inputStream) {
        this.smallThumbnail = inputStream;
        if (inputStream != null) {
            hasSmallThumbnail = true;
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}
