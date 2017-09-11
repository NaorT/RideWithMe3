package com.example.ridewithme;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;
import static com.example.ridewithme.R.id.mListView;
//import static com.example.ridewithme.R.id.submitText;



public class personalZoneAdapter extends ArrayAdapter<TrempData>  {


    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;


    static class ViewHolderItem {
        private TextView uid ,timestamp;
        private TextView name  , from,to,date,time,extra;
        private ImageButton deleteTremp, edittremp ;
        private ImageView row;

    }


    public personalZoneAdapter(Context context, int layoutResource, ArrayList<TrempData> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolderItem viewHolder;

        if (view == null) {
            final LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);

            viewHolder = new ViewHolderItem();
            //viewHolder.name = (EditText) view.findViewById(R.id.pzmyname);
            viewHolder.timestamp = (TextView) view.findViewById(R.id.pznewtimestamp);
            viewHolder.extra = (TextView) view.findViewById(R.id.pznewextra);
            viewHolder.uid = (TextView) view.findViewById(R.id.pznewid);
            viewHolder.deleteTremp = (ImageButton)view.findViewById(R.id.pznewdelete);
            viewHolder.edittremp = (ImageButton)view.findViewById(R.id.pznewedit);
            viewHolder.row = (ImageView)view.findViewById(R.id.pzrowbck);
            viewHolder.date = (TextView) view.findViewById(R.id.pznewdate);
            viewHolder.time = (TextView) view.findViewById(R.id.pznewtime);
            viewHolder.from = (TextView) view.findViewById(R.id.pznewfrom);
            viewHolder.to = (TextView) view.findViewById(R.id.pznewto);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolderItem) convertView.getTag();

        }

        final TrempData data = getItem(position);

        //int[] androidColors = getContext().getResources().getIntArray(R.array.androidcolors);
        //int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        if (data != null) {

            // viewHolder.name.setText(data.get_name());
            viewHolder.timestamp.setText(data.get_timestamp());
            viewHolder.uid.setText(data.get_uid());
            viewHolder.date.setText(data.get_date());
            viewHolder.time.setText(data.get_time());
            viewHolder.to.setText(data.get_to());
            viewHolder.from.setText(data.get_from());
            viewHolder.extra.setText(data.get_extras());

            viewHolder.deleteTremp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Posts").child(data.get_key()).removeValue();
                    notifyDataSetChanged();
                    Toast.makeText(getContext(),"הטרמפ נמחק בהצלחה",Toast.LENGTH_SHORT).show();
                }
            });

        }

        return view;
    }

}





