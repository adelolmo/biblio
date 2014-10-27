package org.ado.biblio;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import org.ado.biblio.domain.BookMessageDTO;
import org.ado.googleapis.books.AbstractBookInfoLoader;
import org.ado.googleapis.books.NoBookInfoFoundException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Class description here.
 *
 * @author andoni
 * @since 26.10.2014
 */
public class BookInfoActivity extends Activity {

    private static final String TAG = BiblioActivity.class.getName();

    private EditText textTitle;
    private EditText textAuthor;
    private ImageView cover;

    private AbstractBookInfoLoader bookInfoLoader;

    private String format;
    private String code;

    public BookInfoActivity() {
        bookInfoLoader = new BookInfoLoader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);
        Log.d(TAG, "onCreate");

        textTitle = (EditText) findViewById(R.id.textTitle);
        textAuthor = (EditText) findViewById(R.id.textAuthor);
        cover = (ImageView) findViewById(R.id.cover);

        format = getIntent().getStringExtra("format");
        code = getIntent().getStringExtra("code");

        final AsyncTask<BookMessageDTO, Void, BookInfoWrapper> asyncTask = new BookInfoLoaderTask().execute(new BookMessageDTO(format, code));
        try {
            final BookInfoWrapper bookInfoWrapper = asyncTask.get();
            textTitle.setText(bookInfoWrapper.getBookInfo().getTitle());
            textAuthor.setText(bookInfoWrapper.getBookInfo().getAuthor());
            cover.setImageBitmap(bookInfoWrapper.getCoverBitmap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accept(View view) {
        Log.d(TAG, "accept");
        new PushBarCodeTask().execute(format, code);
        finish();
    }

    public void cancel(View view) {
        finish();
    }

    class BookInfoLoaderTask extends AsyncTask<BookMessageDTO, Void, BookInfoWrapper> {

        @Override
        protected BookInfoWrapper doInBackground(BookMessageDTO... params) {
            try {
                BookInfoWrapper bookInfoWrapper = new BookInfoWrapper(bookInfoLoader.getBookInfo(params[0]));
                bookInfoWrapper.setCoverBitmap(BitmapFactory.decodeStream(bookInfoWrapper.getBookInfo().getThumbnail()));
                return bookInfoWrapper;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoBookInfoFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class PushBarCodeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            String url = String.format("http://192.168.178.29:8080/push?format=%s&code=%s", params[0], params[1]);
            Log.d(TAG, "http request. url [" + url + "].");

            HttpPost httpPost = new HttpPost(url);
            try {
                client.execute(httpPost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
