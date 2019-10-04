package com.example.marchetrekking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;

public class Recension extends AppCompatActivity {

    SessionManager session;
    private LinearLayout ly;
    private ListView lv;
    private ArrayDatiRecensioni listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recension);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRec) ;
        toolbar.setTitle("Recensioni");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session=new SessionManager(this);
        HashMap<String,String> utente = session.getUserDetail();

        lv=(ListView) findViewById(R.id.listRec) ;
        ly=(LinearLayout) findViewById(R.id.recPercorsi) ;

        Intent i=getIntent();


        HttpURLConnection client = null;
        URL url;
        try {
            url = new URL("http://marchetrekking.altervista.org/recensioni.php");

            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setDoInput(true);
            OutputStream out = new BufferedOutputStream(client.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            String data = URLEncoder.encode("percorso", "UTF-8")
                    + "=" + URLEncoder.encode(i.getExtras().getString("NomePercorso") , "UTF-8");

            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if(!json_string.equals("0[]")) {
                JSONObject json_data = convert2JSON(json_string);
                fill_listview(json_data);
            }
            else{
                Toast.makeText(this, "Nessuna recensione presente", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (client!= null){
                client.disconnect();
            }
        }
    }

    private void fill_listview(JSONObject json_data){

        ArrayList<DatiRecensioni> drec=new ArrayList<>(); //creazione array di percorsi
        Iterator<String> iter = json_data.keys();//iterator che permette scorrere l'arraylist
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = json_data.getJSONObject(key);//valore della chiave, in questo caso nome percorso
                //prelevo i dati ottenuti in risposta dal php
                String nome = value.getString("NomePercorso");
                String utente =value.getString("UserName");
                String descrizione =value.getString("Recensione");

                //istanziazione di un oggetto per ogni percorso
                DatiRecensioni dr=new DatiRecensioni(nome, descrizione, utente);
                //aggiunta dell'oggetto all'arraylist
                drec.add(dr);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
        listAdapter = new ArrayDatiRecensioni(Recension.this, drec);

        lv.setAdapter(listAdapter);

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
}

