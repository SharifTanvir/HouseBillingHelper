package com.example.madproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    private TextView newHouse,showHouseList;
    private EditText tot_floor,tot_unit,house_address;
    private Button addHouse;
    private ListView houseList;

    String uid;

    private List<House> houseListInfo;
    private  HouseAdapter houseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setTitle("Dashboard");
        databaseReference = FirebaseDatabase.getInstance().getReference("house");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
             uid = user.getUid();
        }

        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        houseListInfo = new ArrayList<>();
        houseAdapter = new HouseAdapter(HomeActivity.this,houseListInfo);

        houseList= findViewById(R.id.dv_houseList);

        newHouse = findViewById(R.id.tv_addNewHome);
        showHouseList = findViewById(R.id.tv_houseAddress);
        tot_floor= findViewById(R.id.et_nmFloor);
        tot_unit= findViewById(R.id.et_nmUnit);
        house_address= findViewById(R.id.et_houseAddress);
        addHouse = findViewById(R.id.btn_addHouse);

        newHouse.setOnClickListener(this);
        showHouseList.setOnClickListener(this);
        addHouse.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_addNewHome:

                tot_floor.setVisibility(View.VISIBLE);
                tot_unit.setVisibility(View.VISIBLE);
                house_address.setVisibility(View.VISIBLE);
                addHouse.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_addHouse:

                saveHouse();
                break;

            case R.id.tv_houseAddress:

                showHouse();
                break;
        }

    }

    private void showHouse() {

        houseList.setVisibility(View.VISIBLE);
        databaseReference2.child("house")
                .orderByChild("ownerId")
                .equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                houseListInfo.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    House house = dataSnapshot1.getValue(House.class);
                    houseListInfo.add(house);
                }
                houseList.setAdapter(houseAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void saveHouse() {

        String floors = tot_floor.getText().toString().trim();
        String units = tot_unit.getText().toString().trim();
        String address = house_address.getText().toString().trim();

        String key = databaseReference.push().getKey();

        House house = new House(floors,units,address,uid,key);

        databaseReference.child(key).setValue(house);


        tot_floor.setVisibility(View.INVISIBLE);
        tot_unit.setVisibility(View.INVISIBLE);
        house_address.setVisibility(View.INVISIBLE);
        addHouse.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();





    }
}
