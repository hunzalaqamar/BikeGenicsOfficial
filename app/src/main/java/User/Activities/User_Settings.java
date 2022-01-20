package User.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikegenics.LoginActivity;
import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class User_Settings extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        Button profile = findViewById(R.id.btn_profile);
        Button about_us = findViewById(R.id.about_us);
        Button logout = findViewById(R.id.user_settings_log_out);
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);

        TextView txt_back = findViewById(R.id.txt_back);

        profile.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Profile.class);
            startActivity(in);

            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });
        about_us.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), About_Us.class);
            startActivity(in);
            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });

        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Toast.makeText(getApplicationContext(), "Logout Successfull", Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
        });


        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Home.class);
            startActivity(in);

            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
        });

        btn.setOnClickListener(view -> {
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
                    Intent in = new Intent(getApplicationContext(), User_Home.class);
                    startActivity(in);
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    break;
                case R.id.search:
                    Intent sin = new Intent(getApplicationContext(), User_Search.class);
                    startActivity(sin);
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    break;
                case R.id.viewPost:
                    Intent vin = new Intent(getApplicationContext(), User_ViewPost.class);
                    startActivity(vin);
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
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