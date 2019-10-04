package com.example.marchetrekking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marchetrekking.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class add_recension extends AppCompatActivity {

    private Toolbar t;
    SessionManager session;

    TextView textView1;
    TextView textView2;
    Spinner spinner;
    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recension);
        t=(Toolbar) findViewById(R.id.toolbarRec);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Recensione");

        textView1 = (TextView) findViewById(R.id.textView10);
        textView2 = (TextView) findViewById(R.id.textView13);
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.button2);
        editText = (EditText) findViewById(R.id.editTextR) ;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection client = null;
        try {
            URL url = new URL("http://marchetrekking.altervista.org/percorsi.php");
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in);
            JSONObject json_data = convert2JSON(json_string);
            fill_spinner(json_data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (client!= null){
                client.disconnect();
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String percorso =  spinner.getSelectedItem().toString();
                String recensione = editText.getText().toString();
                session=new SessionManager(add_recension.this);
                HashMap<String,String> utente = session.getUserDetail();
                String user = utente.get(SessionManager.NAME);


                if(!isNullOrEmpty(percorso) && !isNullOrEmpty(recensione)){
                    HttpURLConnection client = null;
                    URL url;
                    try {
                        // se la richiesta è POST
                        url = new URL("http://marchetrekking.altervista.org/aggiungi_recensione.php");
                        client = (HttpURLConnection) url.openConnection();
                        client.setRequestMethod("POST");
                        client.setDoOutput(true);
                        client.setDoInput(true);
                        // write data in request
                        OutputStream out = new BufferedOutputStream(client.getOutputStream());
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                        String data = URLEncoder.encode("percorso", "UTF-8")
                                + "=" + URLEncoder.encode(percorso, "UTF-8");

                        data += "&" + URLEncoder.encode("recensione", "UTF-8") + "="
                                + URLEncoder.encode(recensione, "UTF-8");

                        data += "&" + URLEncoder.encode("user", "UTF-8") + "="
                                + URLEncoder.encode(user, "UTF-8");


                        writer.write(data);
                        writer.flush();
                        writer.close();
                        out.close();

                        InputStream in = client.getInputStream();
                        String json_string = ReadResponse.readStream(in).trim();

                        if (json_string.equals("1")) {
                            Toast.makeText(add_recension.this, "Recensione Aggiunta", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(add_recension.this, Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(add_recension.this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (client != null) {
                            client.disconnect();
                        }
                    }

                }else{
                    Toast.makeText(add_recension.this, "Completa i campi",Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    private void fill_spinner(JSONObject json_data){
        ArrayList<DatiPercorsi> dperc=new ArrayList<>(); //creazione array di percorsi
        Iterator<String> iter = json_data.keys();//iterator che permette scorrere l'arraylist
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = json_data.getJSONObject(key);//valore della chiave, in questo caso nome percorso
                //prelevo i dati ottenuti in risposta dal php
                String nome =value.getString("Nome");

                DatiPercorsi dp=new DatiPercorsi(nome);
                dperc.add(dp);

            } catch (JSONException e) {
                // Something went wrong!
            }
        }

        ArrayList<String> nomi = new ArrayList<>();
        ArrayAdapter<String> arrayAd;
        for(int i = 0; i < dperc.size(); i++){
            nomi.add(dperc.get(i).getNome());
        }

        arrayAd = new ArrayAdapter<String>(add_recension.this, android.R.layout.simple_spinner_item, nomi);
        arrayAd.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAd);

    }

    private JSONObject convert2JSON(String json_data){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json_data);
            //Log.d("My App", obj.toString());
        } catch (Throwable t) {
            //Log.e("My App", "Could not parse malformed JSON: \"" + json_data + "\"");
        }
        return obj;
    }

    public boolean isNullOrEmpty(String s){
        return s == null || s.isEmpty();
    }




}
