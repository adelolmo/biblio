package org.ado.biblio.desktop;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.BookInfo;
import org.ado.googleapis.BookInfoLoader;
import org.ado.googleapis.NoBookInfoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class MainController {

    private final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
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
    private ServerPullingService serverPullingService;
    private BookInfoLoader bookInfoLoader;
    private HttpImageLoader httpImageLoader;

    public MainController() {
        serverPullingService = new ServerPullingService();
        bookInfoLoader = new BookInfoLoader();
        httpImageLoader = new HttpImageLoader();
        data.add(new Book("Super Me", "Andoni del Olmo", "12345"));
    }

    @FXML
    private void initialize() throws Exception {
        LOGGER.info("initializing...");

        tableColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        tableColumnAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        tableViewBooks.setItems(data);

        serverPullingService.setOnSucceeded(event -> {
            LOGGER.info("Succeeded");
            BookMessageDTO[] bookMessages = (BookMessageDTO[]) event.getSource().getValue();
            for (BookMessageDTO bookMessage : bookMessages) {
                LOGGER.info("New book: " + bookMessage.toString());
            }
            if (bookMessages.length > 0) {
                BookMessageDTO bookMessage = bookMessages[0];
                LOGGER.info("Format [" + bookMessage.getFormat() + "] Code [" + bookMessage.getCode() + "]");
                try {
                    BookInfo bookInfo = bookInfoLoader.getBookInfo(bookMessage);
                    addBookToTable(bookInfo);
                    LOGGER.info(bookInfo.toString());
                    if (bookInfo.hasImage()) {
                        imageViewCover.setImage(new Image(httpImageLoader.getImage(bookInfo.getImageUrl())));
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

    private void addBookToTable(BookInfo bookMessage) {
        data.add(new Book(bookMessage.getTitle(), bookMessage.getAuthor(), bookMessage.getIsbn()));
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
