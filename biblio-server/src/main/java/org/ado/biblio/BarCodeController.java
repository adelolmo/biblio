package org.ado.biblio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
@RestController
@SuppressWarnings("unused")
public class BarCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarCodeController.class);

    @Autowired
    private BarCodeCache barCodeCache;

    @RequestMapping(value = "/books/{id}", method = RequestMethod.POST)
    public void postBooks(@PathVariable(value = "id") String id,
                          @RequestParam(value = "format") String format,
                          @RequestParam(value = "code") String code) {

        LOGGER.info("POST /books/{} - format [{}] code [{}].", id, format, code);

        barCodeCache.add(id, new BookMessage(format, code));
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public BookMessage[] getBooks(@NotNull @PathVariable(value = "id") String id) {

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

            return bookMessages;
        } else {

            return null;
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
