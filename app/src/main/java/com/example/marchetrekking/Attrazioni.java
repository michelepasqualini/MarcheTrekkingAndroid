package com.example.marchetrekking;

import android.content.Intent;
import android.os.StrictMode;
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


public class Attrazioni extends AppCompatActivity {

    private LinearLayout ly;
    private ListView lv;
    private ArrayList aAttrazioni;

    private ArrayAdapter<String> arrayAd;
    private ArrayDatiAttrazioni listAdapter;

    private Toolbar t;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        session=new SessionManager(this);
        HashMap<String,String> utente = session.getUserDetail();

        setContentView(R.layout.activity_attrazioni);
        t=(Toolbar) findViewById(R.id.toolbarAttrazioni);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Attrazioni");

        lv=(ListView) findViewById(R.id.lista) ;
        ly=(LinearLayout) findViewById(R.id.lypercosi) ;

        Intent i=getIntent();

        HttpURLConnection client = null;
        URL url;
        try {

            url = new URL("http://marchetrekking.altervista.org/attrazioni.php");
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setDoInput(true);
            OutputStream out = new BufferedOutputStream(client.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String nome = i.getExtras().getString("NomePercorso");
            String nClean = nome.replace("'", "''");
            String data = URLEncoder.encode("percorso", "UTF-8")
                    + "=" + URLEncoder.encode(nClean , "UTF-8");

            writer.write(data);
            writer.flush();
            writer.close();
            out.close();
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in);
            if(!json_string.equals("0[]\n")) {
                JSONObject json_data = convert2JSON(json_string);
                fill_listview(json_data/*, i.getBooleanExtra("miei", false)*/);
            }
            else{
                Toast.makeText(this, "Nessuna attrazione presente", Toast.LENGTH_SHORT).show();
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


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int a = position;
                DatiAttrazioni d= listAdapter.getItem(position);
                String fe = d.getNomeattrazione();
                Intent intent = new Intent(Attrazioni.this, DettaglioAttrazione.class);
                intent.putExtra("desc", d);
                startActivity(intent);
            }
        });

    }

    private void fill_listview(JSONObject json_data/*, boolean miei*/){
        ArrayList<DatiAttrazioni> dattr = new ArrayList<>(); //creazione array di attrazioni
        Iterator<String> iter = json_data.keys();//iterator che permette scorrere l'arraylist
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = json_data.getJSONObject(key);//valore della chiave, in questo caso nomepercorso
                //prelevo i dati ottenuti in risposta dal php
                int id = value.getInt("Id");
                String nome = value.getString("Nome");
                String descrizione = value.getString("Descrizione");
                String immagine = value.getString("Immagine");
                String NomePercorso = value.getString("NomePercorso");

                //istanziazione di un oggetto per ogni attrazione
                DatiAttrazioni da;
                da = new DatiAttrazioni(id, nome, descrizione, immagine, NomePercorso);

                //aggiunta dell'oggetto all'arraylist
                dattr.add(da);
            }catch (JSONException e) {
                // Something went wrong!
            }
        }
        listAdapter = new ArrayDatiAttrazioni(Attrazioni.this, dattr);

        lv.setAdapter(listAdapter);
    }

    private JSONObject convert2JSON(String json_data){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json_data);

        } catch (Throwable t) {

        }
        return obj;
    }
}
