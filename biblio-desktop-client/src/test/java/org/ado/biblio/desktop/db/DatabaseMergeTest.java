package org.ado.biblio.desktop.db;

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

import org.ado.biblio.desktop.InjectMock;
import org.ado.biblio.desktop.InjectTestCase;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.model.Book;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.sql.SQLException;
import java.util.Locale;

import static org.ado.biblio.desktop.util.DateUtils.parseSqlite;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Andoni del Olmo
 * @since 30.04.15
 */
public class DatabaseMergeTest extends InjectTestCase<DatabaseMerge> {

    private static final String LOCAL_DATABASE_FILENAME = "local_biblio.db";
    private static final String REMOTE_DATABASE_FILENAME = "remote_biblio.db";
    private static final Book BOOK_ONE = new Book(1, "The Book One", "Andoni", "1234", parseSqlite("2015-02-01 08:00:00"), "tag1, tag2");
    private static final Book BOOK_TWO = new Book(2, "Apocalipsis Maya", "Steven Alter", "1111", parseSqlite("2015-02-02 08:00:00"), "");
    private static final Book BOOK_THREE = new Book(3, "Summer of Night", "Dan Simmons", "2222", parseSqlite("2015-02-03 08:00:00"), "horror");

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @InjectMock
    private DropboxManager dropboxManagerMock;

    @InjectMock
    private DatabaseFactory databaseFactoryMock;

    private File localDatabaseFile;
    private File remoteDatabaseFile;

    public void setUp() throws Exception {
        localDatabaseFile = new File(temporaryFolder.getRoot(), LOCAL_DATABASE_FILENAME);
        remoteDatabaseFile = new File(temporaryFolder.getRoot(), REMOTE_DATABASE_FILENAME);
        Locale.setDefault(Locale.ENGLISH);

    }

    private void createDabataseConnections() throws SQLException {
        final DatabaseFactory databaseFactory = new DatabaseFactory();
        final DatabaseManager localDatabaseManager = databaseFactory.create(localDatabaseFile);
        DatabaseContext.setDatabaseManager(localDatabaseManager);

        when(databaseFactoryMock.create(any(File.class))).thenReturn(databaseFactory.create(remoteDatabaseFile));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMergeBooks_newRemoteBooks_noConflict() throws SQLException {
        DatabaseTester.create().setDatabaseFile(localDatabaseFile).withBook(BOOK_ONE).populate();
        DatabaseTester.create().setDatabaseFile(remoteDatabaseFile).withBook(BOOK_TWO).populate();
        createDabataseConnections();

        unitUnderTest.mergeBooks();

        DatabaseTester.create().setDatabaseFile(localDatabaseFile).withBook(BOOK_ONE).withBook(BOOK_TWO).withNumberOfBooks(2).check();
        DatabaseTester.create().setDatabaseFile(remoteDatabaseFile).withBook(BOOK_TWO).withNumberOfBooks(1).check();
    }

    @Test
    public void testMergeBooks_newRemoteBooks_conflict() throws SQLException {
        DatabaseTester.create().setDatabaseFile(localDatabaseFile).withBook(BOOK_ONE).withBook(BOOK_TWO).populate();
        DatabaseTester.create().setDatabaseFile(remoteDatabaseFile).withBook(BOOK_TWO).populate();
        createDabataseConnections();

        unitUnderTest.mergeBooks();

        DatabaseTester.create().setDatabaseFile(localDatabaseFile).withBook(BOOK_ONE).withBook(BOOK_TWO).withNumberOfBooks(2).check();
        DatabaseTester.create().setDatabaseFile(remoteDatabaseFile).withBook(BOOK_TWO).withNumberOfBooks(1).check();
    }

    @Test
    public void testMergeBooks_noNewRemoteBooks() throws SQLException {
        DatabaseTester.create().setDatabaseFile(localDatabaseFile).withBook(BOOK_ONE).populate();
        createDabataseConnections();

        unitUnderTest.mergeBooks();

        DatabaseTester.create().setDatabaseFile(localDatabaseFile).withBook(BOOK_ONE).withNumberOfBooks(1).check();
        DatabaseTester.create().setDatabaseFile(remoteDatabaseFile).withNumberOfBooks(0).check();
    }
}