package org.ado.biblio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

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

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void greeting(@RequestParam(value = "id") String id,
                         @RequestParam(value = "format") String format,
                         @RequestParam(value = "code") String code) {

        LOGGER.info("/push. id[{}] format [{}] code [{}].", id, format, code);

        barCodeCache.add(id, new BookMessage(format, code));
    }

    @RequestMapping(value = "/pull", method = RequestMethod.GET)
    public BookMessage[] getBookMessage(@NotNull @RequestParam(value = "id") String id) {

        LOGGER.info("/pull. id[{}]", id);

        boolean pullingActive = true;
        while (pullingActive) {
            if (!barCodeCache.isEmpty(id)) {
                pullingActive = false;
            }
            pause(200);
        }

        final BookMessage[] bookMessages = barCodeCache.getBookMessages(id);

        StringBuilder messages = new StringBuilder();
        for (BookMessage bookMessage : bookMessages) {
            messages.append(bookMessage).append(" ");
        }
        LOGGER.info("sending to [{}] message [{}]", id, messages);

        return bookMessages;
    }

    private void pause(int millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
