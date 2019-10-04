package com.example.marchetrekking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class aggPercorso extends AppCompatActivity {

    private LinearLayout lineary;
    private Button agg, continua, indietro;
    private ListView list;
    private ArrayList array;
    private ArrayList<String> latitudine, longitudine;

    private ArrayAdapter<String> arrayAdapter;

    private LinearLayout ly;

    private EditText lat, longi;
    private HashMap<String,String> punti = new HashMap<>();
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_percorso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAggPercorso) ;
        toolbar.setTitle("Aggiungi Percorso");

        //list=(ListView) findViewById(R.id.listaAggPercorsi) ;
        agg=(Button) findViewById(R.id.aggp);
        continua =(Button) findViewById(R.id.continua);
        indietro = (Button) findViewById(R.id.undo);
        continua.setEnabled(false);
        indietro.setEnabled(false);
        lineary=(LinearLayout) findViewById(R.id.lyaggp) ;
        ly = (LinearLayout) findViewById(R.id.layoutAggPerc) ;

        lat = (EditText) findViewById(R.id.insLat);
        longi = (EditText) findViewById(R.id.insLong);

        array = new ArrayList();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_crea_percorso, R.id.tappa, array);
        //list.setAdapter(arrayAdapter);


        //final ArrayList<TextView> latitudine=new ArrayList<TextView>();
        //ArrayList<TextView> longitudine=new ArrayList<TextView>();


        //int a=0;
        agg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continua.setEnabled(true);
                indietro.setEnabled(true);
                //latitudine.add(tx);
                //latitudine.add(ty);
                LinearLayout row = new LinearLayout(aggPercorso.this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                p.setMargins(20,0,20,0);
                row.setLayoutParams(p);
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView= new TextView(aggPercorso.this);
                textView.setText("Tappa" + a);
                EditText et1 = new EditText(aggPercorso.this);//lat
                EditText et2 = new EditText(aggPercorso.this);//long
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                et1.setLayoutParams(params);
                et2.setLayoutParams(params);
                textView.setLayoutParams(params);
                row.addView(textView);
                row.addView(et1);
                row.addView(et2);
                ly.addView(row);

                //tx.setTextSize(40);
                //tlat.setText("text");

                //array.add("Tappa " + a);
                //list.setAdapter(arrayAdapter);

                //lineary.addView(tx);
                //lineary.invalidate();
                a++;
                //Toast.makeText(aggPercorso.this, "hai fatto click", Toast.LENGTH_LONG).show();
            }

        });

        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cont =true;
                latitudine = new ArrayList<String>();
                longitudine = new ArrayList<String>();
                for(int x=0; x < ly.getChildCount(); x++) {
                    LinearLayout view= (LinearLayout) ly.getChildAt(x);
                    EditText e1 = (EditText) view.getChildAt(1);
                    latitudine.add(e1.getText().toString());
                    EditText e2 = (EditText) view.getChildAt(2);
                    longitudine.add(e2.getText().toString());
                    punti.put(e1.getText().toString(),e2.getText().toString());
                    //LinearLayout l2 = (LinearLayout) l.getChildAt(x);
                    /*EditText e1 = (EditText) view.findViewById(R.id.insLat);
                    EditText e2 = (EditText) view.findViewById(R.id.insLong;
                    String lat = et1.getText().toString();
                    String longi = e2.getText().toString();
                    punti.put(lat,longi);*/
                }


                for(int i = 0; i < ly.getChildCount(); i ++){
                    if(!isNullOrEmpty(latitudine.get(i)) && !isNullOrEmpty(longitudine.get(i))){
                        cont = true;
                    }else {
                        cont = false;
                        break;
                    }
                }



                if(cont == true){
                    Intent intent = new Intent(aggPercorso.this, NuovoPercorsoDesc.class);
                    intent.putExtra("latitudine", latitudine);
                    intent.putExtra("longitudine", longitudine);
                    startActivity(intent);
                }else{
                    Toast.makeText(aggPercorso.this, "Completa i campi",Toast.LENGTH_SHORT).show();
                }
            }

            public boolean isNullOrEmpty(String s){
                    return s == null || s.isEmpty();
            }
        });


        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = a ;
                ly.removeViewAt(i-1);
                a--;

                if(ly.getChildCount() == 0){
                    indietro.setEnabled(false);
                    continua.setEnabled(false);
                }

            }
        });

    }

}

