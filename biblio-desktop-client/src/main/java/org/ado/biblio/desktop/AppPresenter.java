package org.ado.biblio.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.ado.biblio.desktop.about.AboutPresenter;
import org.ado.biblio.desktop.about.AboutView;
import org.ado.biblio.desktop.android.AndroidView;
import org.ado.biblio.desktop.db.DatabaseConnection;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.DropboxPresenter;
import org.ado.biblio.desktop.dropbox.DropboxView;
import org.ado.biblio.desktop.lend.LendPresenter;
import org.ado.biblio.desktop.lend.LendView;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.model.LendBook;
import org.ado.biblio.desktop.returnbook.ReturnBookPresenter;
import org.ado.biblio.desktop.returnbook.ReturnBookView;
import org.ado.biblio.desktop.server.ServerPullingService;
import org.ado.biblio.desktop.server.ServerStatusEnum;
import org.ado.biblio.desktop.server.ServerStatusService;
import org.ado.biblio.desktop.settings.SettingsPresenter;
import org.ado.biblio.desktop.settings.SettingsView;
import org.ado.biblio.desktop.update.UpdateManager;
import org.ado.biblio.desktop.update.UpdateService;
import org.ado.biblio.desktop.util.ImageUtils;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.BookInfo;
import org.ado.googleapis.books.NoBookInfoFoundException;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
 * @since 25.10.2014
 */
public class AppPresenter implements Initializable, LendPresenter.LendBookListener, ReturnBookPresenter.ReturnBookListener {

    private final Logger LOGGER = LoggerFactory.getLogger(AppPresenter.class);
    private final ObservableList<Book> bookData = FXCollections.observableArrayList();
    private final ObservableList<LendBook> lendBookData = FXCollections.observableArrayList();

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<Book> tableViewBooks;

    @FXML
    private TableColumn<Book, String> tableColumnId;

    @FXML
    private TableColumn<Book, String> tableColumnAuthor;

    @FXML
    private TableColumn<Book, String> tableColumnTitle;

    @FXML
    private TableColumn<Book, String> tableColumnCreation;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextField textFieldAuthor;

    @FXML
    private TextField textFieldIsbn;

    @FXML
    private TextField textFieldTags;

    @FXML
    private ImageView imageViewCover;

    @FXML
    private Button buttonReturnBook;

    @FXML
    private Button buttonLend;

    @FXML
    private ImageView imageViewServerStatus;

    @FXML
    private Label labelSystem;

    @Inject
    private BookInfoLoader bookInfoLoader;

    @Inject
    private ServerPullingService serverPullingService;

    @Inject
    private ServerStatusService serverStatusService;

    @Inject
    private UpdateService updateService;

    @Inject
    private DatabaseConnection databaseConnection;

    @Inject
    private DropboxManager dropboxManager;

    @Inject
    private UpdateManager updateManager;

    private Stage stage;
    private Integer bookId;
    private int bookFocusedIndex;

