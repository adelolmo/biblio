package org.ado.biblio;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Service
public class BarCodeCache {

    private Map<String, List<BookMessage>> map;

    @PostConstruct
    private void init() {
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
        if (bookMessagesList != null) {
            BookMessage[] bookMessageArray = bookMessagesList.toArray(new BookMessage[bookMessagesList.size()]);
            bookMessagesList.clear();
            return bookMessageArray;
        } else {
            return new BookMessage[]{};
        }
    }

    public boolean isEmpty(@NotNull String id) {
        final List<BookMessage> bookMessages = map.get(id);
        return bookMessages == null || bookMessages.isEmpty();
    }
}
