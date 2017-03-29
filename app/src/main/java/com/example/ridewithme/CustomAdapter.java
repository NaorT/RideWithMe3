package com.example.ridewithme;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Mesfin & Naor on 04/12/2016.
 */

public class CustomAdapter extends ArrayAdapter<TrempData> {
    private TextView name;
    private TextView phone;
    private TextView date_time;
    private TextView msg;
    private TextView extra;
    private TextView uid;
    private ImageButton deleteTremp , editTremp , phoneBtn;
    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;



    public CustomAdapter(Context context, int layoutResource, ArrayList<TrempData> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            final LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);
        }

        final TrempData data = getItem(position);
        data.setPos(position);

        if (data != null) {
            name = (TextView) view.findViewById(R.id.user_name);
            phone = (TextView) view.findViewById(R.id.user_phone);
            date_time = (TextView) view.findViewById(R.id.user_date_time);
            msg = (TextView) view.findViewById(R.id.user_msg);
            extra = (TextView) view.findViewById(R.id.user_extra);
            uid = (TextView) view.findViewById(R.id.user_uid);
            deleteTremp = (ImageButton)view.findViewById(R.id.remove);
            editTremp = (ImageButton)view.findViewById(R.id.edit);
            phoneBtn = (ImageButton)view.findViewById(R.id.phone_btn);

            if (name != null & phone != null & msg != null & date_time != null & uid != null) {
                name.setText(data.get_name());
                phone.setText(data.get_phone());
                date_time.setText(data.get_timestamp());
                msg.setText(data.get_from() + "--> " + data.get_to() + ", " + data.get_date() + ", " + data.get_time());
                uid.setText(data.get_uid());

                if(data.get_extras().length() == 0) extra.setVisibility(View.GONE);
                extra.setText("הערות:" + " " + data.get_extras());

                /*deleteTremp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(),data._key,Toast.LENGTH_LONG).show();
                        mDatabase.orderByChild("_key").equalTo(getItemId(position)).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().setValue(null);
                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                                    }
                                });
                    }
                });*/

                phoneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get_phone()));
                        getContext().startActivity(i);
                    }
                });

                if(firebaseAuth.getCurrentUser().getUid().equals(uid.getText())){
                    deleteTremp.setVisibility(View.VISIBLE);
                    editTremp.setVisibility(View.VISIBLE);
                    phoneBtn.setVisibility(View.GONE);
                }
                else{
                    deleteTremp.setVisibility(View.GONE);
                    editTremp.setVisibility(View.GONE);
                    phoneBtn.setVisibility(View.VISIBLE);
                }
            }
        }

        return view;
    }

}
