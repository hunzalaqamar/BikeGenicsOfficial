package User.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class About_Us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        EditText text = findViewById(R.id.user_aboutus_postdesc);

        text.setEnabled(false);
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
                    Intent in = new Intent(getApplicationContext(), User_Home.class);
                    startActivity(in);
                    About_Us.this.finish();
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    break;
                case R.id.search:
                    Intent sin = new Intent(getApplicationContext(), User_Search.class);
                    startActivity(sin);
                    About_Us.this.finish();
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    break;
                case R.id.viewPost:
                    Intent vin = new Intent(getApplicationContext(), User_ViewPost.class);
                    startActivity(vin);
                    About_Us.this.finish();
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    break;
                case R.id.settings:
                    Intent min = new Intent(getApplicationContext(), User_Settings.class);
                    startActivity(min);
                    About_Us.this.finish();
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
