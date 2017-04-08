package com.example.ridewithme;


import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class PersonalZone extends AppCompatActivity implements StartCommunication {
    private ListView personalzone_lv;
    private CustomAdapter adapter;
    private ArrayList<TrempData> personaldataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public PersonalZone(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_zone);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        personalzone_lv = (ListView)findViewById(R.id.myzone_listview);
        adapter = new CustomAdapter(this,R.layout.listview_row,personaldataArrayList);
        personalzone_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateMyZone();
        personalzone_lv.setEmptyView(findViewById(R.id.emptylist));

    }

    public void updateMyZone(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setComm(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setComm(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setComm(DataSnapshot dataSnapshot) {
        personaldataArrayList.clear();

        for (DataSnapshot str : dataSnapshot.getChildren()) {
            TrempData trempData = new TrempData();
            trempData.set_key(str.getValue(TrempData.class).get_key());
            trempData.set_uid(str.getValue(TrempData.class).get_uid());
            trempData.set_name(str.getValue(TrempData.class).get_name());
            trempData.set_phone(str.getValue(TrempData.class).get_phone());
            trempData.set_from(str.getValue(TrempData.class).get_from());
            trempData.set_to(str.getValue(TrempData.class).get_to());
            trempData.set_date(str.getValue(TrempData.class).get_date());
            trempData.set_time(str.getValue(TrempData.class).get_time());
            trempData.set_extras(str.getValue(TrempData.class).get_extras());
            trempData.set_timestamp(str.getValue(TrempData.class).get_timestamp());
            if(mAuth.getCurrentUser().getUid().toString().equals((str.getValue(TrempData.class).get_uid()))) {
                personaldataArrayList.add(0, trempData);
                personalzone_lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }
    }




}

