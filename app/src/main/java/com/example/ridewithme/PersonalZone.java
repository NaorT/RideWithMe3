package com.example.ridewithme;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.HexagonImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.data;
import static android.R.attr.id;


public class PersonalZone extends AppCompatActivity {
    private ListView personalzone_lv;
    private personalZoneAdapter adapter;
    private ArrayList<TrempData> personaldataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TrempData trempData2;
    private BottomNavigationView mBottomBar;
    private final int SELECT_PHOTO = 1;
    ImageView bg;
    ImageButton logout;
    TextView yourName;
    SharedPreferences sp;
    CircularImageView profileImg;
    Addtremp trempDialog;
    TrempData data;

    public PersonalZone() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpersonalzone);

        bg = (ImageView) findViewById(R.id.bg);
        profileImg = (CircularImageView) findViewById(R.id.profileimg);
        sp = getSharedPreferences("profilePicture", MODE_PRIVATE);

        if (!sp.getString("dp", "").equals("")) {
            byte[] decodedString = Base64.decode(sp.getString("dp", ""), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImg.setImageBitmap(decodedByte);
        }
        logout = (ImageButton) findViewById(R.id.logoutpz);
        yourName = (TextView) findViewById(R.id.pzyourname);
        mBottomBar = (BottomNavigationView) findViewById(R.id.navigationpersonalzoneee);
        personalzone_lv = (ListView) findViewById(R.id.pzzone_listview);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        BottomNavigationViewHelper.disableShiftMode(mBottomBar);
        adapter = new personalZoneAdapter(this, R.layout.pzrow, personaldataArrayList);
        personalzone_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateMyZone();
        personalzone_lv.setEmptyView(findViewById(R.id.emptylist));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        mBottomBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.add2:
                                FragmentManager manager2 = getFragmentManager();
                                Addtremp trempDialog2 = new Addtremp();
                                trempDialog2.show(manager2, "Addtremp");
                                break;

                            case R.id.search:
                                FragmentManager manager = getFragmentManager();
                                SearchTremp trempDialog = new SearchTremp();
                                trempDialog.show(manager, "Searchtremp");
                                break;

                            case R.id.myzone:
                                // if user is in personalZone do nothing
                                //overridePendingTransition  (R.anim.slide_in, R.anim.slide_in);
                                break;

                            case R.id.map:
                                Intent intent1 = new Intent(PersonalZone.this, MapActivity.class);
                                startActivity(intent1);
                                break;

                            case R.id.mainscreen:
                                Intent intent2 = new Intent(PersonalZone.this, MainScreen.class);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });

        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (mAuth.getCurrentUser().getUid().toString().equals(user.getId().toString())) {
                        yourName.setText(user.getName().toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profileImg.setImageBitmap(selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        sp.edit().putString("dp", encodedImage).commit();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public void updateMyZone() {

        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                personaldataArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    trempData2 = ds.getValue(TrempData.class);
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

    public void onClick(View v) {

        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        data = adapter.getItem(position);
        Bundle args = new Bundle();
        args.putString("name", data.get_name());
        args.putString("phone", data.get_phone());
        args.putString("from", data.get_from());
        args.putString("to", data.get_to());
        args.putString("date", data.get_date());
        args.putString("time", data.get_time());
        args.putString("extra", data.get_extras());
        FragmentManager manager = getFragmentManager();
        trempDialog = new Addtremp();
        trempDialog.setArguments(args);
        trempDialog.show(manager, "Addtremp");


    }

    /*public void sendEditedTremp(View view) {
    
        Date myDate = new Date();
        SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");
        String mdy = mdyFormat.format(myDate);
        String name = trempDialog.name.getText().toString();
        String phone = trempDialog.phone.getText().toString();
        String from = trempDialog.from.getText().toString();
        String to = trempDialog.to.getText().toString();
        String date = trempDialog.date.getText().toString();
        String time = trempDialog.time.getText().toString();
        String extra = trempDialog.extra.getText().toString();
        String timestamp = mdy;
        String uid = mAuth.getCurrentUser().getUid();
        String key = data.get_key().toString();
        TrempData trempData = new TrempData(key, uid, name, phone, from, to, date, time, extra, timestamp);
        mDatabase.child("Posts").child(data.get_key().toString()).setValue(trempData);

    }*/


}