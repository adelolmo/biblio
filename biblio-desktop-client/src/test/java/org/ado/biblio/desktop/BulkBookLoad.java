package org.ado.biblio.desktop;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
 * @since 07.12.14
 */
public class BulkBookLoad {

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkBookLoad.class);

    public static void main(String[] args) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://www.kimonolabs.com/api/43h8bb74?apikey=a506e75ca96092b0f73b0ff59c15abe6");
        HttpResponse response = client.execute(request);

        LOGGER.info("Response code [{}]", response.getStatusLine().getStatusCode());

        String responseContent = IOUtils.toString(response.getEntity().getContent());
        final KimonoIsbn kimonoIsbn = new Gson().fromJson(responseContent, KimonoIsbn.class);
        LOGGER.info(kimonoIsbn.toString());

        final List<StringMap> books = kimonoIsbn.getResults().get("books");
        for (StringMap book : books) {
            final String isbnUrl = (String) book.get("isbn");
            LOGGER.info(isbnUrl);
            send(isbnUrl);
        }
    }

    private static void send(String isbnUrl) throws IOException {
        final String isbn = isbnUrl.split("/")[5];
        HttpClient client = HttpClientBuilder.create().build();
        String url = String.format("%s/books/%s?format=%s&code=%s", "http://localhost:8086", "fd9728f8-e611-4e40-8c3d-6b959d265d62", "EAN_13", isbn);

        HttpPost httpPost = new HttpPost(url);
        client.execute(httpPost);
    }

    private static class KimonoIsbn {
        private String name;
        private int count;
        private String frequency;
        private int version;
        private boolean newdata;
        private Map<String, List<StringMap>> results;

        public Map<String, List<StringMap>> getResults() {
            return results;
        }
    }

    private static class Book {
        private List<String> books;
        private String isbn;

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public List<String> getBooks() {
            return books;
        }

        public void setBooks(List<String> books) {
            this.books = books;
        }
    }


// ----- STATIC ACCESSOR ---------------------------------------------------------------------------------------- //

// ----- TO BE IMPLEMENTED BY SUBCLASSES ------------------------------------------------------------------------ //

// ----- OVERWRITTEN METHODS ------------------------------------------------------------------------------------ //

// ----- GETTERS / SETTER --------------------------------------------------------------------------------------- //

// ----- INTERNAL HELPER ---------------------------------------------------------------------------------------- //

// ----- HELPER CLASSES ----------------------------------------------------------------------------------------- //

// ----- DEPENDENCY INJECTION ----------------------------------------------------------------------------------- //

}