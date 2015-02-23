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
 * @author Andoni del Olmo,
 * @since 29.01.15
 */
public class LendBook implements BookDetails {

    private IntegerProperty id;
    private StringProperty title;
    private StringProperty author;
    private StringProperty isbn;
    private ObservableValue<Date> creation;
    private StringProperty tags;
    private StringProperty person;

    public LendBook(Integer id, String title, String author, String isbn, Date creation, String tags, String person) {
        if (id != null) {
            this.id = new SimpleIntegerProperty(id);
        }
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.creation = new SimpleObjectProperty<>(creation);
        this.tags = new SimpleStringProperty(tags);
        this.person = new SimpleStringProperty(person);
    }

    public int getId() {
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

    public StringProperty isbnProperty() {
        return isbn;
    }

    public Date getCreation() {
        return creation.getValue();
    }

    public StringProperty creationProperty() {
        if (creation != null && creation.getValue() != null) {
            return new SimpleStringProperty(DateUtils.format(creation.getValue()));
        } else {
            return null;
        }
    }

    public StringProperty tagsProperty() {
        return tags;
    }

    public String getPerson() {
        return person.get();
    }

    public StringProperty personProperty() {
        return person;
    }

    @Override
    public String getTags() {
        return null;
    }

    @Override
    public boolean isLent() {
        return true;
    }

}