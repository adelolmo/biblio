package org.ado.biblio.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ado.biblio.desktop.android.AndroidView;
import org.ado.biblio.desktop.db.DatabaseConnection;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.DropboxPresenter;
import org.ado.biblio.desktop.dropbox.DropboxView;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.settings.SettingsPresenter;
import org.ado.biblio.desktop.settings.SettingsView;
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

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class AppPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(AppPresenter.class);
    private final ObservableList<Book> data = FXCollections.observableArrayList();

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
    private Label labelSystem;

    @Inject
    private BookInfoLoader bookInfoLoader;

    @Inject
    private ServerPullingService serverPullingService;

    @Inject
    private DatabaseConnection databaseConnection;

    @Inject
    private DropboxManager dropboxManager;

    private Integer bookId;
    private int bookFocusedIndex;

    @PostConstruct
    public void init() throws Exception {
        serverPullingService.setClientId(AppConfiguration.getAppId());
        reloadBooksTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("initializing...");

        tableColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        tableColumnAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        tableColumnCreation.setCellValueFactory(cellData -> cellData.getValue().creationProperty());

        tableViewBooks.setItems(data);

        serverPullingService.setOnSucceeded(event -> {
            LOGGER.info("Succeeded");
            BookMessageDTO[] bookMessages = (BookMessageDTO[]) event.getSource().getValue();
            if (bookMessages != null) {
                for (BookMessageDTO bookMessage : bookMessages) {
                    LOGGER.info("processing book [{}]", bookMessage);
                    try {
                        addBook(bookInfoLoader.getBookInfo(bookMessage));
                        labelSystem.setText(null);

                    } catch (IOException e) {
                        e.printStackTrace();
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
            labelSystem.setText("error");
        });
        serverPullingService.start();

        tableViewBooks.setOnMouseClicked(this::loadBookDetails);
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

    }

    public void search(Event event) throws SQLException {
        LOGGER.info("search");
        String searchSequence = textFieldSearch.getCharacters().toString();
        LOGGER.debug(searchSequence);
        if (StringUtils.isBlank(searchSequence)) {
            reloadBooksTable();

        } else {
            data.removeIf(new Predicate<Book>() {
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
            data.set(bookFocusedIndex, book);
        } else {
            final Book book = databaseConnection
                    .insertBook(new Book(textFieldTitle.getText(), textFieldAuthor.getText(), textFieldIsbn.getText(), new Date(), textFieldTags.getText()));
            data.add(book);
        }
    }

    public void add() {
        LOGGER.info("add");
        resetBookView();
        textFieldTitle.requestFocus();
    }

    public void delete() throws SQLException, DropboxException {
        LOGGER.info("delete");

        Action response = Dialogs.create()
                .title("Delete")
                .message("Do you want to delete the book?")
                .showConfirm();

        if (response == org.controlsfx.dialog.Dialog.ACTION_YES) {
            databaseConnection.deleteBook(bookId);
            data.remove(bookFocusedIndex);
            ImageUtils.deleteCover(textFieldIsbn.getText());
            dropboxManager.deleteCover(textFieldIsbn.getText());

            resetBookView();
        }
    }

    public void lend() {
        LOGGER.info("lend");

    }

    private void reloadBooksTable() throws SQLException {
        data.clear();
        data.addAll(databaseConnection.getBookList().stream().collect(Collectors.toList()));
    }

    private void loadBookDetails(MouseEvent event) {
        bookFocusedIndex = ((TableView) event.getSource()).getFocusModel().getFocusedIndex();
        Book book = (Book) ((TableView) event.getSource()).getFocusModel().getFocusedItem();
        bookId = book.getId();
        textFieldTitle.setText(book.getTitle());
        textFieldAuthor.setText(book.getAuthor());
        textFieldIsbn.setText(book.getIsbn());
        textFieldTags.setText(book.getTags());
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
            data.add(book);
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
}
