package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

    private SensorManager sm;
    private float aclVal;
    private  float aclLast;
    private  float shake;

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

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        aclLast = SensorManager.GRAVITY_EARTH;
        aclVal = SensorManager.GRAVITY_EARTH;
        shake = 0.0f;


    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            aclLast = aclVal;
            aclVal = (float) Math.sqrt((double) x*x + y*y + z*z);
            float delta = aclVal - aclLast;
            shake = shake * 0.9f + delta;

            if (shake > 12){

                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

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


        if(floors.isEmpty()) {
            tot_floor.setError("Enter number of floor");
            tot_floor.requestFocus();
            return;
        }
        if(units.isEmpty()) {
            tot_unit.setError("Enter number of units");
            tot_unit.requestFocus();
            return;
        }
        if(address.isEmpty()) {
            house_address.setError("Enter  address");
            house_address.requestFocus();
            return;
        }

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