    @PostConstruct
    public void init() throws Exception {
        LOGGER.info("PostConstruct...");
        serverPullingService.setClientId(AppConfiguration.getAppId());
        reloadBooksTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("initializing...");

        updateService.setOnSucceeded(updateManager.getOnSucceeded());
        updateService.setOnFailed(updateManager.getOnFailed());
        updateService.start();

        tableColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        tableColumnTitle.setCellFactory(new Callback<TableColumn<Book, String>, TableCell<Book, String>>() {
            @Override
            public TableCell<Book, String> call(TableColumn<Book, String> param) {
                return new TableCell<Book, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        getTableRow().getStyleClass().remove("bookLent");

                        if (item != null) {
                            setText(item);
                            final boolean lent = isBookLend();
                            if (lent) {
                                getTableRow().getStyleClass().add("bookLent");
                            }
                        }
                    }

                    private boolean isBookLend() {
                        final TableRow tableRow = getTableRow();
                        if (tableRow == null) {
                            return false;
                        }
                        final Object item = tableRow.getItem();
                        if (item == null) {
                            LOGGER.warn("Unexpected null in table row \"{}\".", "");
                            return false;
                        }
                        return ((Book) item).getLent();
                    }
                };
            }
        });
        tableColumnAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        tableColumnCreation.setCellValueFactory(cellData -> cellData.getValue().creationProperty());

        tableViewBooks.setItems(bookData);
        buttonLend.setDisable(true);
        buttonReturnBook.setDisable(true);

        serverStatusService.setOnSucceeded(event -> {
            final ServerStatusEnum serverStatus = (ServerStatusEnum) event.getSource().getValue();
            switch (serverStatus) {
                case ONLINE:
                    serverStatusImageOnline();
                    break;
                case OFFLINE:
                    serverStatusImageOffline();
                    break;
            }
        });
        serverStatusService.start();

        serverPullingService.setOnSucceeded(event -> {
            LOGGER.info("Succeeded");
            serverStatusImageOnline();
            BookMessageDTO[] bookMessages = (BookMessageDTO[]) event.getSource().getValue();
            if (bookMessages != null) {
                for (BookMessageDTO bookMessage : bookMessages) {
                    LOGGER.info("processing book [{}]", bookMessage);
                    try {
                        addBook(bookInfoLoader.getBookInfo(bookMessage));
                        labelSystem.setText(null);

                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                        labelSystem.setText(e.getMessage());
                    } catch (NoBookInfoFoundException e) {
                        LOGGER.error(e.getMessage());
                        labelSystem.setText(e.getMessage());
                    }
                }
            }
            serverPullingService.restart();

        });
        serverPullingService.setOnFailed(event -> {
            LOGGER.error(event.getSource().getMessage());
            serverStatusImageOffline();
            serverPullingService.restart();
        });
        serverPullingService.start();

        tableViewBooks.setOnMouseClicked(this::loadBookDetails);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @PreDestroy
    private void destroy() {
        LOGGER.info("destroy");
        serverPullingService.cancel();
    }

    public void linkAndroid() {
        final AndroidView androidView = new AndroidView();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(androidView.getView()));
        stage.setTitle("Android");
        stage.show();
    }

    public void linkDropbox() throws DropboxException {
        Stage stage = new Stage();
        DropboxView dropboxView = new DropboxView();
        final DropboxPresenter dropboxPresenter = (DropboxPresenter) dropboxView.getPresenter();
        dropboxPresenter.setStage(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(dropboxView.getView()));
        stage.setTitle("Dropbox");
        stage.show();
    }

    public void settings() {
        Stage stage = new Stage();
        final SettingsView settingsView = new SettingsView();
        final SettingsPresenter presenter = (SettingsPresenter) settingsView.getPresenter();
        presenter.setStage(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(settingsView.getView()));
        stage.setTitle("Settings");
        stage.show();
    }

    public void exit() {
        LOGGER.info("exit");
        stage.close();
    }

    public void about() {
        LOGGER.info("about");
        Stage stage = new Stage();
        final AboutView aboutView = new AboutView();
        final AboutPresenter presenter = (AboutPresenter) aboutView.getPresenter();
        presenter.setStage(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(aboutView.getView()));
        stage.setTitle("About");
        stage.show();
    }

    public void search(Event event) throws SQLException {
        LOGGER.info("search");
        String searchSequence = textFieldSearch.getCharacters().toString();
        LOGGER.debug(searchSequence);

        reloadBooksTable();
        if (!StringUtils.isBlank(searchSequence)) {
            reloadBooksTable();
            bookData.removeIf(new Predicate<Book>() {
                @Override
                public boolean test(Book book) {

                    final boolean containsMatch = book.getTitle().toLowerCase().contains(searchSequence)
                            || book.getAuthor().toLowerCase().contains(searchSequence)
                            || book.getTags().toLowerCase().contains(searchSequence);

                    return !containsMatch;
                }
            });
        }
    }

    public void clear() throws SQLException {
        LOGGER.info("clear");
        textFieldSearch.clear();
        reloadBooksTable();
    }

    public void save() throws SQLException {
        LOGGER.info("save");
        Collections.addAll(new ArrayList<>(), textFieldTags.getText().split(","));
        if (bookId != null) {
            final Book book = new Book(bookId, textFieldTitle.getText(), textFieldAuthor.getText(), textFieldIsbn.getText(), new Date(), textFieldTags.getText());
            databaseConnection.updateBook(book);
            bookData.set(bookFocusedIndex, book);
        } else {
            final Book book = databaseConnection
                    .insertBook(new Book(textFieldTitle.getText(), textFieldAuthor.getText(), textFieldIsbn.getText(), new Date(), textFieldTags.getText()));
            bookData.add(book);
        }
    }

    public void add() {
        LOGGER.info("add");
        resetBookView();
        textFieldTitle.requestFocus();
        tableViewBooks.scrollTo(bookData.size());
    }

    public void delete() throws SQLException, DropboxException {
        LOGGER.info("delete");

        Action response = Dialogs.create()
                .title("Delete")
                .message("Do you want to delete the book?")
                .showConfirm();

        if (response == org.controlsfx.dialog.Dialog.ACTION_YES) {
            databaseConnection.deleteBook(bookId);
            bookData.remove(bookFocusedIndex);
            ImageUtils.deleteCover(textFieldIsbn.getText());
            dropboxManager.deleteCover(textFieldIsbn.getText());

            resetBookView();
        }
    }

    public void returnBook() {
        LOGGER.info("returnBook");

        Stage stage = new Stage();
        final ReturnBookView returnBookView = new ReturnBookView();
        final ReturnBookPresenter presenter = (ReturnBookPresenter) returnBookView.getPresenter();
        presenter.init(stage, this, bookId);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(returnBookView.getView()));
        stage.setTitle("Return Book");
        stage.show();
    }

    @Override
    public void returnBook(Book book) {
        bookData.set(bookFocusedIndex, book);
        setButtonsToLend();
    }

    public void lend() throws SQLException {
        LOGGER.info("lend");

        Stage stage = new Stage();
        final LendView lendView = new LendView();
        final LendPresenter presenter = (LendPresenter) lendView.getPresenter();
        presenter.init(stage, this, bookId);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(lendView.getView()));
        stage.setTitle("Lend Book");
        stage.show();
    }

    @Override
    public void lentBook(Book book) {
        bookData.set(bookFocusedIndex, book);
        setButtonsToReturn();
    }

    private void reloadBooksTable() throws SQLException {
        bookData.clear();
        bookData.addAll(databaseConnection.getBookList().stream().collect(Collectors.toList()));
    }

    private void loadBookDetails(MouseEvent event) {
        bookFocusedIndex = ((TableView) event.getSource()).getFocusModel().getFocusedIndex();
        Book book = (Book) ((TableView) event.getSource()).getFocusModel().getFocusedItem();
        bookId = book.getId();
        textFieldTitle.setText(book.getTitle());
        textFieldAuthor.setText(book.getAuthor());
        textFieldIsbn.setText(book.getIsbn());
        textFieldTags.setText(book.getTags());

        if (book.lentProperty().getValue()) {
            setButtonsToReturn();
        } else {
            setButtonsToLend();
        }

        if (StringUtils.isNotBlank(book.getIsbn())) {
            imageViewCover.setImage(ImageUtils.readCoverOrDefault(book.getIsbn()));
        } else {
            imageViewCover.setImage(null);
        }
    }

    private void addBook(BookInfo bookInfo) {
        LOGGER.info(bookInfo.toString());
        try {
            final Book book = databaseConnection.insertBook(new Book(bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getIsbn(), new Date(), ""));
            bookData.add(book);
        } catch (SQLException e) {
            LOGGER.error(String.format("Cannot insert book into database. %s", bookInfo.toString()), e);
        }

        if (bookInfo.getThumbnail() != null && StringUtils.isNotBlank(bookInfo.getIsbn())) {
            try {
                final File file = ImageUtils.writeCover(bookInfo.getThumbnail(), bookInfo.getIsbn());
                dropboxManager.uploadCover(bookInfo.getIsbn(), file);

            } catch (IOException e) {
                LOGGER.error(String.format("Cannot write book's cover into disk. %s", bookInfo.toString()), e);
            } catch (DropboxException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetBookView() {
        bookId = null;
        textFieldTitle.setText(null);
        textFieldAuthor.setText(null);
        textFieldIsbn.setText(null);
        imageViewCover.setImage(null);
    }

    private void setButtonsToReturn() {
        buttonLend.setDisable(true);
        buttonReturnBook.setDisable(false);
    }

    private void setButtonsToLend() {
        buttonLend.setDisable(false);
        buttonReturnBook.setDisable(true);
    }

    private void serverStatusImageOffline() {
        imageViewServerStatus.setImage(getImageResource("trafficlight-red.png"));
    }

    private void serverStatusImageOnline() {
        imageViewServerStatus.setImage(getImageResource("trafficlight-green.png"));
    }

    private Image getImageResource(String resourceName) {
        return new Image(AppPresenter.class.getResourceAsStream(resourceName));
    }
}
