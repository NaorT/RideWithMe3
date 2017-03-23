package com.example.ridewithme;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class PersonalZone extends AppCompatActivity {
    public ListView personalzone_lv;
    public CustomAdapter adapter;
    public ArrayList<_7Data> personaldataArrayList = new ArrayList<>();
    public Addtremp addtremp = new Addtremp();
    public _7Data data;

    public PersonalZone(){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_zone);
        personalzone_lv = (ListView) findViewById(R.id.myzone_listview);
        adapter = new CustomAdapter(this, R.layout.listview_row, personaldataArrayList);


    }




}
