package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.AppConfiguration;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.NoAccountDropboxException;
import org.ado.biblio.desktop.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static org.ado.biblio.desktop.AppConfiguration.DATABASE_FILE;

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
 * @since 27.04.15
 */
public class DatabaseMerge {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMerge.class);

    @Inject
    private DropboxManager dropboxManager;

    @Inject
    private DatabaseFactory databaseFactory;

    public void mergeBooks() throws SQLException {

        final List<Book> localBookList = getLocalBookList();
        final List<Book> remoteBookList = getRemoteBookList();

        copyRemoteBookToLocal(localBookList, remoteBookList);
        reconnectDatabase();
    }

    // ----- INTERNAL HELPER ---------------------------------------------------------------------------------------- //

    private void copyRemoteBookToLocal(List<Book> localBookList, List<Book> remoteBookList) throws SQLException {
        for (Book remoteBook : remoteBookList) {
            if (!containsBook(localBookList, remoteBook)) {
                DatabaseContext.getDatabaseManager().insertBook(remoteBook);
            }
        }
    }

    private void reconnectDatabase() throws SQLException {
        LOGGER.info("reconnect local db");
        DatabaseContext.getDatabaseManager().close();
        DatabaseContext.setDatabaseManager(databaseFactory.create(AppConfiguration.DATABASE_FILE));
    }

    private boolean containsBook(List<Book> localBookList, Book remoteBook) {
        for (Book book : localBookList) {
            if (StringUtils.equalsIgnoreCase(book.getIsbn(), remoteBook.getIsbn())) {
                return true;
            }
        }
        return false;
    }

    private List<Book> getLocalBookList() throws SQLException {
        LOGGER.info("local db");
        final DatabaseManager databaseManager = DatabaseContext.getDatabaseManager();
        return databaseManager.getBookList();
    }

    private List<Book> getRemoteBookList() throws SQLException {
        LOGGER.info("remote db");
        DatabaseManager remoteDatabaseManager = null;
        try {
            remoteDatabaseManager = databaseFactory.create(downloadRemoteDatabase());
            return remoteDatabaseManager.getBookList();
        } finally {
            if (remoteDatabaseManager != null) {
                remoteDatabaseManager.close();
            }
        }
    }

    private File downloadRemoteDatabase() {
        try {
            return dropboxManager.downloadSync(DATABASE_FILE.getName());
        } catch (DropboxException | NoAccountDropboxException e) {
            throw new IllegalStateException("Unable to download database", e);
        }
    }

}