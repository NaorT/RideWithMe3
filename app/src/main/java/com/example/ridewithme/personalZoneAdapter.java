package com.example.ridewithme;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;
import static com.example.ridewithme.R.id.mListView;
import static com.example.ridewithme.R.id.submitText;
import static com.example.ridewithme.R.id.switchImage_btn;


public class personalZoneAdapter extends ArrayAdapter<TrempData>  {

    private StorageReference mStorage;
    private Uri mImageUri ,downloadUri ;
    private static final int GALLERY_INTENT = 2 ;
    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;



    static class ViewHolderItem{
        private TextView uid ,timestamp;
        private EditText name  , from,to,date,time,extra;
        private ImageButton deleteTremp , editTremp , phoneBtn , submitText ;
        private ImageView car,timeline;
        private Button switvhImage_btn;
    }


    public personalZoneAdapter(Context context, int layoutResource, ArrayList<TrempData> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolderItem viewHolder;

        if (view == null) {
            final LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);

            viewHolder = new ViewHolderItem();
            viewHolder.name = (EditText) view.findViewById(R.id.pzmyname);
            viewHolder.timestamp = (TextView) view.findViewById(R.id.pzmytimestamp);
            viewHolder.extra = (EditText) view.findViewById(R.id.pzmyextra);
            viewHolder.uid = (TextView) view.findViewById(R.id.pzmyuid);
            viewHolder.deleteTremp = (ImageButton)view.findViewById(R.id.pzremove);
            viewHolder.editTremp = (ImageButton)view.findViewById(R.id.pzedit);
            viewHolder.phoneBtn = (ImageButton)view.findViewById(R.id.pzmyphone);
            viewHolder.submitText = (ImageButton)view.findViewById(R.id.pzsubmit_edit);
            viewHolder.switvhImage_btn = (Button) view.findViewById(R.id.switchImage_btn);
            viewHolder.car = (ImageView)view.findViewById(R.id.pzmycar);
            viewHolder.timeline = (ImageView)view.findViewById(R.id.pztimeline);
            viewHolder.date = (EditText) view.findViewById(R.id.pzmydate);
            viewHolder.time = (EditText) view.findViewById(R.id.pzmytime);
            viewHolder.from = (EditText) view.findViewById(R.id.pzmyfrom);
            viewHolder.to = (EditText) view.findViewById(R.id.pzmyto);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        final TrempData data = getItem(position);


        //int[] androidColors = getContext().getResources().getIntArray(R.array.androidcolors);
        //int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        if (data != null) {

            viewHolder.name.setText(data.get_name());
            viewHolder.timestamp.setText(data.get_timestamp());
            viewHolder.uid.setText(data.get_uid());
            viewHolder.date.setText(data.get_date());
            viewHolder.time.setText(data.get_time());
            viewHolder.to.setText(data.get_to());
            viewHolder.from.setText(data.get_from());
            viewHolder.extra.setText(data.get_extras());

            viewHolder.editTremp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.submitText.setVisibility(View.VISIBLE);
                    setEditable(viewHolder.from);

                }
            });

            viewHolder.submitText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String new_from = viewHolder.from.getText().toString();
                    data.set_from(new_from);
                    cancelEditable(viewHolder.from);
                    viewHolder.submitText.setVisibility(View.GONE);
                    mDatabase.child(getItem(position).get_key().toString()).child("_from").setValue(new_from);

                }
            });

            viewHolder.phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get_phone()));
                    getContext().startActivity(i);
                }
            });

            viewHolder.deleteTremp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Posts").child(data.get_key()).removeValue();
                    notifyDataSetChanged();
                    Toast.makeText(getContext(),"Tremp deleted!",Toast.LENGTH_SHORT).show();
                }
            });

            if(firebaseAuth.getCurrentUser().getUid().equals(viewHolder.uid.getText())){

                viewHolder.car.setColorFilter(Color.rgb(255,164,30));
                //viewHolder.car.setImageURI(data.get_image());
            }
            else{

                viewHolder.car.setColorFilter(Color.rgb(176,176,176));
            }

        }

        return view;
    }

    public void setEditable(EditText v){

        v.setBackground(new ColorDrawable(GRAY));
        v.setEnabled(true);
        v.setCursorVisible(true);
        v.setFocusableInTouchMode(true);
        v.setInputType(InputType.TYPE_CLASS_TEXT);
        v.requestFocus();
    }

    public void cancelEditable(EditText v){
        v.setEnabled(false);
        v.setBackground(new ColorDrawable(TRANSPARENT));
        v.setCursorVisible(false);
        v.setFocusableInTouchMode(false);
    }


}





