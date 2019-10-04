package com.example.marchetrekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

public class Home extends AppCompatActivity {
    SessionManager session;
    private Toolbar t;
    Button lo;
    Button percorsi, insperc, mypercorsi;
    Button recension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session=new SessionManager(this);
        HashMap<String,String> utente = session.getUserDetail();
        String n = utente.get(SessionManager.NAME);
        String p = utente.get(SessionManager.PASS);

        t= (Toolbar)findViewById(R.id.toolbarHome);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Welcome " + n);

        percorsi = (Button) findViewById(R.id.percorsi);
        insperc=(Button) findViewById(R.id.insPercorso);
        recension =(Button) findViewById(R.id.fai);
        mypercorsi=(Button) findViewById(R.id.mypercorsi);



        percorsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Home.this, ElencoPercorsi.class);
                startActivity(p);
            }
        });

        insperc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Home.this, aggPercorso.class);
                startActivity(p);
            }
        });

        recension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Home.this, add_recension.class);
                startActivity(p);
            }
        });

        //miei percorsi
        mypercorsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Home.this, ElencoPercorsi.class);
                p.putExtra("miei", true);
                startActivity(p);

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>

        switch (item.getItemId()){

            case R.id.l:
                session.logout();
                return true;

            default:
                return true;

        }
    }
}