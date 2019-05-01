package com.example.madproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tagTenant;
    private EditText tenantName;
    private EditText tenantPhone;
    private EditText tenantNid;

    private Button tenantAdd;
    private Button tenantUpdate;
    private Button tenantDelete;
    private Button tenantCall;

    private Button createBill;
    private ListView billList;
    private List<Bill> biiListInfo;
    private  BillAdapter billAdapter;



    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private String uid;


    private String flat_id;
    private String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        this.setTitle("Rent Info");

        Intent intent = getIntent();
        flat_id = intent.getStringExtra("flat_id");


        databaseReference = FirebaseDatabase.getInstance().getReference("tenant");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        databaseReference2 = FirebaseDatabase.getInstance().getReference();


        tagTenant = findViewById(R.id.tv_tagTenant);
        tenantName = findViewById(R.id.et_tenantName);
        tenantPhone = findViewById(R.id.et_tenantPhone);
        tenantNid = findViewById(R.id.et_tenantNID);

        tenantAdd = findViewById(R.id.btn_tenantAdd);
        tenantUpdate= findViewById(R.id.btn_tenantUpdate);
        tenantDelete = findViewById(R.id.btn_tenantDelete);
        tenantCall = findViewById(R.id.btn_tenantCall);

        createBill = findViewById(R.id.btn_addBill);
        billList = findViewById(R.id.lv_billList);
        biiListInfo = new ArrayList<>();
        billAdapter = new BillAdapter(RentActivity.this,biiListInfo);



        tenantAdd.setOnClickListener(this);
        tenantUpdate.setOnClickListener(this);
        tenantDelete.setOnClickListener(this);
        tenantCall.setOnClickListener(this);

        createBill.setOnClickListener(this);

       showCurrentTenant();
       ShowBillList();



    }

    private void ShowBillList() {

        databaseReference2.child("bill")
                .orderByChild("flatId")
                .equalTo(flat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                biiListInfo.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Bill bill = dataSnapshot1.getValue(Bill.class);
                    biiListInfo.add(bill);
                }
                billList.setAdapter(billAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_tenantAdd:

                addTenant();
                break;
            case R.id.btn_tenantUpdate:

                updateTenant();
                break;
            case R.id.btn_tenantDelete:

                deleteTenant();
                break;
            case R.id.btn_addBill:

                Intent intent = new Intent(getApplicationContext(), BillActivity.class);
                intent.putExtra("flat_id", flat_id);
                intent.putExtra("flag", "hide");
                startActivity(intent);
                break;


            case R.id.btn_tenantCall:

                String phn = tenantPhone.getText().toString().trim();

                Intent intent2 = new Intent(Intent.ACTION_CALL);
                intent2.setData(Uri.parse("tel:"+phn));
                startActivity(intent2);
                break;
        }

    }


    private void addTenant() {

        String name = tenantName.getText().toString().trim();
        String phone = tenantPhone.getText().toString().trim();
        String nid = tenantNid.getText().toString().trim();
        String status = "livingNow";

        if(name.isEmpty()) {
            tenantName.setError("Enter name");
            tenantName.requestFocus();
            return;
        }
        if(phone.isEmpty()) {
            tenantPhone.setError("Enter phone number");
            tenantPhone.requestFocus();
            return;
        }
        if(nid.isEmpty()) {
            tenantNid.setError("Enter nid");
            tenantNid.requestFocus();
            return;
        }

        if (!check.equals("active")){
            String key = databaseReference.push().getKey();
            Tenant tenant = new Tenant(key,flat_id,name,phone,nid,status);
            databaseReference.child(key).setValue(tenant);

            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();

        }



    }

    private void verify(){
        check = "active";

    }

    private void deleteTenant() {

        databaseReference2.child("tenant")
                .orderByChild("flatId")
                .equalTo(flat_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            Tenant tenant = dataSnapshot1.getValue(Tenant.class);
                            tenant.setStatus("left");
                            databaseReference.child(tenant.getTenantKey()).setValue(tenant);
                            Toast.makeText(getApplicationContext(), "Item deleted...!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        tenantName.setText("");
        tenantPhone.setText("");
        tenantNid.setText("");
    }

    private void updateTenant() {

        databaseReference2.child("tenant")
                .orderByChild("flatId")
                .equalTo(flat_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            Tenant tenant = dataSnapshot1.getValue(Tenant.class);
                            tenant.setPhone(tenantPhone.getText().toString().trim());
                            tenant.setNid(tenantNid.getText().toString().trim());
                            tenant.setName(tenantName.getText().toString().trim());
                            databaseReference.child(tenant.getTenantKey()).setValue(tenant);
                            Toast.makeText(getApplicationContext(), "Information updated successfully...!", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void showCurrentTenant() {

        databaseReference2.child("tenant")
                .orderByChild("flatId")
                .equalTo(flat_id)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Tenant tenant = dataSnapshot1.getValue(Tenant.class);
                    if(tenant.getStatus().equals("livingNow")){
                        tenantName.setText(tenant.getName());
                        tenantPhone.setText(tenant.getPhone());
                        tenantNid.setText(tenant.getNid());
                        verify();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
