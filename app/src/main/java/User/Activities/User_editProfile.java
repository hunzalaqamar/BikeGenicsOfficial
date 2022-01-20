package User.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class User_editProfile extends AppCompatActivity {
Uri image;
ImageView postimage;
    EditText username,fullname,phonenumber,age,password;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);

         fullname = findViewById(R.id.user_editprofile_fullname_txt);
         age = findViewById(R.id.user_editProfile_age_txt);
         username = findViewById(R.id.user_editProfile_username_txt);
         password = findViewById(R.id.user_editProfile_password_txt);
         phonenumber = findViewById(R.id.user_editProfile_phone_txt);
         Button cancel = findViewById(R.id.user_edit_profile_cancel_btn);
postimage = findViewById(R.id.user_editProfile_postImage);
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);

        TextView txt_back = findViewById(R.id.txt_back);
        postimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mGetContent.launch("image/*");

            }
        });
        cancel.setOnClickListener(view->{
                    Intent in = new Intent(getApplicationContext(), User_Profile.class);
                    startActivity(in);

                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                }
                );
        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Profile.class);
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
    }ActivityResultLauncher<String> mGetContent;

    {
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            Toast.makeText(getApplicationContext(),"Your image is selected",Toast.LENGTH_SHORT).show();

                            postimage.setImageURI(result);
                            image = result;
                        }
                    }
                });
    }
}