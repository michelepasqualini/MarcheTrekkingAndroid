package com.example.marchetrekking;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_listview extends ArrayAdapter<String> {

    private String[] nameem;
    private String[] number;
    private Integer[] imageid;
    private Integer[] imageid2;
    private Activity context;

    public Custom_listview(Activity context, String[] nameem, String[] number,Integer[] imageid, Integer[] imageid2) {
        super(context, R.layout.listview_layout, nameem);
        this.context=context;
        this.nameem=nameem;
        this.number=number;
        this.imageid=imageid;
        this.imageid2=imageid2;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View r = convertView;
        ViewHolder viewHolder = null;
        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) r.getTag();
        }

        viewHolder.ivw.setImageResource(imageid[position]);
        viewHolder.tvw1.setText(nameem[position]);
        viewHolder.tvw2.setText(number[position]);
        viewHolder.ivw2.setImageResource(imageid2[position]);


        return r;

    }

    class ViewHolder{
        TextView tvw1;
        TextView tvw2;
        ImageView ivw;
        ImageView ivw2;
        ViewHolder(View v){
            tvw1=v.findViewById(R.id.textView);
            tvw2=v.findViewById(R.id.textViewNumber);
            ivw=v.findViewById(R.id.imageView);
            ivw2=v.findViewById(R.id.imageView2);
        }
    }
}
