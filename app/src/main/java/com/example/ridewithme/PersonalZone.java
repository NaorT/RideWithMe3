package com.example.ridewithme;


import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;


public class PersonalZone extends AppCompatActivity implements StartCommunication {
    private ListView personalzone_lv;
    private ImageButton profileImage;
    // private Button submitImage;
    private CustomAdapter adapter;
    private ArrayList<TrempData> personaldataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //private DatabaseReference mDatabaseUsers;
    private static final  int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    private StorageReference mStorageImage;
    private FloatingActionButton logoutbtn , addbtn;
    //private String user_id;



    public PersonalZone(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_zone);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageImage = FirebaseStorage.getInstance().getReference().child("profile_images");
        logoutbtn = (FloatingActionButton) findViewById(R.id.logout);
        addbtn = (FloatingActionButton) findViewById(R.id.floatingAdd);
        //user_id = mAuth.getCurrentUser().getUid().toString();
        profileImage = (ImageButton)findViewById(R.id.profile_image);
        personalzone_lv = (ListView)findViewById(R.id.myzone_listview);
        // submitImage = (Button)findViewById(R.id.submitImage);
        adapter = new CustomAdapter(this,R.layout.testrow,personaldataArrayList);
        personalzone_lv.setAdapter(adapter);
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


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
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
                profileImage.setImageURI(mImageUri);


                StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                       // mDatabase.child("Posts").child("image").setValue(mImageUri);

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    public void updateMyZone(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setComm(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setComm(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setComm(DataSnapshot dataSnapshot) {
        personaldataArrayList.clear();

        for (DataSnapshot str : dataSnapshot.getChildren()) {
            TrempData trempData = new TrempData();
            trempData.set_key(str.getValue(TrempData.class).get_key());
            trempData.set_uid(str.getValue(TrempData.class).get_uid());
            trempData.set_name(str.getValue(TrempData.class).get_name());
            trempData.set_phone(str.getValue(TrempData.class).get_phone());
            trempData.set_from(str.getValue(TrempData.class).get_from());
            trempData.set_to(str.getValue(TrempData.class).get_to());
            trempData.set_date(str.getValue(TrempData.class).get_date());
            trempData.set_time(str.getValue(TrempData.class).get_time());
            trempData.set_extras(str.getValue(TrempData.class).get_extras());
            trempData.set_timestamp(str.getValue(TrempData.class).get_timestamp());

            if(mAuth.getCurrentUser().getUid().toString().equals((str.getValue(TrempData.class).get_uid()))) {
                personaldataArrayList.add(0, trempData);
                personalzone_lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /*@Override
    public void onBackPressed() {

        this.finish();
        overridePendingTransition  (R.anim.slide_out, R.anim.slide_out);
    }*/




}

