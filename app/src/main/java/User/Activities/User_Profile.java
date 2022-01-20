package User.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class User_Profile extends AppCompatActivity {
    EditText username,fullname,phonenumber,age,password;
     TextView login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullname = findViewById(R.id.user_profile_fullname_txt);
        age = findViewById(R.id.user_profile_age_txt);
        username = findViewById(R.id.user_profile_username_txt);
        password = findViewById(R.id.user_profile_password_txt);
        phonenumber = findViewById(R.id.user_profile_phone_txt);
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        Button edit_profile = findViewById(R.id.user_profile_editprofileBtn);
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);
        fullname.setEnabled(false);
        age.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
        phonenumber.setEnabled(false);
        TextView txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Settings.class);
            startActivity(in);

            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);

        });
        edit_profile.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_editProfile.class);
            startActivity(in);

            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

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
    }

    }
