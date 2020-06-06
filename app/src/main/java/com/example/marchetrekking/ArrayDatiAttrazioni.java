package com.example.marchetrekking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArrayDatiAttrazioni extends ArrayAdapter<DatiAttrazioni> {

    private Activity context;
    private ArrayList<DatiAttrazioni> da;

    public ArrayDatiAttrazioni(Activity context, ArrayList<DatiAttrazioni> da){
        super(context, R.layout.rowlv,da);

        this.context=context;
        this.da=da;
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
        nome.setText(da.get(position).getNomeattrazione());
        desc.setText(da.get(position).getDescrizione());
        String img2 = da.get(position).getImmagine();
        int drawableID = context.getResources().getIdentifier(img2,"drawable", context.getPackageName());
        img.setImageResource(drawableID);

        return rowView;

    }
}

