package org.ado.googleapis.books;

import com.google.gson.Gson;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.json.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public abstract class AbstractBookInfoLoader {

    private static final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=%s";
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractBookInfoLoader.class);

    public abstract HttpClient getHttpClient();

    public BookInfo getBookInfo(BookMessageDTO bookMessage) throws IOException, NoBookInfoFoundException {
        HttpClient client = getHttpClient();
        String url = String.format(GOOGLE_BOOKS_URL, bookMessage.getCode());
        LOGGER.info(String.format("Book search url \"%s\"", url));
        HttpResponse response = client.execute(new HttpGet(url));

        Volumes volumes = new Gson().fromJson(IOUtils.toString(response.getEntity().getContent()), Volumes.class);
        if (volumes == null || volumes.getItems() == null) {
            throw new NoBookInfoFoundException(bookMessage);
        }
        return getBookInfo(volumes.getItems().get(0));
    }

    private BookInfo getBookInfo(Item item) {
        BookInfo bookInfo = new BookInfo();
        VolumenInfo volumeInfo = item.getVolumeInfo();

        if (volumeInfo != null) {
            StringBuilder authors = new StringBuilder();
            if (!volumeInfo.getAuthors().isEmpty()) {
                for (String author : volumeInfo.getAuthors()) {
                    authors.append(author);
                }
            }
            bookInfo.setAuthor(authors.toString());

            bookInfo.setTitle(volumeInfo.getTitle());

            ImageLinks imageLinks = volumeInfo.getImageLinks();
            if (imageLinks != null) {
                bookInfo.setThumbnail(getThumbnail(imageLinks.getThumbnail()));
                bookInfo.setSmallThumbnail(getThumbnail(imageLinks.getSmallThumbnail()));
            }

            List<IndustryIdentifier> industryIdentifiers = volumeInfo.getIndustryIdentifiers();
            for (IndustryIdentifier industryIdentifier : industryIdentifiers) {
                if (IndustryIdentifierTypeEnum.ISBN_13 == industryIdentifier.getType()) {
                    bookInfo.setIsbn(industryIdentifier.getIdentifier());
                }
            }
        }

        return bookInfo;
    }

    private InputStream getThumbnail(String url) {
        try {
            return getHttpClient().execute(new HttpGet(url)).getEntity().getContent();
        } catch (Exception e) {
            return null;
        }
    }
}
