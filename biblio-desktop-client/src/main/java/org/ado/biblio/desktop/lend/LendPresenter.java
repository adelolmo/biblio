package org.ado.biblio.desktop.lend;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ado.biblio.desktop.db.DatabaseManager;
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
    private DatabaseManager databaseManager;

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
                final Book book = databaseManager.lendBook(bookId, person, ctime);
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