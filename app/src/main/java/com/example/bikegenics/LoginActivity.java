package com.example.bikegenics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Admin.Activities.Admin_Home;
import User.Activities.User_Home;

public class LoginActivity extends AppCompatActivity {
    Button login_btn;
    EditText useremail, password;
    TextView signup_btn;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    Sprite doubleBounce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup_btn = (TextView) findViewById(R.id.login_txt_signup);
        login_btn = (Button) findViewById(R.id.login_login_btn);
        useremail = (EditText) findViewById(R.id.login_username_txt);
        password = (EditText) findViewById(R.id.login_password_txt);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        doubleBounce = new Wave();

        progressBar.setVisibility(View.GONE);

        if (firebaseUser != null) {
            if (firebaseUser.getEmail().equals("admin@bg.com")) {
                startActivity(new Intent(getApplicationContext(), Admin_Home.class));
            } else {
                startActivity(new Intent(getApplicationContext(), User_Home.class));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Sign in Again", Toast.LENGTH_SHORT).show();
        }

        login_btn.setOnClickListener(view -> {
            try {
                loginUser();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        signup_btn.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(in);
        });

    }

    private void disableFields(){
        login_btn.setEnabled(false);
        signup_btn.setEnabled(false);
        useremail.setEnabled(false);
        password.setEnabled(false);
    }

    private void enableField(){
        login_btn.setEnabled(true);
        signup_btn.setEnabled(true);
        useremail.setEnabled(true);
        password.setEnabled(true);
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(doubleBounce);
        disableFields();

        String Email = useremail.getText().toString();
        String Password = password.getText().toString();

        if (TextUtils.isEmpty(Email)) {
            useremail.setError("Email Cannot be Empty");
            useremail.requestFocus();
            enableField();
            progressBar.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(Password)) {
            password.setError("Password Cannot be Empty");
            password.requestFocus();
            enableField();
            progressBar.setVisibility(View.GONE);
        } else {
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (Email.equals("admin@bg.com")) {
                            Toast.makeText(getApplicationContext(), "User Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Admin_Home.class));
                            progressBar.setVisibility(View.GONE);
                            enableField();
                        } else {
                            Toast.makeText(getApplicationContext(), "User Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), User_Home.class));
                            progressBar.setVisibility(View.GONE);
                            enableField();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        enableField();
                    }
                }
            });
        }

    }
}