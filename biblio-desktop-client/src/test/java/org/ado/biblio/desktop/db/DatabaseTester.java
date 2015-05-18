package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
 * @author Andoni del Olmo
 * @since 12.05.15
 */
public class DatabaseTester {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseTester.class);

    private static DatabaseTester databaseTester;
    private File databaseFile;
    private List<Book> bookList;
    private int size;

    private DatabaseTester() {
        bookList = new ArrayList<>();
        size = -1;
    }

    public static DatabaseTester create() {
        databaseTester = new DatabaseTester();
        return databaseTester;
    }

    public DatabaseTester setDatabaseFile(File databaseFile) {
        this.databaseFile = databaseFile;
        return this;
    }

    public DatabaseTester withBook(Book bookOne) {
        bookList.add(bookOne);
        return this;
    }

    public DatabaseTester withNumberOfBooks(int size) {
        this.size = size;
        return this;
    }

    public void populate() throws SQLException {
        final Connection connection = DatabaseConnection.getConnection(databaseFile);
        new DatabaseInitialize(connection, 1).initializeDatabase();
        for (Book book : bookList) {
            insertBook(connection, book);
        }
        connection.close();
    }

    public void check() throws SQLException {
        final Connection connection = DatabaseConnection.getConnection(databaseFile);

        if (size > -1) {
            assertEquals("number of books", size, getBookCount(connection));
        }

        for (Book book : bookList) {
            assertNotNull(String.format("book with isbn %s found", book.getIsbn()), findBookByIsbn(connection, book.getIsbn()));
        }
        connection.close();
    }

    private int getBookCount(Connection connection) throws SQLException {
        String query = "SELECT COUNT(Book.id) FROM Book";
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    private Book findBookByIsbn(Connection connection, String isbn) throws SQLException {
        String query = "SELECT Book.id, Book.title, Book.author, Book.ctime, Book.isbn, Book.tags," +
                "(SELECT max(Lend.id) FROM Lend WHERE Book.id = Lend.bookId AND Lend.rtime IS NULL) AS lent FROM Book " +
                "WHERE isbn = ?";

        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, isbn);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return getBook(resultSet);
        }

        return null;
    }

    private void insertBook(Connection connection, Book book) throws SQLException {
        String query = "INSERT INTO Book (title, author, isbn, tags) VALUES (?,?,?,?)";
        final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getIsbn());
        statement.setString(4, book.getTags());
        final int i = statement.executeUpdate();
        if (i == 0) {
            throw new SQLException("Update book failed, no rows affected.");
        }
//        final ResultSet resultSet = statement.getGeneratedKeys();
//        if (resultSet.next()) {
//            final int id = resultSet.getInt(1);
//            return new Book(id, book.getTitle(), book.getAuthor(), book.getIsbn(), new java.util.Date(), book.getTags());
//        }
//        return null;
    }

    private Book getBook(ResultSet resultSet) throws SQLException {
        return new Book(resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("isbn"),
                DateUtils.parseSqlite(resultSet.getString("ctime")),
                resultSet.getString("tags"),
                isBookLent(resultSet.getInt("lent")));
    }

    private boolean isBookLent(int lent) {
        return lent > 0;
    }
}
