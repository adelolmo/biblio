package org.ado.biblio.desktop;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import org.ado.biblio.desktop.dropbox.DropboxException;
import org.ado.biblio.desktop.dropbox.DropboxView;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.BookInfo;
import org.ado.googleapis.books.NoBookInfoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
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
    private Label labelSystem;

    @Inject
    private BookInfoLoader bookInfoLoader;

    @Inject
    private ServerPullingService serverPullingService;

    @PostConstruct
    public void init() throws UnknownHostException {
        serverPullingService.setHostId(AppConfiguration.getAppId());
        data.add(new Book("Super Me", "Someone else", "12345"));
        data.add(new Book("Melonize Me", "Andoni del Olmo", "12345"));
        data.add(new Book("The Bible", "God", "666"));
        data.add(new Book("A-Team", "Anja", "687987987"));
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Book book = (Book) o;

            if (author != null ? !author.equals(book.author) : book.author != null) return false;
            if (isbn != null ? !isbn.equals(book.isbn) : book.isbn != null) return false;
            if (title != null ? !title.equals(book.title) : book.title != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = title != null ? title.hashCode() : 0;
            result = 31 * result + (author != null ? author.hashCode() : 0);
            result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Book{");
            sb.append("title=").append(title);
            sb.append(", author=").append(author);
            sb.append(", isbn=").append(isbn);
            sb.append('}');
            return sb.toString();
        }
    }
}
