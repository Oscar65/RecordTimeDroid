package com.oml.recordtimedroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Properties;

public class AcercaDeActivity extends Activity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acerca_de);
        String sAcercade = getResources().getString(R.string.mensaje_acerca_de,
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_BUILD);
        final TextView mTextView = (TextView) findViewById(R.id.TextView01);
        mTextView.setText(sAcercade);
    }

}
