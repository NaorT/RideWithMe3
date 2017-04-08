package com.example.ridewithme;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
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

    private ImageButton logoutbtn, searchBtn, addBtn, webBtn, personalZone;
    public ListView mListView;
    private CustomAdapter adapter;
    public ArrayList<TrempData> dataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("אנא המתן. לוח הטרמפים נטען");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mListView = (ListView) findViewById(R.id.mListView);
        searchBtn = (ImageButton) findViewById(R.id.search);
        addBtn = (ImageButton) findViewById(R.id.add2);
        webBtn = (ImageButton) findViewById(R.id.web);
        logoutbtn = (ImageButton) findViewById(R.id.logout);
        personalZone = (ImageButton) findViewById(R.id.myzone);
        adapter = new CustomAdapter(this, R.layout.listview_row, dataArrayList);
        mListView.setAdapter(adapter);
        retrieveData();

        //mListView.setEmptyView(findViewById(R.id.emptylist1));



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
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/114964358535991/"));
                startActivity(i);
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

    public void retrieveData() {
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
            public void onChildRemoved(final DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                for (TrempData td : dataArrayList) {
                    if (key.equals(td.get_key())) {
                        dataArrayList.remove(td);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

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
            dataArrayList.add(0, trempData);
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        progressDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }





}