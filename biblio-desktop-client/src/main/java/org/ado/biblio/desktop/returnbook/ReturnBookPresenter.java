package org.ado.biblio.desktop.returnbook;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ado.biblio.desktop.db.DatabaseConnection;
import org.ado.biblio.desktop.model.Book;
import org.ado.biblio.desktop.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Andoni del Olmo,
 * @since 14.01.15
 */
public class ReturnBookPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(ReturnBookPresenter.class);

    @FXML
    private TextField textFieldDate;

    @Inject
    private DatabaseConnection databaseConnection;

    private Stage stage;
    private ReturnBookListener returnBookListener;
    private int bookId;

    public void init(Stage stage, ReturnBookListener returnBookListener, int bookId) {
        this.stage = stage;
        this.returnBookListener = returnBookListener;
        this.bookId = bookId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldDate.setText(DateUtils.format(new Date(), DateUtils.DateTypeEnum.DATE));
    }

    public void accept() throws SQLException {
        LOGGER.info("accept");
        if (StringUtils.isBlank(textFieldDate.getText())) {
            Action response = Dialogs.create()
                    .title("Error")
                    .message("Please introduce a date.")
                    .showError();
        } else {

            Date returnDate = null;
            try {
                returnDate = DateUtils.parse(textFieldDate.getText(), DateUtils.DateTypeEnum.DATE);
            } catch (Exception e) {
                Dialogs.create()
                        .title("Wrong date format")
                        .message(String.format("Please introduce a valid date format \"%s\".", DateUtils.getDateFormat(DateUtils.DateTypeEnum.DATE)))
                        .showError();
            }
            if (returnDate != null) {
                final Book book = databaseConnection.returnBook(bookId, returnDate);
                returnBookListener.returnBook(book);
                stage.close();
            }
        }
    }

    public void cancel() {
        LOGGER.info("cancel");
        stage.close();
    }

    public interface ReturnBookListener {
        void returnBook(Book book);
    }
}