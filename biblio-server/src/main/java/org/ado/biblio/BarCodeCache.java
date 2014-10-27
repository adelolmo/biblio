package org.ado.biblio;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Service
public class BarCodeCache {

    private Map<String, List<BookMessage>> map;

    public BarCodeCache() {
        map = new ConcurrentHashMap<String, List<BookMessage>>();
    }

    public void add(@NotNull String id, @NotNull BookMessage bookMessage) {
        final List<BookMessage> bookMessages = map.get(id);
        if (bookMessages == null) {
            final ArrayList<BookMessage> messageList = new ArrayList<BookMessage>();
            messageList.add(bookMessage);
            map.put(id, messageList);

        } else {
            bookMessages.add(bookMessage);
        }
    }

    public BookMessage[] getBookMessages(@NotNull String id) {

        final List<BookMessage> bookMessagesList = map.get(id);
        BookMessage[] bookMessageArray = bookMessagesList.toArray(new BookMessage[bookMessagesList.size()]);
        bookMessagesList.clear();
        return bookMessageArray;
    }

    public boolean isEmpty(@NotNull String id) {
        final List<BookMessage> bookMessages = map.get(id);
        return bookMessages == null || bookMessages.isEmpty();
    }
}
