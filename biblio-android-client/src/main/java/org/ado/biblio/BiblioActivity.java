package org.ado.biblio;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class BiblioActivity extends Activity {

    /**
     * Send this intent to open the Barcodes app in scanning mode, find a barcode, and return
     * the results.
     */
    public static final String SCAN_ACTION_INTENT = "com.google.zxing.client.android.SCAN";
    private static final String TAG = BiblioActivity.class.getName();

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void startBarcodeApp(View view) {
        Log.d(TAG, "startBarcodeApp");
        callScannerApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult - requestCode[" + requestCode + "] resultCode[" + resultCode + "] intent[" + intent + "].");
        Log.d(TAG, "intent [" + (intent != null ? intent.getDataString() : "empty") + "].");

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String contents = scanResult.getContents();
            String format = scanResult.getFormatName();
            Log.d(TAG, "Result. content [" + contents + "] format [" + format + "].");

            Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

            new PushBarCodeTask().execute(format, contents);
        } else {
            // Handle cancel
            Log.e(TAG, "Scan was cancelled");
            Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    }

    private void callScannerApp() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("SCAN_MODE", "PRODUCT_MODE");
        integrator.initiateScan();
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

