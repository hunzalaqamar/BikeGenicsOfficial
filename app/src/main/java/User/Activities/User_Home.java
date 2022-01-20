package User.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bikegenics.LoginActivity;
import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class User_Home extends AppCompatActivity {
    Button contact_btn, delete_btn;
    TextView fullname, datetime;
    EditText desc;
    ImageView userImage, postimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        contact_btn = findViewById(R.id.user_postfeed_contactBtn);
        delete_btn = findViewById(R.id.user_postfeed_deletebtn);
        desc = findViewById(R.id.user_addPost_postdesc);
        fullname = findViewById(R.id.user_postfeed_userfullname);
        datetime = findViewById(R.id.user_postfeed_datetime);
        postimage = findViewById(R.id.user_addPost_postImage);
        userImage = findViewById(R.id.user_postfeed_userImage);
        desc.setEnabled(false);

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);

        TextView txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(in);
        });

        btn.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_AddPost.class);
            startActivity(in);
            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });

        bnv.setBackground(null);
        bnv.getMenu().getItem(2).setEnabled(false);


        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
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