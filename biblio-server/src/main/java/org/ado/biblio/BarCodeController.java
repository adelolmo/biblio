package org.ado.biblio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
@RestController
public class BarCodeController {

    @Autowired
    private BarCodeCache barCodeCache;

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void greeting(@RequestParam(value = "format") String format,
                         @RequestParam(value = "code") String code) {

        System.out.println("/push  format [" + format + "] code [" + code + "].");

        barCodeCache.add(new BookMessage(format, code));
    }

    @RequestMapping(value = "/pull", method = RequestMethod.GET)
    public BookMessage[] getBookMessage() {

        System.out.println("/pull");

        boolean pullingActive = true;
        while (pullingActive) {
            if (!barCodeCache.isEmpty()) {
                pullingActive = false;
            }
            pause(200);
        }

        final BookMessage[] bookMessages = barCodeCache.getBookMessages();

        StringBuilder messages = new StringBuilder();
        for (BookMessage bookMessage : bookMessages) {
            messages.append(bookMessage).append(" ");
        }
        System.out.println("sending -> " + messages);

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
