package com.oml.recordtimedroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ScrollingActivity extends AppCompatActivity {
    private int linea = 1;
    private long lFechaAnterior;
    private String data;
    private String fileName = "lines";
    private MyTextView mtvSumLapses;
    private long lTotal;
    private long lFechaInicial = -1;
    LinearLayout csLL;
    Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        csLL = findViewById(R.id.cs_linear_layout);
        mtvSumLapses = (MyTextView) findViewById(R.id.sumLapses);

        try {
            myContext = this;
            String[] sFileList = this.fileList();
            if (Arrays.asList(sFileList).contains(fileName)) {
                SimpleDateFormat sdfDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.ROOT);
                DiasHorasMinutosSegundosMilisegundos dhmsmTempo = new DiasHorasMinutosSegundosMilisegundos();

                FileInputStream fin = openFileInput(fileName);
                int c;
                String sFecha;
                long lFecha;
                lFechaAnterior = -1;
                StringBuilder sbBuff = new StringBuilder();
                mtvSumLapses.setlDiferencia(0);

                while ((c = fin.read()) != -1) {
                    if (c == '\n') {
                        // To be backward compatible with file format dd-MM-yyyy HH:mm:ss.SSS
                        if (sbBuff.toString().contains("-")) {
                            Date d = sdfDateFormat.parse(sbBuff.toString());
                            if (d != null) {
                                lFecha = d.getTime();
                            } else {
                                lFecha = 0;
                            }
                        } else {
                            lFecha = Long.parseLong(sbBuff.toString());
                        }
                        if (lFechaInicial == -1) lFechaInicial = lFecha;
                        Date d = new Date(lFecha);
                        sFecha = sdfDateFormat.format(d);
                        // Create MyTextView dynamically
                        MyTextView mtv = new MyTextView(this);
                        LinearLayout.LayoutParams llLP = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                        llLP.setMargins(0, 0, 0, 0);
                        mtv.setLayoutParams(llLP);
                        mtv.setText(String.format(Locale.ROOT, "%03d | %s |", linea++, sFecha));
                        mtv.setSelected(false);
                        if (lFechaAnterior != -1) {
                            calculaDiasHorasMinutosSegundos(lFechaAnterior, lFecha, dhmsmTempo);
                            mtv.setText(String.format(Locale.ROOT, "%s %03d %s %02d:%02d:%02d.%03d",
                                    mtv.getText(),
                                    (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias),
                                    (int) dhmsmTempo.getHoras(),
                                    (int) dhmsmTempo.getMinutos(), (int) dhmsmTempo.getSegundos(),
                                    (int) dhmsmTempo.getMilisegundos()));
                            mtv.setlDiferencia(lFechaAnterior - lFecha);

                            calculaDiasHorasMinutosSegundos(lFechaInicial, lFecha, dhmsmTempo);
                            mtvSumLapses.setText(String.format(Locale.ROOT, "%s %03d %s %02d:%02d:%02d.%03d",
                                    getResources().getString(R.string.total),
                                    (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias),
                                    (int) dhmsmTempo.getHoras(),
                                    (int) dhmsmTempo.getMinutos(), (int) dhmsmTempo.getSegundos(),
                                    (int) dhmsmTempo.getMilisegundos()));
                            mtv.setOnClickListener(mtv_OnClickListener);
                        }
                        lFechaAnterior = lFecha;
                        sbBuff.setLength(0);
                        mtv.setTextSize( 12 );
                        mtv.setTextIsSelectable(true);
                        csLL.addView(mtv);
                        lTotal = lFechaInicial - lFecha;
                    } else {
                        sbBuff.append( (char)c );
                    }
                }
                fin.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),"ERROR " + e.getMessage() +
                    " reading file",Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dFechaHora = new Date();
                long lFechaActual = dFechaHora.getTime();
                SimpleDateFormat sdfDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS",
                        Locale.ROOT);

                if (lFechaInicial == -1) lFechaInicial = lFechaActual;
                String sFecha = sdfDateFormat.format(dFechaHora);
                MyTextView mtv = new MyTextView(myContext);
                LinearLayout.LayoutParams llLP = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                llLP.setMargins(0, 2, 0, 0);
                mtv.setLayoutParams(llLP);
                mtv.setText(String.format(Locale.ROOT, "%03d | %s |", linea, sFecha));
                if (linea > 1) {
                    DiasHorasMinutosSegundosMilisegundos dhmsmTempo =
                            new DiasHorasMinutosSegundosMilisegundos();
                    calculaDiasHorasMinutosSegundos(lFechaAnterior, lFechaActual, dhmsmTempo);
                    String sDiferencia = String.format(Locale.ROOT, " %03d %s %02d:%02d:%02d.%03d",
                            (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias),
                            (int) dhmsmTempo.getHoras(), (int) dhmsmTempo.getMinutos(),
                            (int) dhmsmTempo.getSegundos(), (int) dhmsmTempo.getMilisegundos());
                    mtv.setText(String.format(Locale.ROOT, "%s %s", mtv.getText(), sDiferencia));
                    lTotal = lFechaInicial - lFechaActual;
                    mtv.setlDiferencia(lFechaAnterior - lFechaActual);
                    boolean bSubTotal = false;

                    for (int i = 0; i < csLL.getChildCount(); i++) {
                        if (csLL.getChildAt(i).isSelected()) {
                            bSubTotal = true;
                            break;
                        }
                    }

                    if (!bSubTotal) {
                        calculaDiasHorasMinutosSegundos(lTotal, 0, dhmsmTempo);

                        mtvSumLapses.setText(String.format(Locale.ROOT, "%s %03d %s %02d:%02d:%02d.%03d",
                            getResources().getString(R.string.total),
                            (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias),
                            (int) dhmsmTempo.getHoras(),
                            (int) dhmsmTempo.getMinutos(), (int) dhmsmTempo.getSegundos(),
                            (int) dhmsmTempo.getMilisegundos()));
                    }

                    mtv.setOnClickListener(mtv_OnClickListener);
                    String notification = getResources().getString(R.string.notificacion) + " " +
                            linea + " +" + sDiferencia;
                    Snackbar.make(view, notification, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                linea++;
                data = lFechaActual + "\n";
                lFechaAnterior = lFechaActual;
                mtv.setTextSize( 12 );
                mtv.setTextIsSelectable(true);
                csLL.addView(mtv);

                // Save line in file with unix timestamp in milliseconds
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

    private void calculaDiasHorasMinutosSegundos(long dIni, long dFin,
                                                 DiasHorasMinutosSegundosMilisegundos dhmsm) {
        long diff = dFin - dIni;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;
        diff = diff % secondsInMilli;

        dhmsm.setDias(elapsedDays);
        dhmsm.setHoras(elapsedHours);
        dhmsm.setMinutos(elapsedMinutes);
        dhmsm.setSegundos(elapsedSeconds);
        dhmsm.setMilisegundos(diff);
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

        if (id == R.id.action_reset) {
            try {
                String[] sFileList = this.fileList();
                if (Arrays.asList(sFileList).contains(fileName)) {
                    boolean ret = this.deleteFile(fileName);
                    if (ret) {
                        Toast.makeText(getBaseContext(), "file " + fileName + " deleted", Toast.LENGTH_LONG).show();
                        //sbText.delete(0, sbText.length());
                        //csLL.removeAllViewsInLayout();
                        int csLLGCC = csLL.getChildCount();
                        for (int i = 1; i < csLLGCC; i++) {
                            csLL.removeView(csLL.getChildAt(1));
                        }
                        linea = 1;
                        mtvSumLapses.setText("");
                        mtvSumLapses.setlDiferencia(0);
                        lFechaInicial = -1;
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

    //On click listener for dynamic myTextView
    final View.OnClickListener mtv_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            if (((MyTextView) v).getSelectionStart() != ((MyTextView) v).getSelectionEnd()) return;
            // Issue:
            // If you click over a selected line it doesn't deselect if you click again.
            // You need to select another line and click again the previous line to deselect it.
            // Executes twice this method, so I set enabled to false to avoid it.
            ((MyTextView) v).setEnabled(false);
            boolean selected = !v.isSelected();
            DiasHorasMinutosSegundosMilisegundos dhmsmTempo = new DiasHorasMinutosSegundosMilisegundos();
            String sTipo;

            v.setSelected(selected);
            v.setBackgroundColor(selected ? Color.CYAN : Color.TRANSPARENT);
            LinearLayout csLL = findViewById(R.id.cs_linear_layout);

            if (selected) {
                mtvSumLapses.setlDiferencia(mtvSumLapses.getlDiferencia() + ((MyTextView) v).getlDiferencia());
            } else {
                mtvSumLapses.setlDiferencia(mtvSumLapses.getlDiferencia() - ((MyTextView) v).getlDiferencia());
            }

            if (mtvSumLapses.getlDiferencia() == 0) {
                sTipo = getResources().getString(R.string.total);
                calculaDiasHorasMinutosSegundos(lTotal, 0, dhmsmTempo);
            } else {
                sTipo = getResources().getString(R.string.selected_lines);
                calculaDiasHorasMinutosSegundos(mtvSumLapses.getlDiferencia(), 0, dhmsmTempo);
            }

            mtvSumLapses.setText(String.format(Locale.ROOT, "%s %03d %s %02d:%02d:%02d.%03d",
                    sTipo,
                    (int) dhmsmTempo.getDias(), getResources().getString(R.string.dias),
                    (int) dhmsmTempo.getHoras(),
                    (int) dhmsmTempo.getMinutos(), (int) dhmsmTempo.getSegundos(),
                    (int) dhmsmTempo.getMilisegundos()));
            ((MyTextView) v).setEnabled(true);
        }
    };

}