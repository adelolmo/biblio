package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.InjectTestCase;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.model.LendBook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import static org.ado.biblio.desktop.util.DateUtils.parseSqlite;
import static org.junit.Assert.assertEquals;

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

public class DatabaseManagerTest extends InjectTestCase<DatabaseManager> {

    private static final Book BOOK_ONE = new Book(1, "The Book One", "Andoni", "1234", parseSqlite("2015-02-01 08:00:00"), "tag1, tag2");
    private static final Book BOOK_TWO = new Book(2, "Apocalipsis Maya", "Steven Alter", "1111", parseSqlite("2015-02-02 08:00:00"), "");
    private static final Book BOOK_THREE = new Book(3, "Summer of Night", "Dan Simmons", "2222", parseSqlite("2015-02-03 08:00:00"), "horror");

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    public void setUp() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        final Connection connection = DatabaseConnection.getConnection(temporaryFolder.newFile("biblio.db"));
        unitUnderTest.setConnection(connection);
        unitUnderTest.upgradeDatabase();
    }

    @Test
    public void testGetBookList() throws Exception {
        createUsecase();

        final List<Book> bookList = unitUnderTest.getBookList();

        assertEqualsBook("book one", BOOK_ONE, bookList.get(0), false);
        assertEqualsBook("book two", BOOK_TWO, bookList.get(1), true);
        assertEqualsBook("book three", BOOK_THREE, bookList.get(2), false);
    }

    @Test
    public void testGetLentBookList() throws Exception {
        createUsecase();

        final List<LendBook> lentBookList = unitUnderTest.getLentBookList();

        assertEquals("number of lent books", 1, lentBookList.size());
        assertEqualsLendBook("lend book two", createLendBook(BOOK_TWO, "Juan"), lentBookList.get(0));
    }

    private void createUsecase() throws SQLException {
        unitUnderTest.insertBook(BOOK_ONE);
        unitUnderTest.insertBook(BOOK_TWO);
        unitUnderTest.insertBook(BOOK_THREE);
        unitUnderTest.lendBook(1, "Alberto", parseSqlite("2015-02-19 08:00:00"));
        unitUnderTest.returnBook(1, parseSqlite("2015-02-20 08:00:00"));
        unitUnderTest.lendBook(1, "Michael", parseSqlite("2015-02-21 10:00:00"));
        unitUnderTest.returnBook(1, parseSqlite("2015-02-22 10:00:00"));
        unitUnderTest.lendBook(2, "Juan", parseSqlite("2015-02-23 09:00:00"));
    }

    private LendBook createLendBook(Book book, String person) {
        return new LendBook(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), parseSqlite(book.creationProperty().get()), book.getTags(), person);
    }

    private void assertEqualsBook(String comment, Book expectedBook, Book currentBook, boolean lent) {
        assertEquals(comment.concat(" id"), expectedBook.getId(), currentBook.getId());
        assertEquals(comment.concat(" author"), expectedBook.getAuthor(), currentBook.getAuthor());
        assertEquals(comment.concat(" title"), expectedBook.getTitle(), currentBook.getTitle());
        assertEquals(comment.concat(" isbn"), expectedBook.getIsbn(), currentBook.getIsbn());
        assertEquals(comment.concat(" tags"), expectedBook.getTags(), currentBook.getTags());
        assertEquals(comment.concat(" lent"), lent, currentBook.isLent());
    }

    private void assertEqualsLendBook(String comment, LendBook expectedBook, LendBook currentBook) {
        assertEquals(comment.concat(" id"), expectedBook.getId(), currentBook.getId());
        assertEquals(comment.concat(" author"), expectedBook.getAuthor(), currentBook.getAuthor());
        assertEquals(comment.concat(" title"), expectedBook.getTitle(), currentBook.getTitle());
        assertEquals(comment.concat(" isbn"), expectedBook.getIsbn(), currentBook.getIsbn());
        assertEquals(comment.concat(" tags"), expectedBook.getTags(), currentBook.getTags());
    }
}