package org.ado.biblio.desktop.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author Andoni del Olmo,
 * @since 19.01.15
 */
public class DatabaseInitialize {

    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitialize.class);

    private Connection connection;

    private UpgradeDatabase upgradeDatabase;
    private int databaseVersion;

    public DatabaseInitialize(Connection connection, int databaseVersion) {
        this.connection = connection;
        this.upgradeDatabase = new UpgradeDatabase(connection);
        this.databaseVersion = databaseVersion;
    }

    public void initializeDatabase() throws SQLException {
        createSchema();
        upgradeSchema();
    }

    private void createSchema() throws SQLException {
        executeQuery(new StringBuilder().append("CREATE TABLE IF NOT EXISTS [Book]")
                .append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("'title' TEXT,")
                .append("'author' TEXT,")
                .append("'ctime' DATETIME DEFAULT CURRENT_TIMESTAMP,")
                .append("'isbn' TEXT,")
                .append("'tags' TEXT DEFAULT '');")
                .toString());
        executeQuery("CREATE TABLE  IF NOT EXISTS Lend(" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "bookId INTEGER NOT NULL, " +
                "person TEXT NOT NULL, " +
                "ctime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                "rtime DATETIME DEFAULT NULL, " + // return timestamp
                "FOREIGN KEY(bookId) REFERENCES Book(id));");
        executeQuery("CREATE TABLE IF NOT EXISTS System (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'key' TEXT NOT NULL UNIQUE, " +
                "'value' TEXT NOT NULL" +
                ");");
        executeQuery("INSERT OR IGNORE INTO System (key, value) VALUES ('dbversion', '1');");
    }

    private void upgradeSchema() throws SQLException {
        final int currentDatabaseVersion = getCurrentDatabaseVersion();
        LOGGER.info("Current DB version {}", currentDatabaseVersion);

        if (currentDatabaseVersion < databaseVersion) {
            LOGGER.info("DB needs upgrade to version {}", databaseVersion);

            int index = currentDatabaseVersion;
            while (index < databaseVersion) {
                upgradeDatabase.upgradeToVersion(++index);
            }
            updateCurrentDatabaseVersion(databaseVersion);
        }

    }

    private int getCurrentDatabaseVersion() throws SQLException {
        final String query = "SELECT value FROM System WHERE key = 'dbversion'";
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            return Integer.valueOf(resultSet.getString("value"));
        }
        return -1;
    }

    private void updateCurrentDatabaseVersion(int databaseVersion) throws SQLException {

        String query = "UPDATE System SET value=? WHERE key='dbversion';";
        final PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, String.valueOf(databaseVersion));
        final int i = statement.executeUpdate();
        if (i == 0) {
            throw new SQLException("Return book failed, no rows affected.");
        }
    }

    private void executeQuery(String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
}