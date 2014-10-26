package org.ado.biblio;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Service
public class BarCodeCache {

    private List<BookMessage> bookMessageList;

    public BarCodeCache() {
        bookMessageList = new ArrayList<BookMessage>();
    }

    public void add(BookMessage bookMessage) {
        bookMessageList.add(bookMessage);
    }

    public BookMessage[] getBookMessages() {
        BookMessage[] bookMessages = bookMessageList.toArray(new BookMessage[bookMessageList.size()]);
        bookMessageList.clear();
        return bookMessages;
    }

    public boolean isEmpty() {
        return bookMessageList.isEmpty();
    }
}
