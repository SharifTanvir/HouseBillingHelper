package com.example.madproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HouseAdapter extends ArrayAdapter<House> {

    private Activity context;
    private List<House> house_List;

    public HouseAdapter(Activity context, List<House> house_List) {
        super(context, R.layout.samplelayout, house_List);
        this.context = context;
        this.house_List = house_List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.samplelayout,null,true);
        House house = house_List.get(position);
        TextView t1 = view.findViewById(R.id.tv_list_houseAddress);

        t1.setText(house.getAddress());
        final String h_id = house.getkeyValue();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), FlatActivity.class);

                intent.putExtra("House_id", h_id);
                context.startActivity(intent);

            }
        });
        return view;
    }
}
