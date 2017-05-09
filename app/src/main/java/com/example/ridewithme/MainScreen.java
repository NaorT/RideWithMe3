package com.example.ridewithme;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Mesfin & Naor on 14/12/2016.
 */


public class MainScreen extends AppCompatActivity {

    public ListView mListView;
    private mainScreenAdapter adapter;
    public ArrayList<TrempData> dataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private BottomNavigationView mBottomBar;


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
        mBottomBar = (BottomNavigationView) findViewById(R.id.navigation);
        adapter = new mainScreenAdapter(this, R.layout.row, dataArrayList);
        mListView.setAdapter(adapter);
        updateMainScreen();


        mBottomBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.add2:
                                FragmentManager manager2 = getFragmentManager();
                                Addtremp trempDialog2 = new Addtremp();
                                trempDialog2.show(manager2, "Addtremp");
                                break;

                            case R.id.search:
                                FragmentManager manager = getFragmentManager();
                                SearchTremp trempDialog = new SearchTremp();
                                trempDialog.show(manager, "Searchtremp");
                                break;

                            case R.id.myzone:
                                Intent intent = new Intent(MainScreen.this, PersonalZone.class);
                                startActivity(intent);
                                //overridePendingTransition  (R.anim.slide_in, R.anim.slide_in);
                                break;
                        }
                        return true;
                    }
                });

        //check if the user is signOut. if yes he will remove to the login activity
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainScreen.this, LoginActivity.class));
                }
            }
        };
    }

    public void updateMainScreen() {
        progressDialog.dismiss();
        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TrempData trempData2 = ds.getValue(TrempData.class);
                    dataArrayList.add(0, trempData2);
                }
                mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}