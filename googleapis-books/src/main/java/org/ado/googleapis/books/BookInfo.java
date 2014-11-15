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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookInfo bookInfo = (BookInfo) o;

        if (hasSmallThumbnail != bookInfo.hasSmallThumbnail) return false;
        if (hasThumbnail != bookInfo.hasThumbnail) return false;
        if (author != null ? !author.equals(bookInfo.author) : bookInfo.author != null) return false;
        if (isbn != null ? !isbn.equals(bookInfo.isbn) : bookInfo.isbn != null) return false;
        if (smallThumbnail != null ? !smallThumbnail.equals(bookInfo.smallThumbnail) : bookInfo.smallThumbnail != null)
            return false;
        if (thumbnail != null ? !thumbnail.equals(bookInfo.thumbnail) : bookInfo.thumbnail != null) return false;
        if (title != null ? !title.equals(bookInfo.title) : bookInfo.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (hasThumbnail ? 1 : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (hasSmallThumbnail ? 1 : 0);
        result = 31 * result + (smallThumbnail != null ? smallThumbnail.hashCode() : 0);
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BookInfo{");
        sb.append("author='").append(author).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", hasThumbnail=").append(hasThumbnail);
        sb.append(", thumbnail=").append(thumbnail);
        sb.append(", hasSmallThumbnail=").append(hasSmallThumbnail);
        sb.append(", smallThumbnail=").append(smallThumbnail);
        sb.append(", isbn='").append(isbn).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
