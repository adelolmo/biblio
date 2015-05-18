package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.model.LendBook;
import org.ado.biblio.desktop.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class DatabaseManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
    private static final int DATABASE_VERSION = 1;

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void upgradeDatabase() throws SQLException {
        DatabaseInitialize databaseInitialize = new DatabaseInitialize(connection, DATABASE_VERSION);
        databaseInitialize.initializeDatabase();
    }

    @PreDestroy
    private void destroy() {
        try {
            close();
        } catch (SQLException e) {
            LOGGER.error("Cannot close connection to DB", e);
        }
    }

    public void close() throws SQLException {
        LOGGER.info("close db connection.");
        if (connection != null) {
            connection.close();
        }
    }

    public Book insertBook(Book book) throws SQLException {
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
        final ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            final int id = resultSet.getInt(1);
            return new Book(id, book.getTitle(), book.getAuthor(), book.getIsbn(), new java.util.Date(), book.getTags());
        }
        return null;
    }

    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE Book SET title=?, author=?, isbn=?, tags=? WHERE id=?";
        final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getIsbn());
        statement.setString(4, book.getTags());
        statement.setInt(5, book.getId());
        final int i = statement.executeUpdate();
        if (i == 0) {
            throw new SQLException("Update book failed, no rows affected.");
        }
    }

    public void deleteBook(Integer bookId) throws SQLException {
        String query = "DELETE FROM Book WHERE id=?";
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        final int i = preparedStatement.executeUpdate();
    }

    public List<Book> getBookList() throws SQLException {
        String query = "SELECT Book.id, Book.title, Book.author, Book.ctime, Book.isbn, Book.tags," +
                "(SELECT max(Lend.id) FROM Lend WHERE Book.id = Lend.bookId AND Lend.rtime IS NULL) AS lent FROM Book ";
        final List<Book> bookList = new ArrayList<>();

        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            bookList.add(getBook(resultSet));
        }

        return bookList;
    }

    public List<LendBook> getLentBookList() throws SQLException {
        String query = "SELECT Book.id, Book.title, Book.author, Book.ctime, Book.isbn, Book.tags," +
                "(SELECT Lend.person FROM Lend WHERE Book.id = Lend.bookId AND Lend.rtime IS NULL GROUP BY Lend.id LIMIT 1) AS person FROM Book " +
                "WHERE person IS NOT NULL";
        final List<LendBook> lendBookList = new ArrayList<>();

        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            lendBookList.add(getLentBook(resultSet));
        }
        return lendBookList;
    }

    public Book returnBook(int bookId, Date rtime) throws SQLException {
        String query = "UPDATE Lend SET rtime=? WHERE bookId=?;";
        final PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, DateUtils.formatSqlite(rtime));
        statement.setInt(2, bookId);
        final int i = statement.executeUpdate();
        if (i == 0) {
            throw new SQLException("Return book failed, no rows affected.");
        }
        return getBook(bookId);
    }

    public Book lendBook(int bookId, String person, Date ctime) throws SQLException {
        String query = "INSERT INTO Lend (bookId, person, ctime) VALUES (?,?,?);";
        final PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, bookId);
        statement.setString(2, person);
        statement.setString(3, DateUtils.formatSqlite(ctime));
        final int i = statement.executeUpdate();
        if (i == 0) {
            throw new SQLException("Lend book failed, no rows affected.");
        }
        return getBook(bookId);
    }

    private Book getBook(int id) throws SQLException {
        String query = "SELECT Book.id, Book.title, Book.author, Book.ctime, Book.isbn, Book.tags," +
                " (SELECT max(Lend.id) FROM Lend WHERE Book.id = Lend.bookId AND Lend.rtime IS NULL) AS lent" +
                " FROM Book WHERE Book.id = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            return getBook(resultSet);
        }
        return null;
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

    private LendBook getLentBook(ResultSet resultSet) throws SQLException {
        return new LendBook(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("isbn"),
                DateUtils.parseSqlite(resultSet.getString("ctime")),
                resultSet.getString("tags"),
                resultSet.getString("person")
        );
    }


    private boolean isBookLent(int lent) {
        return lent > 0;
    }
}
