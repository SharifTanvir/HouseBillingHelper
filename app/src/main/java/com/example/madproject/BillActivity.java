package com.example.madproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BillActivity extends AppCompatActivity {

    private EditText billMonth;
    private EditText billRent;
    private EditText billMeterReading;
    private EditText billElectricity;
    private EditText billTotal;
    private EditText billRentStatus;
    private EditText billElectricityStatus;

    private Button ok;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;


    private String flat_id;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        this.setTitle("Bill");

        Intent intent = getIntent();
        flat_id = intent.getStringExtra("flat_id");
        flag = intent.getStringExtra("flag");




        databaseReference = FirebaseDatabase.getInstance().getReference("bill");
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        billMonth = findViewById(R.id.et_billMonth);
        billRent = findViewById(R.id.et_billRent);
        billMeterReading = findViewById(R.id.et_billMeterReading);
        billElectricity = findViewById(R.id.et_billElectricity);
        billTotal = findViewById(R.id.et_billTotal);
        billRentStatus = findViewById(R.id.et_billRentStatus);
        billElectricityStatus = findViewById(R.id.et_billElectricityStatus);


        ok = findViewById(R.id.btn_makeBill);





        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!flag.equals("hide")){

                    databaseReference2.child("bill")
                            .orderByChild("billKey")
                            .equalTo(flag)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                                    {
                                        Bill bill = dataSnapshot1.getValue(Bill.class);
                                        bill.setMonth(billMonth.getText().toString().trim());
                                        bill.setRent(Float.valueOf(billRent.getText().toString().trim()));
                                        bill.setMeterReading(Float.valueOf(billMeterReading.getText().toString().trim()));
                                        bill.setElectricityBill(Float.valueOf(billElectricity.getText().toString().trim()));
                                        bill.setTotal(Float.valueOf(billTotal.getText().toString().trim()));
                                        bill.setRentStatus(billRentStatus.getText().toString().trim());
                                        bill.setElectricityStatus(billElectricityStatus.getText().toString().trim());
                                        databaseReference.child(bill.getBillKey()).setValue(bill);
                                        Toast.makeText(getApplicationContext(), "Information updated successfully...!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                }else {


                    String month = billMonth.getText().toString().trim();
                    Float rent = Float.valueOf(billRent.getText().toString().trim());
                    Float meter = Float.valueOf(billMeterReading.getText().toString().trim());
                    Float electricity = Float.valueOf(billElectricity.getText().toString().trim());
                    Float total = Float.valueOf(billTotal.getText().toString().trim());
                    String rentStatus = billRentStatus.getText().toString().trim();
                    String electricityStatus = billElectricityStatus.getText().toString().trim();

                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);


                    String key = databaseReference.push().getKey();
                    Bill bill = new Bill(month, rent, meter, electricity, total, rentStatus, electricityStatus, key, flat_id, thisDate);
                    databaseReference.child(key).setValue(bill);
                }
            }
        });

        if (!flag.equals("hide")){
            billDetails();

        }
    }

    private void billDetails() {

        databaseReference2.child("bill")
                .orderByChild("billKey")
                .equalTo(flag)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            Bill bill = dataSnapshot1.getValue(Bill.class);

                            billMonth.setText(bill.getMonth());
                            billRent.setText(String.valueOf(bill.getRent()));
                            billMeterReading.setText(String.valueOf(bill.getMeterReading()));
                            billElectricity.setText(String.valueOf(bill.getElectricityBill()));
                            billTotal.setText(String.valueOf(bill.getTotal()));
                            billRentStatus.setText(bill.getRentStatus());
                            billElectricityStatus.setText(bill.getElectricityStatus());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
