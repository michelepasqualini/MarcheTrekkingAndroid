package com.example.marchetrekking;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ElencoPercorsi extends AppCompatActivity {

    private LinearLayout ly;
    private ListView lv;
    private ArrayList aPercorsi;

    private ArrayAdapter<String> arrayAd;
    private ArrayDatiPercorsi listAdapter;

    private Toolbar t;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session=new SessionManager(this);
        HashMap<String,String> utente = session.getUserDetail();
        setContentView(R.layout.activity_elenco_percorsi);
        t=(Toolbar) findViewById(R.id.toolbarElPer);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Percorsi");


        lv=(ListView) findViewById(R.id.lista) ;
        ly=(LinearLayout) findViewById(R.id.lypercosi) ;

        Intent i=getIntent();

        HttpURLConnection client = null;
        try {
            URL url;
            if(i.getBooleanExtra("miei", false)) {
                url = new URL("http://marchetrekking.altervista.org/myPercorsi.php?username=" + utente.get(SessionManager.NAME));
            }else{
                url = new URL("http://marchetrekking.altervista.org/percorsi.php");
            }
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in);
            if(!json_string.equals("0[]\n")) {
                JSONObject json_data = convert2JSON(json_string);
                fill_listview(json_data, i.getBooleanExtra("miei", false));
            }
            else{
                Toast.makeText(this, "Nessuna percorso presente", Toast.LENGTH_SHORT).show();
                finish();
                //Intent go = new Intent(ElencoPercorsi.this, Home.class);
                //startActivity(go);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (client!= null){
                client.disconnect();
            }
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int a = position;
                DatiPercorsi d=listAdapter.getItem(position);
                String fe=d.getNome();
                Intent intent = new Intent(ElencoPercorsi.this, DettaglioPercorso.class);
                intent.putExtra("desc", d);
                startActivity(intent);
            }
        });

    }

    private void fill_listview(JSONObject json_data, boolean miei){

        ArrayList<DatiPercorsi> dperc=new ArrayList<>(); //creazione array di percorsi
        Iterator<String> iter = json_data.keys();//iterator che permette scorrere l'arraylist
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = json_data.getJSONObject(key);//valore della chiave, in questo caso nome percorso
                //prelevo i dati ottenuti in risposta dal php
                int id;
                String nome =value.getString("Nome");
                String descrizione =value.getString("Descrizione");
                String mappa=value.getString("Mappa");
                Double lunghezza = Double.parseDouble(value.getString("Lunghezza"));
                int livello =Integer.parseInt(value.getString("Livello"));
                String durata = value.getString("Durata");
                String immagine;
                DatiPercorsi dp;
                if(!miei) {
                    immagine= value.getString("Immagine");
                    //istanziazione di un oggetto per ogni percorso
                    dp =new DatiPercorsi(nome, mappa, descrizione, lunghezza, livello, durata, immagine);

                } else{
                    immagine= "logo";
                    //istanziazione di un oggetto per ogni percorso attraverso l'id
                    id = Integer.parseInt(value.getString("idPercUtente"));
                    dp=new DatiPercorsi(id, nome, mappa, descrizione, lunghezza, livello, durata, immagine);
                }


                //aggiunta dell'oggetto all'arraylist
                dperc.add(dp);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
        listAdapter = new ArrayDatiPercorsi(ElencoPercorsi.this, dperc);

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
