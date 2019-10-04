package com.example.marchetrekking;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

public class Principale extends AppCompatActivity {
    SessionManager session;
    Button btnreg, btnaccedi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(this);
        session.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
        btnreg = (Button) findViewById(R.id.registrati);
        btnaccedi = (Button) findViewById(R.id.accedi);

        //img.loadUrl("http://marchetrekking.altervista.org/immagini/1.jpg");

        btnaccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(Principale.this, activity_login.class);
                startActivity(log);
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrati = new Intent(Principale.this, MainActivity.class);
                startActivity(registrati);
            }
        });
    }





}

