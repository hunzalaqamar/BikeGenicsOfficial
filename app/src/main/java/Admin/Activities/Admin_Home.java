package Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bikegenics.LoginActivity;
import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Admin_Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    Sprite doubleBounce;
    EditText categoryName;
    Button btn_ok, btn_cancel, btn_addPost, btn_addCategory, btn_viewPost, logout, btn_viewCat, btn_pf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btn_addPost = findViewById(R.id.btn_addPost);
        btn_addCategory = findViewById(R.id.btn_addCat);
        btn_viewPost = findViewById(R.id.btn_viewPost);
        logout = findViewById(R.id.btn_logout);
        btn_viewCat = findViewById(R.id.btn_viewCat);
        btn_pf = findViewById(R.id.btn_postfeed);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        if (firebaseUser != null) {
            try {
                btn_addCategory.setOnClickListener(view -> {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Home.this, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_custom, viewGroup, false);
                    btn_ok = dialogView.findViewById(R.id.alertbuttonOk);
                    btn_cancel = dialogView.findViewById(R.id.alertbuttonCancel);
                    categoryName = (EditText) dialogView.findViewById(R.id.alertcategoryName);
                    progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
                    doubleBounce = new Wave();
                    progressBar.setIndeterminateDrawable(doubleBounce);
                    progressBar.setVisibility(View.GONE);


                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (categoryName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Enter Required Information", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                btn_ok.setEnabled(false);
                                btn_cancel.setEnabled(false);
                                categoryName.setEnabled(false);
                                String catKey = categoryName.getText().toString().toLowerCase(Locale.ROOT);
                                catKey = catKey.replaceAll("\\s+","");
                                Map<String, Object> category = new HashMap<>();
                                category.put(catKey, categoryName.getText().toString());

                                db.collection("admin").document("category")
                                        .set(category, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Category Added Successfully", Toast.LENGTH_SHORT).show();
                                                btn_ok.setEnabled(true);
                                                btn_cancel.setEnabled(true);
                                                categoryName.setEnabled(true);
                                                alertDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Category Not Added" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                btn_ok.setEnabled(true);
                                                btn_cancel.setEnabled(true);
                                                categoryName.setEnabled(true);
                                                alertDialog.dismiss();
                                            }
                                        });
                            }
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.GONE);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                });

                btn_pf.setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), Admin_PostFeed.class));
                });

                btn_addPost.setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), Admin_AddPost.class));
                });
                btn_viewCat.setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), Admin_ViewCategory.class));
                });
                btn_viewPost.setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), Admin_ViewPost.class));
                });
                logout.setOnClickListener(view -> {
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(), "Log Out Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Sign in Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
}