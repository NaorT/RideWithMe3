package com.example.ridewithme;


import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;
import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by Mesfin & Naor on 04/12/2016.
 */

public class mainScreenAdapter extends ArrayAdapter<TrempData>  {

    private static class ViewHolder {
        private TextView  name ,timestamp ,uid, from,to,date,time,myextra ;
        private ImageButton phoneBtn, msgBtn;
        private ImageView  profileImage, bck;
    }

    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    public ArrayList<User> userArrayList = new ArrayList<>();
    public ArrayList<TrempData> datalist = new ArrayList<>();

    public mainScreenAdapter(Context context, int layoutResource, ArrayList<TrempData> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        setUserList();
        setDataList();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            final LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.newname);
            viewHolder.phoneBtn = (ImageButton) view.findViewById(R.id.mynewphone);
            viewHolder.msgBtn = (ImageButton) view.findViewById(R.id.newmsg);
            viewHolder.timestamp = (TextView) view.findViewById(R.id.newtimestamp);
            viewHolder.uid = (TextView) view.findViewById(R.id.newid);
            viewHolder.bck = (ImageView) view.findViewById(R.id.rowbck);
            viewHolder.profileImage = (ImageView)view.findViewById(R.id.newprofileimg);
            viewHolder.date = (TextView) view.findViewById(R.id.newdate);
            viewHolder.time = (TextView) view.findViewById(R.id.newtime);
            viewHolder.from = (TextView) view.findViewById(R.id.newfrom);
            viewHolder.to = (TextView) view.findViewById(R.id.newto);
            viewHolder.myextra = (TextView) view.findViewById(R.id.newextra);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final TrempData data = getItem(position);

        if (data != null) {

            for (int i = 0; i < userArrayList.size() ; i++) {
                for (int j = 0; j < datalist.size() ; j++) {
                    if(userArrayList.get(i).getId().equals(datalist.get(j).get_uid())){
                        mDatabase.child("Posts").child(datalist.get(j).get_key()).child("_image").setValue(userArrayList.get(i).getUserImage());
                    }
                }
            }
            if(!data.get_image().equals("default")) {
                Picasso.with(getContext()).load(data.get_image()).into(viewHolder.profileImage);
            }

            viewHolder.name.setText(data.get_name());
            viewHolder.timestamp.setText(data.get_timestamp());
            viewHolder.uid.setText(data.get_uid());
            viewHolder.date.setText(data.get_date());
            viewHolder.time.setText(data.get_time());
            viewHolder.to.setText(data.get_to());
            viewHolder.from.setText(data.get_from());
            viewHolder.uid.setText(data.get_uid());
            viewHolder.myextra.setText(data.get_extras());

            viewHolder.phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get_phone()));
                    getContext().startActivity(i);
                }
            });

            viewHolder.msgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                    sendIntent.putExtra("address",data.get_phone());
                    sendIntent.setType("text/plain");
                    startActivity(getContext(),sendIntent,null);

                }
            });

            if(firebaseAuth.getCurrentUser().getUid().equals(viewHolder.uid.getText())){

                viewHolder.phoneBtn.setClickable(false);
                viewHolder.phoneBtn.getBackground().setAlpha(50);
                viewHolder.msgBtn.setClickable(false);
                viewHolder.msgBtn.getBackground().setAlpha(50);
            }

            else{
                viewHolder.phoneBtn.setClickable(true);
                viewHolder.phoneBtn.getBackground().setAlpha(255);
                viewHolder.msgBtn.setClickable(true);
                viewHolder.msgBtn.getBackground().setAlpha(255);

            }
        }

        return view;

    }


    public void setUserList(){
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    userArrayList.add(0,user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setDataList(){
        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    TrempData td = ds.getValue(TrempData.class);
                    datalist.add(0,td);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}





