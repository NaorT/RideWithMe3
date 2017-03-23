package com.example.ridewithme;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

public class LoginActivity extends AppCompatActivity {
    //Views
    private EditText et_email,et_password;
    private Button loginBtn,enterBtn;
    private ProgressDialog progressDialog;
    //FireBase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        //initialize views
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        enterBtn = (Button) findViewById(R.id.enter);
        progressDialog = new ProgressDialog(this);
        //when user start the app, this method check if he already sign in.
        //if yes, he will move to main screen of the app.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LoginActivity.this , MainScreen.class));
                }
            }
        };

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("אנא המתן...");
                progressDialog.show();
                startSignIn();
            }

        });

        //when loginBtn pushed, startSignIn() method start
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                SignIn signIn = new SignIn();
                signIn.show(manager, "signIn");

            }
        });
            }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    //this method start the sighIn proccess after the email and password insert correctly
    private void startSignIn(){
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){
            Toast.makeText(LoginActivity.this,"אנא בדוק שמילאת הכל",Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"אימייל או סיסמה שגויים",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
}

