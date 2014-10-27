package org.ado.biblio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Class description here.
 *
 * @author andoni
 * @since 27.10.2014
 */
public class LinkActivity extends Activity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences("biblio", Context.MODE_PRIVATE);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String contents = scanResult.getContents();
            sharedPreferences.edit().putString("link", contents).commit();
        }
        finish();
    }
}
