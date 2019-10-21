package com.example.marchetrekking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.MonthDay;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    private String user;
    private String n;
    private String c;
    private String dnascita;
    private String telef;
    private String mail;
    private String pass;


    private EditText id, nome, cognome, data, email, password, telefono;
    private Button invia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReg) ;
        toolbar.setTitle("Registrati");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session =new SessionManager(this);
        id = (EditText) findViewById(R.id.utente);
        nome = (EditText) findViewById(R.id.nome);
        cognome = (EditText) findViewById(R.id.cognome);
        data = (EditText) findViewById(R.id.data);
        //data.setText("2005/12/31");
        telefono = (EditText) findViewById(R.id.telefono);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        invia = (Button) findViewById(R.id.button);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(2005, 11, 31);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy/MM/dd", Locale.US);
        data.setText(simpleDateFormat.format(calendar.getTime()));


        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                data.setText(simpleDateFormat.format(calendar.getTime()));

                }
        };

        data.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new DatePickerDialog(MainActivity.this,datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }

        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this,datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = id.getText().toString();
                n = nome.getText().toString();
                String nomeClean = n.replace("'", "''");
                c = cognome.getText().toString();
                String cognomeClean = c.replace("'", "''");
                dnascita = data.getText().toString();
                telef = telefono.getText().toString();
                mail = email.getText().toString();
                pass = password.getText().toString();

                String d[] = dnascita.split("/");
                if(Integer.parseInt(d[0]) >= 2005)
                {
                    Toast.makeText(MainActivity.this, "Data di nascita non valida", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isNullOrEmpty(user) && !isNullOrEmpty(n) && !isNullOrEmpty(c) && !isNullOrEmpty(dnascita) && !isNullOrEmpty(telef) && !isNullOrEmpty(mail) &&!isNullOrEmpty(pass) ) {


                    HttpURLConnection client = null;
                    URL url;
                    try {
                        // se la richiesta è POST
                        url = new URL("http://marchetrekking.altervista.org/aggiungi_utente.php");
                        client = (HttpURLConnection) url.openConnection();
                        client.setRequestMethod("POST");
                        client.setDoOutput(true);
                        client.setDoInput(true);
                        // write data in request
                        OutputStream out = new BufferedOutputStream(client.getOutputStream());
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                        String data = URLEncoder.encode("mail", "UTF-8")
                                + "=" + URLEncoder.encode(mail, "UTF-8");

                        data += "&" + URLEncoder.encode("pass", "UTF-8") + "="
                                + URLEncoder.encode(pass, "UTF-8");


                        data += "&" + URLEncoder.encode("n", "UTF-8") + "="
                                + URLEncoder.encode(n, "UTF-8");


                        data += "&" + URLEncoder.encode("c", "UTF-8") + "="
                                + URLEncoder.encode(c, "UTF-8");


                        data += "&" + URLEncoder.encode("dnascita", "UTF-8") + "="
                                + URLEncoder.encode(dnascita, "UTF-8");


                        data += "&" + URLEncoder.encode("telef", "UTF-8") + "="
                                + URLEncoder.encode(telef, "UTF-8");


                        data += "&" + URLEncoder.encode("user", "UTF-8") + "="
                                + URLEncoder.encode(user, "UTF-8");


                        writer.write(data);
                        writer.flush();
                        writer.close();
                        out.close();

                        InputStream in = client.getInputStream();
                        String json_string = ReadResponse.readStream(in).trim();

                        if (json_string.equals("1")) {
                            session.createSession(user, pass);
                            Toast.makeText(MainActivity.this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (client != null) {
                            client.disconnect();
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Completa i campi",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    public void onBackPressed() {
        Intent setIntent = new Intent(MainActivity.this, Principale.class);
        startActivity(setIntent);
        finish();
    }

    public boolean isNullOrEmpty(String s){
        return s == null || s.isEmpty();
    }
}


