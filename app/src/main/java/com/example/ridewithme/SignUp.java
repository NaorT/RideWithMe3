package com.example.ridewithme;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Naor on 28/01/2017.
 */

public class SignUp extends DialogFragment {

    private TextView textView;
    private EditText et_email, et_password , et_name , et_phone;
    private Button signIn;
    private ProgressDialog progressDialog;
    private TextView comment;
    private DatabaseReference mDatabase;

    //LoginActivity loginActivity;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignUp(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup, null);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        //INITIALIZE DATA_BASE AND VIEWS
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        et_email = (EditText) v.findViewById(R.id.signin_email);
        et_password = (EditText) v.findViewById(R.id.signin_password);
        et_name = (EditText) v.findViewById(R.id.signin_name);
        et_phone = (EditText) v.findViewById(R.id.signin_phone);
        textView = (TextView) v.findViewById(R.id.tv) ;
        comment = (TextView)v.findViewById(R.id.tv7) ;
        signIn = (Button) v.findViewById(R.id.signin_btn);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("אנא המתן...");
        progressDialog.setCanceledOnTouchOutside(false);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        return v;
    }


    public void createUser(){

        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();
        final String name = et_name.getText().toString();
        final String phone = et_phone.getText().toString();
        final String id = "default";
        final String img = "default";
        final double lat = 0;
        final double lng = 0;
        final User user = new User(name,phone,email,password,id,lat,lng,img);


        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){

            //comment.setText("אחד מהשדות ריק. אנא מלא הכל ונסה שנית. הסיסמה צריכה להכיל לפחות 6 תווים");
            comment.setVisibility(View.VISIBLE);
        }

        else{
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {

                        if(password.length() < 6){
                            comment.setText("על הסיסמא להכיל 6 תווים לפחות");
                            comment.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    }

                    else{
                        DatabaseReference db = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
                        user.setId(mAuth.getCurrentUser().getUid().toString());
                        db.setValue(user);
                        startActivity(new Intent(getActivity() , MainScreen.class));
                    }

                }
            });
        }
    }


}
