package org.ado.biblio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BiblioActivity extends Activity {

    private static final String TAG = BiblioActivity.class.getName();

    private SharedPreferences sharedPreferences;

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
        sharedPreferences = getApplicationContext().getSharedPreferences("biblio", Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void startBarcodeApp(View view) {
        Log.d(TAG, "startBarcodeApp");
        if (isDeviceLinked()) {
            callScannerApp();
        } else {
            showDeviceNotLinkErrorMessage();
        }
    }

    public void link(View view) {
        startActivity(new Intent(this, LinkActivity.class));
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

            final Intent bookInfoIntent = new Intent(this, BookInfoActivity.class);
            bookInfoIntent.putExtra("link", sharedPreferences.getString("link", null));
            bookInfoIntent.putExtra("format", format);
            bookInfoIntent.putExtra("code", contents);
            startActivity(bookInfoIntent);

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

    private boolean isDeviceLinked() {
        return sharedPreferences.getString("link", null) != null;
    }

    private void showDeviceNotLinkErrorMessage() {
        Toast toast = Toast.makeText(this, "Please link the device to the desktop application first of all!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }
}

