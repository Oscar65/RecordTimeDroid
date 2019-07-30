package com.oml.recordtimedroid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.Date;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {
    private TextView tView;
    private static int linea = 1;
    private static String text = "";
    private static Date dFechaAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tView = (TextView) findViewById(R.id.textView1);
        tView.setText(text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notification = getResources().getString(R.string.notificacion) + " " + linea;
                Snackbar.make(view, notification, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                text += String.format(Locale.ROOT,"%03d - ", linea++) +
                        android.text.format.DateFormat.format("dd-MM-yyyy HH:mm:ss",
                                new java.util.Date()) + " ";
                if (dFechaAnterior != null) {
                    Date dAhora = new Date();
                    long different = dAhora.getTime() - dFechaAnterior.getTime();
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;

                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;

                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;

                    long elapsedSeconds = different / secondsInMilli;
                    different = different % secondsInMilli;

                    long elapsedMiliseconds = different;

                    text += String.format(Locale.ROOT, " - %03d %s %02d:%02d:%02d.%03d ", (int)elapsedDays,
                            getResources().getString(R.string.dias), (int)elapsedHours,
                            (int)elapsedMinutes, (int)elapsedSeconds, (int)elapsedMiliseconds);
                }
                text += "\n";
                tView.setText(text);
                dFechaAnterior = new Date();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_acerca_de) {
            Intent i = new Intent(this, AcercaDeActivity.class);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
