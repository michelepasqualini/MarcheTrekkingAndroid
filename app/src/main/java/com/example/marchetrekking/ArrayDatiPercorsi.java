package com.example.marchetrekking;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArrayDatiPercorsi extends ArrayAdapter<DatiPercorsi> {

    private Activity context;
    private ArrayList<DatiPercorsi> dp;

    //metodo costruttore
    public ArrayDatiPercorsi(Activity context, ArrayList<DatiPercorsi> dp) {
        super(context, R.layout.rowlv,dp);

        this.context=context;
        this.dp=dp;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.rowlv,null);

        //istanzio le textview legandole a quelle del layout di riferimento rowlv
        TextView nome = (TextView) rowView.findViewById(R.id.perc);
        TextView desc = (TextView) rowView.findViewById(R.id.descrizione);
        ImageView img = (ImageView) rowView.findViewById(R.id.imageView3);

        //imposto il contenuto delle textview
        nome.setText(dp.get(position).getNome());
        desc.setText(dp.get(position).getDescrizione());
        String img2 = dp.get(position).getImmagine();
        int drawableID = context.getResources().getIdentifier(img2,"drawable", context.getPackageName());
        img.setImageResource(drawableID);

        return rowView;

    }
}
