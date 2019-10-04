package com.example.marchetrekking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArrayDatiRecensioni extends ArrayAdapter<DatiRecensioni> {
    private Activity context;
    private ArrayList<DatiRecensioni> dr;

    //metodo costruttore
    public ArrayDatiRecensioni(Activity context, ArrayList<DatiRecensioni> dr) {
        super(context, R.layout.rowlv2,dr);

        this.context=context;
        this.dr=dr;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.rowlv2,null);

        //istanzio le textview legandole a quelle del layout di riferimento rowlv
        TextView desc = (TextView) rowView.findViewById(R.id.p);
        TextView nome = (TextView) rowView.findViewById(R.id.desc);


        //imposto il contenuto delle textview
        nome.setText(dr.get(position).getUtente());
        desc.setText(dr.get(position).getDescrizione());

        return rowView;

    }
}
