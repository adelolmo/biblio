package org.ado.biblio.desktop;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxManager;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.BookInfo;
import org.ado.googleapis.books.NoBookInfoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

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
    ImageView imageViewQrCode;

    @FXML
    private Label labelSystem;

    @Inject
    private DropboxManager dropboxManager;

    @Inject
    private BookInfoLoader bookInfoLoader;

    @Inject
    private ServerPullingService serverPullingService;

    public AppPresenter() {
        data.add(new Book("Super Me", "Andoni del Olmo", "12345"));
    }

    @PostConstruct
    public void init() throws UnknownHostException {
        serverPullingService.setHostId(getHostId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("initializing...");
        createQrCode();

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
                    addBookToTable(bookInfo);
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
        });
    }

    public void linkDropbox() throws DropboxException {
        LOGGER.debug("Dropbox...");
        if (!dropboxManager.isLinked()) {
            dropboxManager.link(accountInfo -> LOGGER.info("Application is linked to Dropbox account [{}] id [{}]",
                    accountInfo.getDisplayName(), accountInfo.getUserId()));

        } else {
            dropboxManager.unlink();
        }
    }

    private void addBookToTable(BookInfo bookMessage) {
        data.add(new Book(bookMessage.getTitle(), bookMessage.getAuthor(), bookMessage.getIsbn()));
    }

    private void createQrCode() {
        try {
            final String hostNameId = getHostId();
            LOGGER.info("Hostname: {}", hostNameId);
            BitMatrix matrix = new MultiFormatWriter().encode(hostNameId, BarcodeFormat.QR_CODE, 300, 300);
            final BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);

            imageViewQrCode.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch (UnknownHostException | WriterException e) {
            e.printStackTrace();
        }
    }

    private String getHostId() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    public class Book {
        private StringProperty title;
        private StringProperty author;
        private StringProperty isbn;

        public Book(String title, String author, String isbn) {
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.isbn = new SimpleStringProperty(isbn);
        }

        public String getTitle() {
            return title.get();
        }

        public StringProperty titleProperty() {
            return title;
        }

        public String getAuthor() {
            return author.get();
        }

        public StringProperty authorProperty() {
            return author;
        }

        public String getIsbn() {
            return isbn.get();
        }

        public void setIsbn(String isbn) {
            this.isbn.set(isbn);
        }

        public StringProperty isbnProperty() {
            return isbn;
        }
    }
}
