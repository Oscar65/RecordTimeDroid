package com.oml.recordtimedroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AcercaDeActivity extends Activity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acerca_de);
        String sAcercade = getResources().getString(R.string.mensaje_acerca_de_google,
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        final TextView mTextView = (TextView) findViewById(R.id.TextView01);
        mTextView.setText(sAcercade);
    }

}
