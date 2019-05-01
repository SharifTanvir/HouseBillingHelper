package com.example.madproject;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BillAdapter extends ArrayAdapter<Bill> {

    private Activity context;
    private List<Bill> bill_List;

    public BillAdapter(Activity context, List<Bill> bill_List) {
        super(context, R.layout.samplelayout, bill_List);
        this.context = context;
        this.bill_List = bill_List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.samplelayout,null,true);
        Bill bill = bill_List.get(position);
        TextView t1 = view.findViewById(R.id.tv_list_houseAddress);

        t1.setText(bill.getDate());
        final String f_id = bill.getFlatId();
        final String billId = bill.getBillKey();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), BillActivity.class);
                intent.putExtra("flat_id", f_id);
                intent.putExtra("flag", billId);
                context.startActivity(intent);

            }
        });
        return view;
    }
}
