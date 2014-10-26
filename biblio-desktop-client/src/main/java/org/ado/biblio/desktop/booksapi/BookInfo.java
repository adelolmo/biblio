package org.ado.biblio.desktop.booksapi;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class BookInfo {

    private String author;
    private String title;
    private String imageUrl;
    private boolean hasImage = false;
    private String isbn;

    public BookInfo() {
    }

    public BookInfo(String title, String author) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        hasImage = true;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BookInfo{");
        sb.append("author='").append(author).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", hasImage=").append(hasImage);
        sb.append(", isbn='").append(isbn).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
