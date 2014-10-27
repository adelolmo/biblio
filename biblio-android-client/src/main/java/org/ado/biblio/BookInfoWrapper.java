package org.ado.biblio;

import android.graphics.Bitmap;
import org.ado.googleapis.books.BookInfo;

/**
 * Class description here.
 *
 * @author andoni
 * @since 26.10.2014
 */
public class BookInfoWrapper {

    private BookInfo bookInfo;
    private Bitmap coverBitmap;

    public BookInfoWrapper(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Bitmap getCoverBitmap() {
        return coverBitmap;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        this.coverBitmap = coverBitmap;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }
}
