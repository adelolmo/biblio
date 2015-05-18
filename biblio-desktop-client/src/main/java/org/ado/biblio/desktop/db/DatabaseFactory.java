package org.ado.biblio.desktop.db;

import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.NoAccountDropboxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.sql.SQLException;

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
 * @since 29.04.15
 */
public class DatabaseFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFactory.class);

    @Inject
    private DropboxManager dropboxManager;

    public DatabaseManager create(File databaseFile) throws SQLException {
        return create(databaseFile, false);
    }

    public DatabaseManager create(File databaseFile, boolean download) throws SQLException {
        LOGGER.info("create db connection to [{}]. download [{}].", databaseFile.getAbsolutePath(), download);
        if (download) {
            downloadDatabase(databaseFile);
        }
        final DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setConnection(DatabaseConnection.getConnection(databaseFile));
        databaseManager.upgradeDatabase();
        return databaseManager;
    }

    private void downloadDatabase(File databaseFile) {
        try {
            dropboxManager.downloadSync(databaseFile.getName(), databaseFile);
        } catch (DropboxException e) {
            throw new IllegalStateException("Unable to download database", e);
        } catch (NoAccountDropboxException e) {
            LOGGER.debug("Can't download database. {}", e.getMessage());
        }
    }
}