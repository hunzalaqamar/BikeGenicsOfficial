package User.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikegenics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class User_EditPost extends AppCompatActivity {
Button cancel_btn, editpost_btn;
EditText postname,postDesc;
Spinner postcategory;
ImageView postImage;
Uri image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_post);
   cancel_btn = findViewById(R.id.user_editpost_cancelbtn);
   editpost_btn = findViewById(R.id.user_editpost_editpostbtn);
   postname = findViewById(R.id.user_addPost_postname);
   postDesc = findViewById(R.id.user_addPost_postdesc);
   postcategory = findViewById(R.id.user_addPost_categoryname);
   postImage = findViewById(R.id.user_addPost_postImage);
        postImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mGetContent.launch("image/*");

            }
        });
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);

        TextView txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(),  User_AddPost.class);
            startActivity(in);
            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });
        cancel_btn.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(),  User_AddPost.class);
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

        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent vin = new Intent(getApplicationContext(), User_Home.class);
                    startActivity(vin);
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    break;
                case R.id.search:
                    Intent in = new Intent(getApplicationContext(), User_Search.class);
                    startActivity(in);
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    break;
                case R.id.viewPost:
                    Intent in1 = new Intent(getApplicationContext(), User_ViewPost.class);
                    startActivity(in1);
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
    ActivityResultLauncher<String> mGetContent;

    {
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
 Toast.makeText(getApplicationContext(),"Your image is selected",Toast.LENGTH_SHORT).show();

                            postImage.setImageURI(result);
                            image = result;
                        }
                    }
                });
    }
}