package com.example.ridewithme;

import android.app.DialogFragment;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

/**
 * Created by Naor on 28/01/2017.
 */

public class SignIn extends DialogFragment {

    private TextView textView;
    private EditText email,password;
    private Button signIn;
    //LoginActivity loginActivity;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignIn(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_signin, null);

        //INITIALIZE DATA_BASE AND VIEWS
        mAuth = FirebaseAuth.getInstance();
        //loginActivity = new LoginActivity();
        email = (EditText) v.findViewById(R.id.signin_email);
        password = (EditText) v.findViewById(R.id.signin_password);
        textView = (TextView) v.findViewById(R.id.tv) ;
        signIn = (Button) v.findViewById(R.id.signin_btn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        return v;
    }


    public void createUser(){
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();

        if(TextUtils.isEmpty(email1) || TextUtils.isEmpty(password1) ){
            Toast.makeText(getActivity(),"אנא בדוק שמילאת הכל",Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "ישנה בעיה",Toast.LENGTH_SHORT).show();
                            }
                            else startActivity(new Intent(getActivity() , MainScreen.class));
                        }
                    });
        }
    }

}
