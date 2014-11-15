package org.ado.biblio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

    private AbstractBookInfoLoader bookInfoLoader;

    private String link;
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

        TextView textTitle = (TextView) findViewById(R.id.textTitle);
        TextView textAuthor = (TextView) findViewById(R.id.textAuthor);
        ImageView cover = (ImageView) findViewById(R.id.cover);

        link = getIntent().getStringExtra("link");
        format = getIntent().getStringExtra("format");
        code = getIntent().getStringExtra("code");

        final AsyncTask<BookMessageDTO, Void, BookInfoWrapper> asyncTask = new BookInfoLoaderTask().execute(new BookMessageDTO(format, code));
        try {
            final BookInfoWrapper bookInfoWrapper = asyncTask.get();
            textTitle.setText(bookInfoWrapper.getBookInfo().getTitle());
            textAuthor.setText(bookInfoWrapper.getBookInfo().getAuthor());
            final Bitmap coverBitmap = bookInfoWrapper.getCoverBitmap();
            if (coverBitmap != null) {
                cover.setImageBitmap(coverBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accept(View view) {
        Log.d(TAG, "accept");
        new PushBarCodeTask().execute(link, format, code);
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

        boolean connectionError = false;

        @Override
        protected Void doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            String url = String.format("http://192.168.178.29:8080/push?id=%s&format=%s&code=%s", params[0], params[1], params[2]);
            Log.d(TAG, "http request. url [" + url + "].");

            HttpPost httpPost = new HttpPost(url);
            try {
                client.execute(httpPost);
            } catch (Exception e) {
                Log.e(TAG, "Unable to establish connection with server", e);
                connectionError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (connectionError) {
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to establish connection with server", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        }
    }
}