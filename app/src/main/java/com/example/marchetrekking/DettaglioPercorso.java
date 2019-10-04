package com.example.marchetrekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class DettaglioPercorso extends AppCompatActivity {

    private TextView t;
    private Button b;
    private Button rec;
    private String nome;
    private String mappa;
    private ImageView img;
    private DatiPercorsi dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaglio_percorso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDett) ;
        toolbar.setTitle("Dettagli percorso");
        t=(TextView) findViewById(R.id.dettaglio);
        b=(Button) findViewById(R.id.map);
        rec=(Button) findViewById(R.id.recensione);
        img = (ImageView) findViewById(R.id.imageView);

        //prendo gli extras passato dall'intent dall'activity ElencoPercorsi
        Intent i=getIntent();
        DatiPercorsi dp = (DatiPercorsi) i.getSerializableExtra("desc");

        nome =dp.getNome();
        String descrizione =dp.getDescrizione();
        mappa=dp.getMappa();
        Double lunghezza = dp.getLunghezza();
        int livello = dp.getLivello();
        String durata = dp.getDurata();
        String immagine =dp.getImmagine();
        int drawableID = getResources().getIdentifier(immagine,"drawable",getPackageName());
        img.setImageResource(drawableID);

        t.setText("Nome: " + nome + " \n"
                + " Descrizione: " + descrizione + "\n"
                + " Lunghezza: " + lunghezza + "km \n"
                + " Livello: " + livello + "\n"
                + " Durata: " + durata + "h. \n");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(DettaglioPercorso.this, Mappa.class);
                i.putExtra("mappa", mappa);
                startActivity(i);
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DettaglioPercorso.this, Recension.class);
                intent.putExtra("NomePercorso", nome);
                startActivity(intent);
            }
        });



    }
}

