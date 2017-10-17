package com.example.ridewithme;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class PersonalZone extends AppCompatActivity {
    private ListView personalzone_lv;
    private personalZoneAdapter adapter;
    private ArrayList<TrempData> personaldataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TrempData trempData2;
    private BottomNavigationView mBottomBar;
    ImageView bg;
    ImageButton logout,sidemenu;
    TextView yourName;
    CircularImageView profileImg;
    Edittremp trempDialog;
    TrempData data;
    ProgressDialog progressDialog;

    private static final int GALLERY_INTENT = 2 ;
    private StorageReference mStorage;
    private Uri mImageUri ,downloadUri;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    FirebaseUser firebaseUser ;
    AlertDialog alertDialog;

    public PersonalZone() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_zone);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("אנא המתן...");
        progressDialog.setCanceledOnTouchOutside(false);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setExpandedTitleGravity(50|3);
        collapsingToolbarLayout.setCollapsedTitleGravity(3);
        //Set a listener to know the current visible state of CollapseLayout
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(PersonalZone.this, R.color.light_blue));
                }else{
                    toolbar.setBackgroundColor(ContextCompat.getColor(PersonalZone.this, R.color.transparent));
                }
            }
        });
        setSupportActionBar(toolbar);

        sidemenu = (ImageButton) findViewById(R.id.sidemenu);

        bg = (ImageView) findViewById(R.id.bg);
        profileImg = (CircularImageView) findViewById(R.id.profileimg);
        logout = (ImageButton) findViewById(R.id.logoutpz);
        yourName = (TextView) findViewById(R.id.pzyourname);
        mBottomBar = (BottomNavigationView) findViewById(R.id.navigationpersonalzoneee);
        BottomNavigationViewHelper.disableShiftMode(mBottomBar);

        personalzone_lv = (ListView) findViewById(R.id.pzzone_listview);
        adapter = new personalZoneAdapter(this, R.layout.row_personal_zone, personaldataArrayList);
        personalzone_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateMyZone();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

            }
        });

       /* profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image*//*");
                startActivityForResult(galleryIntent, GALLERY_INTENT);
            }
        });*/

        sidemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopUpMenu(v);
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
                        String img = user.getUserImage();
                        yourName.setText(user.getName().toString());
                        collapsingToolbarLayout.setTitle(user.getName());

                        if( ! user.getUserImage().equals("default")){
                            Picasso.with(PersonalZone.this).load(img).into(bg);
                            Picasso.with(PersonalZone.this).load(img).into(profileImg);

                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Helper function to show icons in popup menu
    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showpopUpMenu(View v) {

        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.personal_zone_menu, popup.getMenu());
        setForceShowIcon(popup);

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change_picture:
                        Intent galleryIntent = new Intent();
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, GALLERY_INTENT);
                        break;

                    case R.id.change_phoneNumber:
                        showChangeNumberDialog();
                        break;

                    /*case R.id.change_password:
                        showChangePasswordDialog();
                        break;*/

                    case R.id.logout:
                        mAuth.signOut();
                        break;

                    case R.id.delete_account:
                        deleteAccount();
                        break;

                }
                return true;
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog.show();
                mImageUri = result.getUri();
                StorageReference filepath = mStorage.child("photos").child(mAuth.getCurrentUser().getUid()).child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUri = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("userImage").setValue(downloadUri.toString());
                        //Picasso.with(PersonalZone.this).load(downloadUri).into(profileImg);

                        progressDialog.dismiss();
                        Toast.makeText(PersonalZone.this, "התמונה עלתה בהצלחה", Toast.LENGTH_SHORT).show();


                    }

                });

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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

    //Edit tremp function
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
        args.putString("key", data.get_key());

        FragmentManager manager = getFragmentManager();
        trempDialog = new Edittremp();
        trempDialog.setArguments(args);
        trempDialog.show(manager, "Edittremp");


    }

    public void showChangeNumberDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_user_phone_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText new_phone = (EditText) dialogView.findViewById(R.id.change_phoneNumberET);


        dialogBuilder.setTitle("עדכון פלאפון");
        dialogBuilder.setMessage("הכנס את המספר החדש");
        dialogBuilder.setPositiveButton("שלח", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("phone").setValue(new_phone.getText().toString());
                mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            TrempData td = ds.getValue(TrempData.class);
                            if (mAuth.getCurrentUser().getUid().toString().equals(td.get_uid())) {
                                mDatabase.child("Posts").child(td.get_key().toString()).child("_phone").setValue(new_phone.getText().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        dialogBuilder.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startActivity(new Intent(PersonalZone.this , MainScreen.class));
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }



    public void deleteAccount(){
        alertDialog = new AlertDialog.Builder(PersonalZone.this).create();
        alertDialog.setTitle("מחיקת משתמש");
        alertDialog.setMessage("האם אתה בטוח שברצונך למחוק את החשבון?");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteAllUserPosts();
                mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).removeValue();

                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(PersonalZone.this,"החשבון נמחק בהצלחה",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PersonalZone.this , LoginActivity.class));
                    }
                });
            }
        });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "לא", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PersonalZone.this , MainScreen.class));

            }
        });

        alertDialog.show();
    }

    private void deleteAllUserPosts() {
        for (int i = 0; i< personaldataArrayList.size(); i++){
            TrempData data_to_delete = personaldataArrayList.get(i);
            if(mAuth.getCurrentUser().getUid().equals(data_to_delete.get_uid())){
                mDatabase.child("Posts").child(data_to_delete.get_key()).removeValue();

            }
        }
    }

}