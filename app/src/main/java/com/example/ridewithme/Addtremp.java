package com.example.ridewithme;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

public class Addtremp extends DialogFragment {
    public EditText name, phone, from, to, date, time, extra;
    public Button send_btn,update;
    private DatabaseReference mDatabase, mDatabase2;
    public String str_key, str_uid, str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp, send_time2;
    public TrempData trempData;
    private FirebaseAuth mAuth;
    private MyDatePicker myDatePicker;
    private MyTimePicker myTimePicker;
    String userUid;


    public Addtremp() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.adddialog, null);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_drawable);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle mArgs = getArguments();


        //INITIALIZE DATA_BASE AND VIEWS
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        //timeStamp
        final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        send_time2 = dateFormat1.format(new Date());
        //Initialize views
        name = (EditText) v.findViewById(R.id.name);
        phone = (EditText) v.findViewById(R.id.phone);
        from = (EditText) v.findViewById(R.id.from);
        to = (EditText) v.findViewById(R.id.to);
        date = (EditText) v.findViewById(R.id.date);
        myDatePicker = new MyDatePicker(getActivity(),date);
        time = (EditText) v.findViewById(R.id.time);
        myTimePicker = new MyTimePicker(getActivity(),time);
        extra = (EditText) v.findViewById(R.id.extra);
        send_btn = (Button) v.findViewById(R.id.add3) ;

       /* from.setText(mArgs.getString("from"));
        to.setText(mArgs.getString("to"));
        date.setText(mArgs.getString("date"));
        time.setText(mArgs.getString("time"));
        extra.setText(mArgs.getString("extra"));
        name.setText(mArgs.getString("name"));
        phone.setText(mArgs.getString("phone"));*/


        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if(mAuth.getCurrentUser().getUid().toString().equals(user.getId().toString())){
                        name.setText(user.getName().toString());
                        phone.setText(user.getPhone().toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date myDate = new Date();
                SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");
                String mdy = mdyFormat.format(myDate);
                str_name = name.getText().toString();
                str_phone = phone.getText().toString();
                str_from = from.getText().toString();
                str_to = to.getText().toString();
                str_date = date.getText().toString();
                str_time = time.getText().toString();
                str_extra = extra.getText().toString();
                str_timestamp = send_time2 + ", " + mdy;
                str_uid = mAuth.getCurrentUser().getUid();

                trempData = new TrempData(str_key, str_uid, str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp);
                if (name.length() == 0) {
                    name.requestFocus();
                    name.setHintTextColor(Color.RED);
                    name.setHint("מלא את שמך");
                    return;
                } else if (phone.length() != 10) {
                    phone.requestFocus();
                    phone.setText("");
                    phone.setHintTextColor(Color.RED);
                    phone.setTextSize(15);
                    phone.setHint("מספר פלאפון לא תקין. נסה שוב");
                    return;
                } else if (from.length() == 0) {
                    from.requestFocus();
                    from.setHintTextColor(Color.RED);
                    from.setHint("מלא מוצא");
                    return;
                } else if (to.length() == 0) {
                    to.requestFocus();
                    to.setHintTextColor(Color.RED);
                    to.setHint("מלא יעד");
                    return;
                } else if (date.length() == 0) {
                    date.requestFocus();
                    date.setHintTextColor(Color.RED);
                    date.setHint("מלא תאריך");
                    return;
                } else if (time.length() == 0) {
                    time.requestFocus();
                    time.setHintTextColor(Color.RED);
                    time.setHint("מלא שעת יציאה");
                    return;
                }

                DatabaseReference newPost = mDatabase.push();
                str_key = newPost.getKey();
                trempData = new TrempData(str_key, str_uid, str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp);
                newPost.setValue(trempData);
                Toast.makeText(getActivity(), "Tremp Added", Toast.LENGTH_SHORT).show();
                dismiss();
            }

        });
        return v;
    }
    //this method set the dialog fragment to full screen
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setLayout(attributes.MATCH_PARENT, attributes.MATCH_PARENT);


    }



}






