package com.example.marchetrekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class DettaglioAttrazione extends AppCompatActivity {

    private Toolbar t;
    private TextView textView;
    private DatiAttrazioni da;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_attrazione);

        t=(Toolbar) findViewById(R.id.tDettaglioAttr);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Dettaglio attrazione");

        textView = (TextView) findViewById(R.id.textDettaglioAttr);
        img = (ImageView) findViewById(R.id.imageView);

        Intent i = getIntent();
        da = (DatiAttrazioni) i.getSerializableExtra("desc");

        String nome = da.getNomeattrazione();
        String descrizione = da.getDescrizione();
        String immagine = da.getImmagine();
        int drawableID = getResources().getIdentifier(immagine,"drawable",getPackageName());
        img.setImageResource(drawableID);


        textView.setText(" \n" + "Nome: " + nome + " \n"
                       + " \n" + "Descrizione: " + descrizione + "\n");
    }
}
