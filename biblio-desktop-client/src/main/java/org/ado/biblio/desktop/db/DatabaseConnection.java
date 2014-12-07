package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.AppConfiguration;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.util.DateUtils;
import org.apache.commons.io.FileUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class DatabaseConnection {

    private static final File DATABASE_FILE = new File(AppConfiguration.APP_CONFIG_DIRECTORY, "biblio.db");
    private Connection connection;

    @PostConstruct
    private void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            if (!DATABASE_FILE.exists()) {
                FileUtils.touch(DATABASE_FILE);
            }
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", DATABASE_FILE.getAbsolutePath()));

            initializeDatabase();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to establish connection to database", e);
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
        String query = "SELECT id, title, author, ctime, isbn, tags FROM Book";
        final List<Book> bookList = new ArrayList<>();

        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            bookList.add(new Book(resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("isbn"),
                    DateUtils.parseSqlite(resultSet.getString("ctime")),
                    resultSet.getString("tags")));
        }

        return bookList;
    }

    private void initializeDatabase() throws SQLException {
//        executeQuery("CREATE TABLE IF NOT EXISTS [Table 1] (id INTEGER PRIMARY KEY AUTOINCREMENT, 'text column' TEXT, 'int column' INTEGER);");
        executeQuery(new StringBuilder().append("CREATE TABLE IF NOT EXISTS [Book]")
                .append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("'title' TEXT,")
                .append("'author' TEXT,")
                .append("'ctime' DATETIME DEFAULT CURRENT_TIMESTAMP,")
                .append("'isbn' TEXT,")
                .append("'tags' TEXT DEFAULT '');")
                .toString());
    }

    private void executeQuery(String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private List<String> getTagList(String tagsString) {
        return Arrays.asList(tagsString.split(","));
    }

    private String getTagListString(List<String> tagList) {

        final StringBuilder stringBuilder = new StringBuilder();
        for (String tag : tagList) {
            stringBuilder.append(tag).append(",");
        }
        return stringBuilder.toString();
    }
}
