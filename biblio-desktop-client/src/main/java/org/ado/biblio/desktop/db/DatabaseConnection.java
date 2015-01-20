package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.AppConfiguration;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.util.DateUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class DatabaseConnection {

    private static final File DATABASE_FILE = new File(AppConfiguration.APP_CONFIG_DIRECTORY, "biblio.db");
    private static final int DATABASE_VERSION = 1;
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);
    private Connection connection;
    private DatabaseInitialize databaseInitialize;

    @PostConstruct
    private void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            if (!DATABASE_FILE.exists()) {
                FileUtils.touch(DATABASE_FILE);
            }
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", DATABASE_FILE.getAbsolutePath()));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to establish connection to database", e);
        }

        try {
            databaseInitialize = new DatabaseInitialize(connection, DATABASE_VERSION);
            databaseInitialize.initializeDatabase();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to initialize database", e);
        }
    }

    @PreDestroy
    private void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        String query = "SELECT Book.id, Book.title, Book.author, Book.ctime, Book.isbn, Book.tags, Lend.ctime AS lendctime, Lend.rtime FROM Book " +
                "LEFT OUTER JOIN Lend " +
                "ON Book.id = Lend.bookId " +
                "WHERE Lend.rtime IS NULL";
        final List<Book> bookList = new ArrayList<>();

        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            bookList.add(getBook(resultSet));
        }

        return bookList;
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
        String query = "INSERT INTO Lend (bookId, person, ctime) values (?,?, ?);";
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
        String query = "SELECT Book.id, Book.title, Book.author, Book.ctime, Book.isbn, Book.tags, Lend.ctime AS lendctime, Lend.rtime FROM Book " +
                "LEFT OUTER JOIN Lend " +
                "ON Book.id = Lend.bookId " +
                "WHERE Book.id = ?";
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
                isBookLent(resultSet.getString("lendctime"), resultSet.getString("rtime")));
    }

    private boolean isBookLent(String ctime, String rtime) throws SQLException {
        return ctime != null && rtime == null;
    }
}
