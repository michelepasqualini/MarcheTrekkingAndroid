package com.example.marchetrekking;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Iterator;

public class DettaglioPercorso extends AppCompatActivity {

    private TextView t;
    private Button b;
    private Button rec;
    private String nome;
    private String mappa;
    private ImageView img;
    private DatiPercorsi dp;
    private Toolbar toolbar;
    private Button attr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaglio_percorso);
        toolbar = (Toolbar) findViewById(R.id.toolbarDett) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dettaglio percorso");
        t=(TextView) findViewById(R.id.dettaglio);
        b=(Button) findViewById(R.id.map);
        rec=(Button) findViewById(R.id.recensione);
        img = (ImageView) findViewById(R.id.imageView);
        attr = (Button)  findViewById(R.id.scopri);

        //prendo gli extras passato dall'intent dall'activity ElencoPercorsi
        Intent i=getIntent();
        dp = (DatiPercorsi) i.getSerializableExtra("desc");


        nome =dp.getNome();
        String descrizione =dp.getDescrizione();
        mappa=dp.getMappa();
        Double lunghezza = dp.getLunghezza();
        int livello = dp.getLivello();
        String durata = dp.getDurata();
        String immagine =dp.getImmagine();
        int drawableID = getResources().getIdentifier(immagine,"drawable",getPackageName());
        img.setImageResource(drawableID);


        t.setText(" \n" + "Nome: " + nome + " \n"
                + " \n" + "Descrizione: " + descrizione + "\n"
                + " \n" + "Lunghezza: " + lunghezza + "km \n"
                + " \n" + "Livello Difficoltà: " + livello + "\n"
                + " \n" + "Durata: " + durata + "h. \n");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(DettaglioPercorso.this, Mappa.class);
                i.putExtra("mappa", mappa);
                startActivity(i);
            }
        });


        if(dp.getId() >= 1 ){
            rec.setVisibility(View.GONE);
            attr.setVisibility(View.GONE);
        }else {
            rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DettaglioPercorso.this, Recension.class);
                    intent.putExtra("NomePercorso", nome);
                    startActivity(intent);
                }
            });

            attr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DettaglioPercorso.this, Attrazioni.class);
                    intent.putExtra("NomePercorso", nome);
                    startActivity(intent);
                }
            });
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        if(dp.getId() == 0){
            toolbar.getMenu().findItem(R.id.delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>

        switch (item.getItemId()){

            case R.id.delete:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(DettaglioPercorso.this);
                builder1.setMessage("Cancellare percorso?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                    HttpURLConnection client = null;
                                    URL url;
                                    try {
                                        url = new URL("http://marchetrekking.altervista.org/cancellaPercorso.php");

                                        client = (HttpURLConnection) url.openConnection();
                                        client.setRequestMethod("POST");
                                        client.setDoOutput(true);
                                        client.setDoInput(true);
                                        OutputStream out = new BufferedOutputStream(client.getOutputStream());
                                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

                                        String data = URLEncoder.encode("percorso", "UTF-8")
                                                + "=" + URLEncoder.encode(Integer.toString(dp.getId()) , "UTF-8");

                                        writer.write(data);
                                        writer.flush();
                                        writer.close();
                                        out.close();

                                        InputStream in = client.getInputStream();
                                        String json_string = ReadResponse.readStream(in).trim();

                                        if(!json_string.equals("ok")) {
                                            Toast.makeText(DettaglioPercorso.this, "Errore!", Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(DettaglioPercorso.this, "Percorso cancellato", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent p = new Intent(DettaglioPercorso.this, ElencoPercorsi.class);
                                            p.putExtra("miei", true);
                                            startActivity(p);

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
                        });



                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog alert = builder1.create();
                alert.show();
                return true;

            default:
                return true;

        }
    }
}

