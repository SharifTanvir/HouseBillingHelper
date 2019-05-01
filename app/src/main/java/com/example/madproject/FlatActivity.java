package com.example.madproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

public class FlatActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView flatName;
    private Button addFlats;
    private ListView flatList;
    private List<Flat> flatListInfo;
    private  FlatAdapter flatAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private String uid;

    private String house_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);
        this.setTitle("Flats Info");


        Intent intent = getIntent();
        house_id = intent.getStringExtra("House_id");

        databaseReference = FirebaseDatabase.getInstance().getReference("flat");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        flatListInfo = new ArrayList<>();
        flatAdapter = new FlatAdapter(FlatActivity.this,flatListInfo);

        flatName = findViewById(R.id.et_flatId);
        addFlats = findViewById(R.id.btn_addFlat);
        flatList= findViewById(R.id.lv_flatList);

        addFlats.setOnClickListener(this);

        showFlatList();
    }

    private void showFlatList() {


        databaseReference2.child("flat")
                .orderByChild("houseId")
                .equalTo(house_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                flatListInfo.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Flat flat = dataSnapshot1.getValue(Flat.class);
                    flatListInfo.add(flat);
                }
                flatList.setAdapter(flatAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_addFlat:
                
                addFlat();
                
                break;

        }


    }

    private void addFlat() {


        String floorId = flatName.getText().toString().trim();

        if(floorId.isEmpty()) {
            flatName.setError("Enter name");
            flatName.requestFocus();
            return;
        }

        String key = databaseReference.push().getKey();
        Flat flat = new Flat(floorId,house_id,key);
        databaseReference.child(key).setValue(flat);
        flatName.setText("");

        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
    }
}
