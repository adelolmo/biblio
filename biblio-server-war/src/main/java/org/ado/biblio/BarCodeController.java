package org.ado.biblio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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
@RestController
public class BarCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarCodeController.class);

    @Autowired
    private BarCodeCache barCodeCache;

    @RequestMapping(method = RequestMethod.POST, value = "/books/{id}")
    public ResponseEntity<Void> postBooks(@NotNull @PathVariable(value = "id") String id,
                                          @NotNull @RequestParam(value = "format") String format,
                                          @NotNull @RequestParam(value = "code") String code) {

        LOGGER.info("POST /books/{} - format [{}] code [{}].", id, format, code);

        barCodeCache.add(id, new BookMessage(format, code));

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookMessage[]> getBooks(@NotNull @PathVariable(value = "id") String id) {

        LOGGER.info("GET /books/{}", id);

        boolean pullingActive = true;
        long timeout = System.currentTimeMillis() + (60 * 1000);
        while (pullingActive && timeout > System.currentTimeMillis()) {
            if (!barCodeCache.isEmpty(id)) {
                pullingActive = false;
            }
            pause(200);
        }

        final BookMessage[] bookMessages = barCodeCache.getBookMessages(id);

        if (bookMessages.length > 0) {
            StringBuilder messages = new StringBuilder();
            for (BookMessage bookMessage : bookMessages) {
                messages.append(bookMessage).append(" ");
            }
            LOGGER.info("sending to [{}] message [{}]", id, messages);

            return new ResponseEntity<BookMessage[]>(bookMessages, HttpStatus.OK);
        } else {

            return new ResponseEntity<BookMessage[]>(HttpStatus.OK);
        }
    }

    private void pause(int millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
