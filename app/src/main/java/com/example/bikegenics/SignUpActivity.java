package com.example.bikegenics;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText useremail, fullname, phonenumber, age, password;
    Button signup_btn;
    TextView login_btn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ImageView signupImage;
    Uri image;
    Uri downloadUri;
    FirebaseStorage storage;
    ProgressBar progressBar;
    Sprite doubleBounce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        login_btn = findViewById(R.id.user_login_btn);
        signup_btn = findViewById(R.id.user_signup_btn);
        fullname = findViewById(R.id.user_fullname_txt);
        age = findViewById(R.id.user_age_txt);
        useremail = findViewById(R.id.user_useremail_txt);
        password = findViewById(R.id.user_password_txt);
        phonenumber = findViewById(R.id.user_phone_txt);
        signupImage = findViewById(R.id.signup_Image);
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        doubleBounce = new Wave();
        progressBar.setVisibility(View.GONE);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        signup_btn.setOnClickListener(view -> {
            try {
                if (TextUtils.isEmpty(fullname.getText().toString())) {
                    fullname.setError("Full Name Cannot be Empty");
                    fullname.requestFocus();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(phonenumber.getText().toString())) {
                    phonenumber.setError("Phone Number Cannot be Empty");
                    phonenumber.requestFocus();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(age.getText().toString())) {
                    age.setError("Age Cannot be Empty");
                    age.requestFocus();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(useremail.getText().toString())) {
                    useremail.setError("Email Cannot be Empty");
                    useremail.requestFocus();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    password.setError("Password Cannot be Empty");
                    password.requestFocus();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                } else {
                    uploadimage();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        ActivityResultLauncher<String> mGetContent;
        {
            mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri result) {
                            if (result != null) {

                                Toast.makeText(getApplicationContext(), "Your image is selected", Toast.LENGTH_SHORT).show();
                                signupImage.setImageURI(result);
                                image = result;
                            }
                        }
                    });
        }
        signupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");

            }
        });
    }

    private void disableFields() {
        login_btn.setEnabled(false);
        fullname.setEnabled(false);
        age.setEnabled(false);
        signupImage.setEnabled(false);
        phonenumber.setEnabled(false);
        signup_btn.setEnabled(false);
        useremail.setEnabled(false);
        password.setEnabled(false);
    }

    private void enableField() {
        fullname.setEnabled(true);
        age.setEnabled(true);
        signupImage.setEnabled(true);
        phonenumber.setEnabled(true);
        login_btn.setEnabled(true);
        signup_btn.setEnabled(true);
        useremail.setEnabled(true);
        password.setEnabled(true);
    }

    private void uploadimage() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(doubleBounce);
        disableFields();

        if (image != null) {
            StorageReference reference = storage.getReference().child("image/" + useremail.getText().toString());
            UploadTask uploadTask = reference.putFile(image);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        signupUser();
                    } else {
                        Toast.makeText(getApplicationContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        enableField();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
            enableField();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void signupUser() {
        String Email = useremail.getText().toString();
        String Password = password.getText().toString();
        Map<String, Object> user = new HashMap<>();
        user.put("FullName", fullname.getText().toString());
        user.put("Age", age.getText().toString());
        user.put("PhoneNumber", phonenumber.getText().toString());
        user.put("ProfileImage", downloadUri.toString());

        try {
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        db.collection("users").document(Email).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                progressBar.setVisibility(View.GONE);
                                enableField();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "User Data Not Added", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                enableField();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        enableField();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}