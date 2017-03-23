package com.example.ridewithme;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Mesfin & Naor on 04/12/2016.
 */

public class CustomAdapter extends ArrayAdapter<_7Data>  {
    private TextView name,phone,date_time,msg,extra,uid;
    private ImageButton deleteTremp;
    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;


    public CustomAdapter(Context context, int layoutResource, ArrayList<_7Data> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;




        if (view == null) {
            final LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);
        }

        final _7Data data = getItem(position);

        if (data != null) {
            name = (TextView) view.findViewById(R.id.user_name);
            phone = (TextView) view.findViewById(R.id.user_phone);
            date_time = (TextView) view.findViewById(R.id.user_date_time);
            msg = (TextView) view.findViewById(R.id.user_msg);
            extra = (TextView) view.findViewById(R.id.user_extra);
            uid = (TextView) view.findViewById(R.id.user_uid);
            deleteTremp = (ImageButton)view.findViewById(R.id.remove);


            if (name != null & phone != null & msg != null & date_time != null & uid != null) {
                name.setText(data.get_name());
                phone.setText(data.get_phone());
                date_time.setText(data.get_timestamp());
                msg.setText(data.get_from() + "--> " + data.get_to() + ", " + data.get_date() + ", " + data.get_time());
                extra.setText("הערות:" + " " + data.get_extras());
                uid.setText(data.get_uid());
                if(firebaseAuth.getCurrentUser().getUid().equals(uid.getText())){
                    deleteTremp.setVisibility(View.VISIBLE);
                }
                else deleteTremp.setVisibility(View.INVISIBLE);
            }
        }

        return view;
    }


    public ImageButton getDeleteTremp() {
        return deleteTremp;
    }
}
