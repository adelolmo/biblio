package org.ado.biblio.desktop.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.util.Date;

/**
 * @author Andoni del Olmo,
 * @since 29.01.15
 */
public class LendBook {

    private IntegerProperty id;
    private StringProperty title;
    private StringProperty author;
    private StringProperty isbn;
    private ObservableValue<Date> creation;
    private String tags;
    private BooleanProperty lent;
}