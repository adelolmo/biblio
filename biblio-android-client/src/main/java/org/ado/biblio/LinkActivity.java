package org.ado.biblio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Class description here.
 *
 * @author andoni
 * @since 27.10.2014
 */
public class LinkActivity extends Activity {

    private static final String TAG = LinkActivity.class.getName();
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
            Code code = getCode(scanResult.getContents());
            final SharedPreferences.Editor preferences = sharedPreferences.edit();
            preferences.putString("link", code.getLink()).commit();
            preferences.putString("server", code.getServerHost()).commit();
        }
        finish();
    }

    private Code getCode(String contents) {
        return new Code(contents);
    }

    private class Code {
        private String link;
        private String serverHost;

        public Code(String contents) {
            Log.d(TAG, String.format("contents [%s]", contents));
            final String[] message = contents.split("\\+");
            link = message[0];
            serverHost = message[1];
        }

        public String getLink() {
            return link;
        }

        public String getServerHost() {
            return serverHost;
        }
    }
}
