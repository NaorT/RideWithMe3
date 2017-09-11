package com.example.ridewithme;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class Edittremp extends DialogFragment {

    public EditText from, to, date, time, extra;
    public Button edit_send_btn;
    public String str_from, str_to, str_date, str_time, str_extra, send_time2;
    private DatabaseReference mDatabase;
    private MyDatePicker myDatePicker;
    private MyTimePicker myTimePicker;


    public Edittremp() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_tremp_dialog, null);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_drawable);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final Bundle mArgs = getArguments();
       /* progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("אנא המתן...");*/
        //INITIALIZE DATA_BASE AND VIEWS
        //progressDialog = new ProgressDialog(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        from = (EditText) v.findViewById(R.id.edit_from);
        to = (EditText) v.findViewById(R.id.edit_to);
        date = (EditText) v.findViewById(R.id.edit_date);
        myDatePicker = new MyDatePicker(getActivity(), date);
        time = (EditText) v.findViewById(R.id.edit_time);
        myTimePicker = new MyTimePicker(getActivity(), time);
        extra = (EditText) v.findViewById(R.id.edit_extra);
        edit_send_btn = (Button) v.findViewById(R.id.edit_send);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        send_time2 = dateFormat.format(new Date());

        from.setText(mArgs.getString("from"));
        to.setText(mArgs.getString("to"));
        date.setText(mArgs.getString("date"));
        time.setText(mArgs.getString("time"));
        extra.setText(mArgs.getString("extra"));

        edit_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date myDate = new Date();
                SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");
                String str_mdy = mdyFormat.format(myDate);
                String str_from = from.getText().toString();
                String str_to = to.getText().toString();
                String str_date = date.getText().toString();
                String str_time = time.getText().toString();
                String str_extra = extra.getText().toString();
                String str_timestamp = send_time2 + ", " + str_mdy;
                String key = mArgs.getString("key");

                mDatabase.child("Posts").child(key).child("_from").setValue(str_from);
                mDatabase.child("Posts").child(key).child("_to").setValue(str_to);
                mDatabase.child("Posts").child(key).child("_date").setValue(str_date);
                mDatabase.child("Posts").child(key).child("_time").setValue(str_time);
                mDatabase.child("Posts").child(key).child("_timestamp").setValue(str_timestamp);
                mDatabase.child("Posts").child(key).child("_extras").setValue(str_extra);

                Toast.makeText(getActivity(), "הפרטים עודכנו בהצלחה", Toast.LENGTH_LONG).show();

                dismiss();
            }

        });

        return v;
    }


}
