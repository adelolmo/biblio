package org.ado.biblio.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ado.biblio.desktop.android.AndroidView;
import org.ado.biblio.desktop.db.DatabaseConnection;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.DropboxPresenter;
import org.ado.biblio.desktop.dropbox.DropboxView;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.util.ImageUtils;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.BookInfo;
import org.ado.googleapis.books.NoBookInfoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
    private TableView<Book> tableViewBooks;

    @FXML
    private TableColumn<Book, String> tableColumnId;

    @FXML
    private TableColumn<Book, String> tableColumnAuthor;

    @FXML
    private TableColumn<Book, String> tableColumnTitle;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextField textFieldAuthor;

    @FXML
    private TextField textFieldIsbn;

    @FXML
    private ImageView imageViewCover;

    @FXML
    private Label labelSystem;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonLend;

    @FXML
    private Button buttonSave;

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
        data.addAll(databaseConnection.getBookList().stream().collect(Collectors.toList()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("initializing...");

        tableColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        tableColumnAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        tableViewBooks.setItems(data);

        serverPullingService.setOnSucceeded(event -> {
            LOGGER.info("Succeeded");
            BookMessageDTO[] bookMessages = (BookMessageDTO[]) event.getSource().getValue();
            if (bookMessages != null && bookMessages.length > 0) {
                BookMessageDTO bookMessage = bookMessages[0];
                LOGGER.info("processing book [{}]", bookMessage);
                try {
                    addBook(bookInfoLoader.getBookInfo(bookMessage));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoBookInfoFoundException e) {
                    LOGGER.error(e.getMessage());
                }
                labelSystem.setText(null);

            }
            serverPullingService.restart();

        });
        serverPullingService.setOnFailed(event -> {
            LOGGER.error(event.getSource().getMessage());
            labelSystem.setText("error");
        });
        serverPullingService.start();

        tableViewBooks.setOnMouseClicked(event -> {
                    bookFocusedIndex = ((TableView) event.getSource()).getFocusModel().getFocusedIndex();
                    Book book = (Book) ((TableView) event.getSource()).getFocusModel().getFocusedItem();
                    bookId = book.getId();
                    textFieldTitle.setText(book.getTitle());
                    textFieldAuthor.setText(book.getAuthor());
                    textFieldIsbn.setText(book.getIsbn());
                    imageViewCover.setImage(ImageUtils.readCoverOrDefault(book.getIsbn()));
                }
        );
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

    public void save() throws SQLException {
        LOGGER.info("save");
        if (bookId != null) {
            final Book book = new Book(bookId, textFieldTitle.getText(), textFieldAuthor.getText(), textFieldIsbn.getText());
            databaseConnection.updateBook(book);
            data.set(bookFocusedIndex, book);
        } else {
            final Book book = databaseConnection.insertBook(new Book(textFieldTitle.getText(), textFieldAuthor.getText(), textFieldIsbn.getText()));
            data.add(book);
        }
    }

    public void delete() throws SQLException, DropboxException {
        LOGGER.info("delete");
        databaseConnection.deleteBook(bookId);
        data.remove(bookFocusedIndex);
        ImageUtils.deleteCover(textFieldIsbn.getText());
        dropboxManager.deleteCover(textFieldIsbn.getText());

        bookId = null;
        textFieldTitle.setText(null);
        textFieldAuthor.setText(null);
        textFieldIsbn.setText(null);
        imageViewCover.setImage(null);
    }

    public void lend() {
        LOGGER.info("lend");

    }

    private void addBook(BookInfo bookInfo) {
        LOGGER.info(bookInfo.toString());
        try {
            final Book book = databaseConnection.insertBook(new Book(bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getIsbn()));
            data.add(book);
        } catch (SQLException e) {
            LOGGER.error(String.format("Cannot insert book into database. %s", bookInfo.toString()), e);
        }

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
