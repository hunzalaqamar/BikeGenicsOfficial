package User.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import User.Adapter.User_AdapterViewPost;
import User.DTO.ProfileDTO;

public class User_Profile extends AppCompatActivity {
    EditText username,fullname,phonenumber,age,email;
     TextView login_btn;
  ImageView user_profile_postImage;
    User_AdapterViewPost mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    Sprite doubleBounce;
    ProgressBar progressBar;
    ArrayList<String> temppost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullname = findViewById(R.id.user_profile_fullname_txt);
        age = findViewById(R.id.user_profile_age_txt);
        email = findViewById(R.id.user_profile_email_txt);
        phonenumber = findViewById(R.id.user_profile_phone_txt);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        temppost = new ArrayList<>();
        user_profile_postImage = findViewById(R.id.user_profile_postImage);
        doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
         if(fUser!=null)
         {
             getUserDetails();
             BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
             FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);
             TextView txt_back = findViewById(R.id.txt_back);

             txt_back.setOnClickListener(view -> {
                 Intent in = new Intent(getApplicationContext(), User_Settings.class);
                 startActivity(in);

                 overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);

             });


             btn.setOnClickListener(view ->{
                 Intent in = new Intent(getApplicationContext(), User_AddPost.class);
                 startActivity(in);
                 overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
             });

             bnv.setBackground(null);
             bnv.getMenu().getItem(2).setEnabled(false);
             bnv.getMenu().getItem(4).setChecked(true);


             bnv.setOnItemSelectedListener(item -> {
                 switch (item.getItemId()) {

                     case R.id.home:
                         Intent bin = new Intent(getApplicationContext(), User_Home.class);
                         startActivity(bin);
                         overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                         break;
                     case R.id.search:
                         Intent in = new Intent(getApplicationContext(), User_Search.class);
                         startActivity(in);
                         overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                         break;
                     case R.id.viewPost:
                         Intent vin = new Intent(getApplicationContext(), User_ViewPost.class);
                         startActivity(vin);
                         overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                         break;
                     case R.id.settings:
                         Intent sin = new Intent(getApplicationContext(), User_Settings.class);
                         startActivity(sin);
                         overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                         break;

                 }
                 return true;
             });
         }

    }

    private void getUserDetails(){

        progressBar.setVisibility(View.VISIBLE);

        try {
            DocumentReference docRef = db.collection("users").document(fUser.getEmail().toString());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ProfileDTO prfoileDto = documentSnapshot.toObject(ProfileDTO.class);
                    fullname.setText(prfoileDto.getFullName().toString());
                    age.setText(prfoileDto.getAge().toString());
                    email.setText(fUser.getEmail().toString());
                    phonenumber.setText(prfoileDto.getPhoneNumber().toString());
                    Glide.with(User_Profile.this).load(prfoileDto.getProfileImage().toString()).dontAnimate().error(R.drawable.ic_no_image).into(user_profile_postImage);
                    progressBar.setVisibility(View.GONE);

                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        moveTaskToBack(true);
                    }

                }).create().show();
    }

    }
