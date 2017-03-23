package com.example.ridewithme;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */



public class MainScreen extends AppCompatActivity {

    private ImageButton logoutbtn,searchBtn,addBtn,webBtn,personalZone;
    private ListView mListView;
    private CustomAdapter adapter;
    private ArrayList<_7Data> dataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mListView = (ListView)findViewById(R.id.mListView);
        searchBtn = (ImageButton) findViewById(R.id.search);
        addBtn = (ImageButton) findViewById(R.id.add2);
        webBtn = (ImageButton) findViewById(R.id.web);
        logoutbtn = (ImageButton) findViewById(R.id.logout);
        personalZone = (ImageButton) findViewById(R.id.myzone);
        adapter = new CustomAdapter(this, R.layout.listview_row, dataArrayList);
        mListView.setAdapter(adapter);
        retrieveData();

        //check if the user is signOut. if yes he will remove to the login activity
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainScreen.this, LoginActivity.class));
                }
            }
        };
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                Addtremp trempDialog = new Addtremp();
                trempDialog.show(manager, "Addtremp");
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                SearchTremp trempDialog = new SearchTremp();
                trempDialog.show(manager, "Searchtremp");
            }
        });

        webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        personalZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, PersonalZone.class);
                startActivity(intent);
            }
        });
    }

    public void retrieveData(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
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


    public void getUpdates(DataSnapshot dataSnapshot) {
        dataArrayList.clear();

        for (DataSnapshot str : dataSnapshot.getChildren()) {
            _7Data _7data = new _7Data();
            _7data.set_uid(str.getValue(_7Data.class).get_uid());
            _7data.set_name(str.getValue(_7Data.class).get_name());
            _7data.set_phone(str.getValue(_7Data.class).get_phone());
            _7data.set_from(str.getValue(_7Data.class).get_from());
            _7data.set_to(str.getValue(_7Data.class).get_to());
            _7data.set_date(str.getValue(_7Data.class).get_date());
            _7data.set_time(str.getValue(_7Data.class).get_time());
            _7data.set_extras(str.getValue(_7Data.class).get_extras());
            _7data.set_timestamp(str.getValue(_7Data.class).get_timestamp());
            dataArrayList.add(0,_7data);
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            adapter.getDeleteTremp().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = mDatabase.getKey();
                    mDatabase.child(key).removeValue();
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



}
