package org.ado.biblio.desktop.lend;

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
 * @since 13.01.15
 */
public class LendPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(LendPresenter.class);

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldDate;

    @Inject
    private DatabaseConnection databaseConnection;

    private Stage stage;
    private int bookId;
    private LendBookListener lendBookListener;

    public void init(Stage stage, LendBookListener lendBookListener, int bookId) {
        this.stage = stage;
        this.bookId = bookId;
        this.lendBookListener = lendBookListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldDate.setText(DateUtils.format(new Date(), DateUtils.DateTypeEnum.DATE));
    }

    public void accept() throws SQLException {
        LOGGER.info("accept");

        final String person = textFieldName.getText();

        if (StringUtils.isBlank(person)) {
            Action response = Dialogs.create()
                    .title("Error")
                    .message("Please introduce a name.")
                    .showError();
        } else {
            try {
                Date ctime = DateUtils.parse(textFieldDate.getText(), DateUtils.DateTypeEnum.DATE);
                final Book book = databaseConnection.lendBook(bookId, person, ctime);
                lendBookListener.lentBook(book);

                stage.close();
            } catch (Exception e) {
                Dialogs.create()
                        .title("Wrong date format")
                        .message(String.format("Please introduce a valid date format \"%s\".", DateUtils.getDateFormat(DateUtils.DateTypeEnum.DATE)))
                        .showError();
            }

        }
    }

    public void cancel() {
        LOGGER.info("cancel");
        stage.close();
    }

    public interface LendBookListener {
        void lentBook(Book book);
    }
}