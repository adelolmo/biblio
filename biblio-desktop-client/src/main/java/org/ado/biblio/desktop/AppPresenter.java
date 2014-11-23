package org.ado.biblio.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ado.biblio.desktop.android.AndroidView;
import org.ado.biblio.desktop.db.DatabaseConnection;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.desktop.dropbox.DropboxView;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.util.ImageUtils;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.BookInfo;
import org.ado.googleapis.books.NoBookInfoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @Inject
    private BookInfoLoader bookInfoLoader;

    @Inject
    private ServerPullingService serverPullingService;

    @Inject
    private DatabaseConnection databaseConnection;

    @Inject
    private DropboxManager dropboxManager;

    @PostConstruct
    public void init() throws Exception {
        serverPullingService.setHostId(AppConfiguration.getAppId());
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
                    BookInfo bookInfo = bookInfoLoader.getBookInfo(bookMessage);
                    addBook(bookInfo);
                    LOGGER.info(bookInfo.toString());
                    if (bookInfo.hasThumbnail()) {
                        imageViewCover.setImage(new Image(bookInfo.getThumbnail()));
                    } else {
                        imageViewCover.setImage(null);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoBookInfoFoundException e) {
                    LOGGER.error(e.getMessage());
                }
                labelSystem.setText(null);

            } else {
                labelSystem.setText("error");

            }
            serverPullingService.restart();

        });
        serverPullingService.start();

        tableViewBooks.setOnMouseClicked(event -> {
            Book book = (Book) ((TableView) event.getSource()).getFocusModel().getFocusedItem();
            textFieldTitle.setText(book.getTitle());
            textFieldAuthor.setText(book.getAuthor());
            textFieldIsbn.setText(book.getIsbn());
            final InputStream inputStream = ImageUtils.read(book.getIsbn(), ".jpeg");
            if (inputStream != null) {
                imageViewCover.setImage(new Image(inputStream));
            } else {
                imageViewCover.setImage(null);
            }
        });
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
        DropboxView dropboxView = new DropboxView();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(dropboxView.getView()));
        stage.setTitle("Dropbox");
        stage.show();
    }

    private void addBook(BookInfo bookInfo) {
        final Book book = new Book(bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getIsbn());
        data.add(book);
        try {
            databaseConnection.insertBook(book);
        } catch (SQLException e) {
            LOGGER.error(String.format("Cannot insert book into database. %s", book.toString()), e);
        }

        try {
            final File file = ImageUtils.write(bookInfo.getThumbnail(), bookInfo.getIsbn(), ".jpeg");
            dropboxManager.uploadCover(bookInfo.getIsbn(), file);
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot write book's covet to disk. %s", book.toString()), e);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }
}
