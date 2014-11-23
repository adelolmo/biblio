package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.AppConfiguration;
import org.ado.biblio.desktop.model.Book;
import org.apache.commons.io.FileUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
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

    public void insertBook(Book book) throws SQLException {
        String query = "INSERT INTO Book (title, author, isbn) VALUES (?,?,?)";
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, book.getAuthor());
        preparedStatement.setString(3, book.getIsbn());
        final int i = preparedStatement.executeUpdate();
        System.out.println(i);
    }

    public void insertBook(String title, String author) throws SQLException {
        String query = "INSERT INTO Book (title, author) VALUES (?,?)";
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, author);
        final int i = preparedStatement.executeUpdate();
        System.out.println(i);
    }

    public List<Book> getBookList() throws SQLException {
        String query = "SELECT title, author, isbn FROM Book";
        final ArrayList<Book> bookList = new ArrayList<>();

        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            bookList.add(new Book(resultSet.getString("title"), resultSet.getString("author"), resultSet.getString("isbn")));
        }

        return bookList;
    }

    private void initializeDatabase() throws SQLException {
//        executeQuery("CREATE TABLE IF NOT EXISTS [Table 1] (id INTEGER PRIMARY KEY AUTOINCREMENT, 'text column' TEXT, 'int column' INTEGER);");
        executeQuery(new StringBuilder().append("CREATE TABLE IF NOT EXISTS [Book]")
                .append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("'title' TEXT,")
                .append("'author' TEXT,")
                .append("'isbn' TEXT);")
                .toString());
    }

    private void executeQuery(String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
}
