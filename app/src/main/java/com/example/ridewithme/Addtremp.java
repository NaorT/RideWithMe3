package com.example.ridewithme;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

public class Addtremp extends DialogFragment {
    public EditText name,phone,from,to,date,time,extra;
    public Button send_btn;
    private DatabaseReference mDatabase;
    public String str_uid,str_name,str_phone,str_from,str_to,str_date,str_time,str_extra,str_timestamp,send_time,send_time2;
    public _7Data sevenString;
    private FirebaseAuth mAuth;



    public Addtremp(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.adddialog, null);
        //INITIALIZE DATA_BASE AND VIEWS
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();
        //child name will be like dateFormat "HH:mm:ss dd-MM-yyyy"
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        send_time = dateFormat.format(new Date());
        //timeStamp
        final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        send_time2 = dateFormat1.format(new Date());
        //Initialize views
        name = (EditText) v.findViewById(R.id.name);
        phone = (EditText) v.findViewById(R.id.phone);
        from = (EditText) v.findViewById(R.id.from);
        to = (EditText) v.findViewById(R.id.to);
        date = (EditText) v.findViewById(R.id.date);
        time = (EditText) v.findViewById(R.id.time);
        extra = (EditText) v.findViewById(R.id.extra);
        send_btn = (Button) v.findViewById(R.id.add2);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name = name.getText().toString();
                str_phone = phone.getText().toString();
                str_from = from.getText().toString();
                str_to = to.getText().toString();
                str_date = date.getText().toString();
                str_time = time.getText().toString();
                str_extra = extra.getText().toString();
                str_timestamp = send_time2;
                str_uid = mAuth.getCurrentUser().getUid();
                //sevenString = new _7Data(null,str_uid,str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp);
                if (name.length() == 0) {
                    name.requestFocus();
                    name.setHintTextColor(Color.RED);
                    name.setHint("מלא את שמך");
                    return;
                } else if (phone.length() != 10 ) {
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
                sevenString = new _7Data(str_uid,str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp);
                newPost.setValue(sevenString);
                //mDatabase.push().setValue(sevenString);
                /*child(send_time + " " + str_name ).*/
                Toast.makeText(getActivity(),"הטרמפ נוסף בהצלחה", Toast.LENGTH_SHORT).show();
                dismiss();
            }


        });
        return v;
    }


}
