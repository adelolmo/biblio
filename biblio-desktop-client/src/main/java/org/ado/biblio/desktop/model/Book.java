package org.ado.biblio.desktop.model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import org.ado.biblio.desktop.util.DateUtils;

import java.util.Date;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class Book implements BookDetails {
    private IntegerProperty id;
    private StringProperty title;
    private StringProperty author;
    private StringProperty isbn;
    private ObservableValue<Date> creation;
    private String tags;
    private BooleanProperty lent;

    public Book(String title, String author, String isbn, Date creation, String tags) {
        this(null, title, author, isbn, creation, tags);
    }

    public Book(Integer id, String title, String author, String isbn, Date creation, String tags) {
        this(id, title, author, isbn, creation, tags, false);
    }

    public Book(Integer id, String title, String author, String isbn, Date creation, String tags, boolean lent) {
        if (id != null) {
            this.id = new SimpleIntegerProperty(id);
        }
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.creation = new SimpleObjectProperty<>(creation);
        this.tags = tags;
        this.lent = new SimpleBooleanProperty(lent);
    }

    public int getId() {
        if (id == null) {
            return -1;
        }
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public String getTags() {
        return tags;
    }

    public boolean getLent() {
        return lent.get();
    }

    public BooleanProperty lentProperty() {
        return lent;
    }

    @Override
    public boolean isLent() {
        return getLent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (author != null ? !author.equals(book.author) : book.author != null) return false;
        if (creation != null ? !creation.equals(book.creation) : book.creation != null) return false;
        if (id != null ? !id.equals(book.id) : book.id != null) return false;
        if (isbn != null ? !isbn.equals(book.isbn) : book.isbn != null) return false;
        if (lent != null ? !lent.equals(book.lent) : book.lent != null) return false;
        if (tags != null ? !tags.equals(book.tags) : book.tags != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        result = 31 * result + (creation != null ? creation.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (lent != null ? lent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", author=").append(author);
        sb.append(", isbn=").append(isbn);
        sb.append(", creation=").append(creation);
        sb.append(", tags='").append(tags).append('\'');
        sb.append(", lent=").append(lent);
        sb.append('}');
        return sb.toString();
    }

    public StringProperty creationProperty() {
        if (creation != null && creation.getValue() != null) {
            return new SimpleStringProperty(DateUtils.format(creation.getValue()));
        } else {
            return null;
        }
    }

}
