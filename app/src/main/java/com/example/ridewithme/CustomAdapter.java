package com.example.ridewithme;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;


/**
 * Created by Mesfin & Naor on 04/12/2016.
 */

public class CustomAdapter extends ArrayAdapter<TrempData>  {

    private TextView name  ,phone ,date_time ,msg , extra,uid ;
    private ImageButton deleteTremp , editTremp , phoneBtn ;
    private ImageView sideview;
    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;



    public CustomAdapter(Context context, int layoutResource, ArrayList<TrempData> list) {
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

        final TrempData data = getItem(position);
        data.setPos(position);
        int[] androidColors = getContext().getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];


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
            sideview = (ImageView)view.findViewById(R.id.user_image);

            if (name != null & phone != null & msg != null & date_time != null & uid != null) {
                name.setText(data.get_name());
                name.setTextColor(randomAndroidColor);

                phone.setText(data.get_phone());
                date_time.setText(data.get_timestamp());
                msg.setText(data.get_from() + "--> " + data.get_to() + ", " + data.get_date() + ", " + data.get_time());
                uid.setText(data.get_uid());

                phoneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get_phone()));
                        getContext().startActivity(i);
                    }
                });

                deleteTremp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    TrempData tremp = getItem(position);
                                    if (ds.getKey().toString().equals(tremp.get_key())) {
                                        mDatabase.child(ds.getKey().toString()).removeValue();
                                        notifyDataSetChanged();
                                        Toast.makeText(getContext(),"הטרמפ נמחק בהצלחה",Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


                if(data.get_extras().length() == 0) extra.setVisibility(View.GONE);
                extra.setText("הערות:" + " " + data.get_extras());

                if(firebaseAuth.getCurrentUser().getUid().equals(uid.getText())){
                    deleteTremp.setVisibility(View.VISIBLE);
                    editTremp.setVisibility(View.VISIBLE);
                    phoneBtn.setVisibility(View.GONE);
                    sideview.setColorFilter(Color.rgb(255,164,30));
                }
                else{
                    deleteTremp.setVisibility(View.GONE);
                    editTremp.setVisibility(View.GONE);
                    phoneBtn.setVisibility(View.VISIBLE);
                    sideview.setColorFilter(Color.rgb(176,176,176));
                }
            }
        }




        return view;
    }




}
