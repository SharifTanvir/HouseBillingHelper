package com.example.madproject;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FlatAdapter extends ArrayAdapter <Flat>{


    private Activity context;
    private List<Flat> flat_List;

    public FlatAdapter(Activity context, List<Flat> flat_List) {
        super(context, R.layout.samplelayout, flat_List);
        this.context = context;
        this.flat_List = flat_List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.samplelayout,null,true);
        Flat flat = flat_List.get(position);
        TextView t1 = view.findViewById(R.id.tv_list_houseAddress);

        t1.setText(flat.getFlatId());
        final String f_id = flat.getKeyValue();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), RentActivity.class);
                intent.putExtra("flat_id", f_id);
                context.startActivity(intent);

            }
        });
        return view;
    }
}
