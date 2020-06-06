package com.example.marchetrekking;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class Emergenza extends AppCompatActivity {

    private Toolbar t;
    ListView lst;
    String[] nameem={"Numero unico emergenza","Emergenza Ambientale","Emergenza Sanitaria","Vigili del fuoco"};
    String[] number = {"112","1515","118","115"};
    Integer[] imageid = {R.drawable.emergenzaunica,R.drawable.ambientale,R.drawable.kit,R.drawable.fiamma} ;
    Integer[] imageid2 = {R.drawable.call,R.drawable.call,R.drawable.call,R.drawable.call} ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergenza);

        t = (Toolbar) findViewById(R.id.toolbarEme);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Emergenza");

        lst = (ListView) findViewById(R.id.listview);
        Custom_listview customListView = new Custom_listview(this,nameem, number, imageid, imageid2);
        lst.setAdapter(customListView);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = null;
                Intent i = null;
                switch (position){
                    case 0:
                        uri = Uri.parse("tel:112");
                        i = new Intent(Intent.ACTION_DIAL,uri );
                        startActivity(i);
                        break;

                    case 1:
                        uri = Uri.parse("tel:1515");
                        i = new Intent(Intent.ACTION_DIAL,uri );
                        startActivity(i);
                        break;


                    case 2:
                        uri = Uri.parse("tel:118");
                        i = new Intent(Intent.ACTION_DIAL,uri );
                        startActivity(i);
                        break;

                    case 3:
                        uri = Uri.parse("tel:115");
                        i = new Intent(Intent.ACTION_DIAL,uri );
                        startActivity(i);
                        break;

                    default:
                        break;

                }


            }
        });


    }
}
