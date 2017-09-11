package com.example.ridewithme;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;


/**
 * Created by Mesfin & Naor on 14/12/2016.
 */


public class MainScreen extends AppCompatActivity {

    public ListView mListView;
    private mainScreenAdapter adapter;
    public ArrayList<TrempData> dataArrayList = new ArrayList<>();
    //public ArrayList<User> userArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private BottomNavigationView mBottomBar;
    private TextView tv3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        tv3 = (TextView) findViewById(R.id.textView3);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("אנא המתן. לוח הטרמפים נטען");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mListView = (ListView) findViewById(R.id.mListView);
        mBottomBar = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomBar);

        adapter = new mainScreenAdapter(this, R.layout.row_main_screen, dataArrayList);
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

                            case R.id.map:
                                Intent intent1 = new Intent(MainScreen.this, MapActivity.class);
                                startActivity(intent1);
                                break;

                            case R.id.mainscreen:
                                // if user is in mainScreen do nothing
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



        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if(mAuth.getCurrentUser().getUid().equals(user.getId())){
                        tv3.setText("שלום " + user.getName());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
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

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}