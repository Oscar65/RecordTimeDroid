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
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {
    private TextView tView;
    private int linea = 1;
    private StringBuffer text;
    private Date dFechaAnterior;
    private String data;
    private String fileName = "lines";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tView = (TextView) findViewById(R.id.textView1);
        text = new StringBuffer();

        try {
            String[] sFileList = this.fileList();
            if (Arrays.asList(sFileList).contains(fileName)) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.ROOT);
                DiasHorasMinutosSegundosMilisegundos dhmsmTempo = new DiasHorasMinutosSegundosMilisegundos();

                FileInputStream fin = openFileInput(fileName);
                int c;
                String sFecha = "";

                while ((c = fin.read()) != -1) {
                    if (c == '\n') {
                        Date d = format.parse(sFecha);
                        text.append(String.format(Locale.ROOT, "%03d %s ", linea++, sFecha));
                        if (dFechaAnterior != null) {
                            calculaDiasHorasMinutosSegundos(dFechaAnterior, d, dhmsmTempo);
                            text.append(String.format(Locale.ROOT, " %03d %s %02d:%02d:%02d.%03d",
                                    (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias),
                                    (int) dhmsmTempo.getHoras(),
                                    (int) dhmsmTempo.getMinutos(), (int) dhmsmTempo.getSegundos(),
                                    (int) dhmsmTempo.getMilisegundos()));
                        }
                        text.append("\n");
                        dFechaAnterior = format.parse(sFecha);
                        sFecha = "";
                    } else {
                        sFecha += Character.toString((char) c);
                    }
                }
                fin.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),"ERROR " + e.getMessage() +
                    " reading file",Toast.LENGTH_LONG).show();
        }
        tView.setText(text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notification = getResources().getString(R.string.notificacion) + " " + linea;
                Snackbar.make(view, notification, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Date dFechaHora = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.ROOT);
                CharSequence csFechaHora = dateFormat.format(dFechaHora);
                text.append(String.format(Locale.ROOT, "%03d ", linea));
                text.append(csFechaHora);
                text.append(" ");
                if (linea > 1) {
                    DiasHorasMinutosSegundosMilisegundos dhmsmTempo =
                            new DiasHorasMinutosSegundosMilisegundos();
                    calculaDiasHorasMinutosSegundos(dFechaAnterior, dFechaHora, dhmsmTempo);
                    text.append(String.format(Locale.ROOT, " %03d %s %02d:%02d:%02d.%03d",
                            (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias), (int) dhmsmTempo.getHoras(),
                            (int) dhmsmTempo.getMinutos(), (int) dhmsmTempo.getSegundos(),
                            (int) dhmsmTempo.getMilisegundos()));
                }
                linea++;
                text.append("\n");
                tView.setText(text);
                dFechaAnterior = new Date(dFechaHora.getTime());
                data = dateFormat.format(dFechaHora) + "\n";

                // Save line in file
                try {
                    FileOutputStream fOut = openFileOutput(fileName, MODE_APPEND);
                    fOut.write(data.getBytes());
                    fOut.close();
                    //Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"ERROR " + e.getMessage() +
                            " saving file",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calculaDiasHorasMinutosSegundos(Date dIni, Date dFin,
                                                 DiasHorasMinutosSegundosMilisegundos dhmsm) {
        long different = dFin.getTime() - dIni.getTime();
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

        dhmsm.setDias(elapsedDays);
        dhmsm.setHoras(elapsedHours);
        dhmsm.setMinutos(elapsedMinutes);
        dhmsm.setSegundos(elapsedSeconds);
        dhmsm.setMilisegundos(different);
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

        if (id == R.id.reset) {
            try {
                String[] sFileList = this.fileList();
                if (Arrays.asList(sFileList).contains(fileName)) {
                    boolean ret = this.deleteFile(fileName);
                    if (ret) {
                        Toast.makeText(getBaseContext(), "file " + fileName + " deleted", Toast.LENGTH_LONG).show();
                        text.delete(0,text.length());
                        linea = 1;
                        tView.setText(text);
                    } else {
                        Toast.makeText(getBaseContext(), "file " + fileName + " NOT deleted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "file " + fileName + " NOT exists", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(),"ERROR " + e.getMessage() +
                        " deleting file",Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}