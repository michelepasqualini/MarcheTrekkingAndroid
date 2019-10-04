package com.example.marchetrekking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class activity_login extends AppCompatActivity {
    SessionManager session;
    EditText u, p;
    Button log;
    TextView t;
    String ulog,plog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAcc) ;
        toolbar.setTitle("Accedi");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        u=(EditText) findViewById(R.id.nomeUtente);
        p=(EditText) findViewById(R.id.pass);
        log = (Button) findViewById(R.id.login);

        session =new SessionManager(this);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ulog = u.getText().toString();
                plog = p.getText().toString();

                HttpURLConnection client = null;
                URL url;
                try {
                    // se la richiesta è POST
                    url = new URL("http://marchetrekking.altervista.org/login.php");
                    client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    client.setDoOutput(true);
                    client.setDoInput(true);
                    // write data in request
                    OutputStream out = new BufferedOutputStream(client.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    String data = URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(ulog, "UTF-8");

                    data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                            + URLEncoder.encode(plog, "UTF-8");


                    writer.write(data);
                    writer.flush();
                    writer.close();
                    out.close();

                    InputStream in = client.getInputStream();
                    String json_string = ReadResponse.readStream(in).trim();

                    //t.append(json_string);
                    if(json_string.equals("1")){

                        session.createSession(ulog, plog);

                        Intent log = new Intent(activity_login.this, Home.class);
                        startActivity(log);
                    }
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(activity_login.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Errore!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                    /*if (json_string.equals("1")) {
                        Toast.makeText(activity_login.this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity_login.this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (client != null) {
                        client.disconnect();
                    }
                }
            }
        });



    }

    public void onBackPressed() {
        Intent setIntent = new Intent(activity_login.this, Principale.class);

        startActivity(setIntent);

        return;
    }
}

