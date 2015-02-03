package org.ado.biblio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
 * @since 26.10.2014
 */
public class BookInfoActivity extends Activity {

    public static final String REST_RESOURCE_PUSH_MESSAGE = "%s/books/%s?format=%s&code=%s";
    private static final String TAG = BiblioActivity.class.getName();
    private AbstractBookInfoLoader bookInfoLoader;
    private SharedPreferences sharedPreferences;

    private String link;
    private String format;
    private String code;

    public BookInfoActivity() {
        bookInfoLoader = new BookInfoLoader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("biblio", Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_bookinfo);

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
            } catch (Exception e) {
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
            String url = String.format(REST_RESOURCE_PUSH_MESSAGE,
                    sharedPreferences.getString("server", ""), params[0], params[1], params[2]);
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
