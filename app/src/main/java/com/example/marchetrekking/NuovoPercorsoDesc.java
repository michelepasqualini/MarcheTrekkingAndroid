package com.example.marchetrekking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NuovoPercorsoDesc extends AppCompatActivity {
    EditText nomeP,descP,lungP,livP,durataP;
    SessionManager session;
    HashMap<String, String> hashMap;
    String map="";
    private ArrayList<String> latitudine, longitudine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_descrizione_percorso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2) ;
        toolbar.setTitle("Crea nuovo percorso");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        latitudine= (ArrayList<String>) intent.getSerializableExtra("latitudine");

        longitudine= (ArrayList<String>) intent.getSerializableExtra("longitudine");

        for (int i = 0; i < latitudine.size(); i++) {

                map += latitudine.get(i)+ "," + longitudine.get(i) + ",";
        }

        nomeP=findViewById(R.id.nomeP);
        descP =findViewById(R.id.descP);
        lungP=findViewById(R.id.lungP);
        livP=findViewById(R.id.livP);
        durataP=findViewById(R.id.durataP);
        Button crea = findViewById(R.id.nuovop);

        session=new SessionManager(this);

        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> utente = session.getUserDetail();
                String n = utente.get(SessionManager.NAME);
                String nP = nomeP.getText().toString();
                String dP = descP.getText().toString();
                String luP = lungP.getText().toString();
                String liP = livP.getText().toString();
                String duP = durataP.getText().toString();

                if(!isNullOrEmpty(n) && !isNullOrEmpty(nP) && !isNullOrEmpty(dP) && !isNullOrEmpty(luP) && !isNullOrEmpty(liP) && !isNullOrEmpty(duP)) {

                    HttpURLConnection client = null;
                    URL url;
                    try {
                        // se la richiesta è POST
                        url = new URL("http://marchetrekking.altervista.org/aggiungi_percorso.php");
                        client = (HttpURLConnection) url.openConnection();
                        client.setRequestMethod("POST");
                        client.setDoOutput(true);
                        client.setDoInput(true);
                        // write data in request
                        OutputStream out = new BufferedOutputStream(client.getOutputStream());
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                        String data = URLEncoder.encode("nome", "UTF-8")
                                + "=" + URLEncoder.encode(nP, "UTF-8");

                        data += "&" + URLEncoder.encode("desc", "UTF-8") + "="
                                + URLEncoder.encode(dP, "UTF-8");


                        data += "&" + URLEncoder.encode("lunghezza", "UTF-8") + "="
                                + URLEncoder.encode(luP, "UTF-8");


                        data += "&" + URLEncoder.encode("livello", "UTF-8") + "="
                                + URLEncoder.encode(liP, "UTF-8");


                        data += "&" + URLEncoder.encode("durata", "UTF-8") + "="
                                + URLEncoder.encode(duP, "UTF-8");


                        data += "&" + URLEncoder.encode("map", "UTF-8") + "="
                                + URLEncoder.encode(map, "UTF-8");


                        data += "&" + URLEncoder.encode("user", "UTF-8") + "="
                                + URLEncoder.encode(n, "UTF-8");

                        writer.write(data);
                        writer.flush();
                        writer.close();
                        out.close();

                        InputStream in = client.getInputStream();
                        String json_string = ReadResponse.readStream(in).trim();

                        if (json_string.equals("1")) {
                            //Toast.makeText(NuovoPercorsoDesc.this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NuovoPercorsoDesc.this, Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(NuovoPercorsoDesc.this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (client != null) {
                            client.disconnect();
                        }
                    }
                }else{
                    Toast.makeText(NuovoPercorsoDesc.this, "Completa i campi",Toast.LENGTH_SHORT).show();

                }


            }

        });

    }

    public boolean isNullOrEmpty(String s){
        return s == null || s.isEmpty();
    }




}

