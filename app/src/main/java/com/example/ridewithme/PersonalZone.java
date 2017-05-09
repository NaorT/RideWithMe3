package com.example.ridewithme;


import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

import static android.R.attr.data;


public class PersonalZone extends AppCompatActivity {
    private ListView personalzone_lv;
    private personalZoneAdapter adapter;
    private ArrayList<TrempData> personaldataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FloatingActionButton logoutbtn, addbtn, userprofileBtn;
    private static final int GALLERY_INTENT = 2;
    private StorageReference mStorage;
    private Uri mImageUri, downloadUri;


    public PersonalZone() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_zone);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        logoutbtn = (FloatingActionButton) findViewById(R.id.logout);
        addbtn = (FloatingActionButton) findViewById(R.id.floatingAdd);
        personalzone_lv = (ListView) findViewById(R.id.myzone_listview);
        adapter = new personalZoneAdapter(this, R.layout.pzrow, personaldataArrayList);
        personalzone_lv.setAdapter(adapter);
        personalzone_lv.setLongClickable(true);
        adapter.notifyDataSetChanged();
        updateMyZone();
        personalzone_lv.setEmptyView(findViewById(R.id.emptylist));

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                Addtremp trempDialog = new Addtremp();
                trempDialog.show(manager, "Addtremp");

            }
        });
    }

    public void updateMyZone() {

        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                personaldataArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TrempData trempData2 = ds.getValue(TrempData.class);
                    if (mAuth.getCurrentUser().getUid().toString().equals(trempData2.get_uid())) {
                        personaldataArrayList.add(0, trempData2);
                        personalzone_lv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


   /* public void setprofileImage(View view) {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image*//**//*");
        startActivityForResult(galleryIntent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON).
                    setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();

                StorageReference filepath = mStorage.child("photos").child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageView iv = (ImageView) findViewById(R.id.pzmycar);
                        downloadUri = taskSnapshot.getDownloadUrl();
                        //mDatabase.child("_image").setValue(downloadUri.toString());
                        Picasso.with(PersonalZone.this).load(downloadUri).into(iv);
                        Toast.makeText(PersonalZone.this,"התמונה עלתה בהצלחה",Toast.LENGTH_SHORT).show();

                    }
                });


            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }*/



    /*@Override
    public void onBackPressed() {

        this.finish();
        overridePendingTransition  (R.anim.slide_out, R.anim.slide_out);
    }*/

}

