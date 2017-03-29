package com.example.ridewithme;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

public class SearchTremp extends DialogFragment {
    public EditText from,to,date,time;
    public Button search_send_btn;
    public String str_from,str_to,str_date,str_time;
    private DatabaseReference mDatabase;
    private MyDatePicker myDatePicker;
    private MyTimePicker myTimePicker;
    private ProgressDialog progressDialog;

    public SearchTremp(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.searchdialog, null);

        //INITIALIZE DATA_BASE AND VIEWS
        progressDialog = new ProgressDialog(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        from = (EditText) v.findViewById(R.id.from);
        to = (EditText) v.findViewById(R.id.to);
        date = (EditText) v.findViewById(R.id.date);
        myDatePicker = new MyDatePicker(getActivity(),date);
        time = (EditText) v.findViewById(R.id.time);
        myTimePicker = new MyTimePicker(getActivity(),time);
        search_send_btn = (Button) v.findViewById(R.id.search_send);
        search_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_from = from.getText().toString();
                str_to = to.getText().toString();
                str_date = date.getText().toString();
                str_time = time.getText().toString();

                Toast.makeText(getActivity(), "מחפש, אנא המתן...", Toast.LENGTH_SHORT).show();
                searchfunc3();
                dismiss();
            }
        });
        return v;
    }

    public void searchInDB(DataSnapshot dataSnapshot) {

        for (DataSnapshot str : dataSnapshot.getChildren()) {
            String s1 = str.getValue(TrempData.class).get_from();
            String s2 = str.getValue(TrempData.class).get_to();
            String s3 = str.getValue(TrempData.class).get_date();
            String s4 = str.getValue(TrempData.class).get_time();
            String s5 = str.getValue(TrempData.class).get_name();
            String s6 = str.getValue(TrempData.class).get_phone();
            if(s1.equals(str_from) & s2.equals(str_to)  & s3.equals(str_date)  & s4.equals(str_time)){
                Toast.makeText(getActivity(), "יש טרמפ!" + " " + s5 + ", טלפון: " + s6, Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void searchfunc3(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                searchInDB(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                searchInDB(dataSnapshot);
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

}
