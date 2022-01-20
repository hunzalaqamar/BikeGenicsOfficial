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

import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class User_ViewPost extends AppCompatActivity {
Button editpost_btn,deletepost_btn;
TextView userfullname,dateandtime;
EditText desc;
ImageView user_pic,post_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_post);
        editpost_btn = findViewById(R.id.user_viewPost_editbtn);
        deletepost_btn = findViewById(R.id.user_viewPost_deletebtn);
        userfullname = findViewById(R.id.user_viewPost_fullname);
        dateandtime = findViewById(R.id.user_viewPost_datetime);
        desc = findViewById(R.id.user_addPost_postdesc);
        user_pic = findViewById(R.id.User_viewPost_userImage);
        post_pic = findViewById(R.id.user_addPost_postImage);

        desc.setEnabled(false);

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);

        TextView txt_back = findViewById(R.id.txt_back);

        Button btn_user_vp_edit = findViewById(R.id.user_viewPost_editbtn);

        btn_user_vp_edit.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_EditPost.class);
            startActivity(in);
            overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
        });

        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Home.class);
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
        bnv.getMenu().getItem(3).setChecked(true);

        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent vin = new Intent(getApplicationContext(), User_Home.class);
                    startActivity(vin);
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    break;
                case R.id.search:
                    Intent in = new Intent(getApplicationContext(), User_Search.class);
                    startActivity(in);
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
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