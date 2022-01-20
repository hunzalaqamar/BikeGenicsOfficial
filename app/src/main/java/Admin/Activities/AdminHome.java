package Admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.bikegenics.LoginActivity;
import com.example.bikegenics.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Button btn_addPost = findViewById(R.id.btn_addPost);
        Button btn_viewPost = findViewById(R.id.btn_viewPost);
        Button logout = findViewById(R.id.btn_logout);
        Button btn_viewCat = findViewById(R.id.btn_viewCat);
        Button btn_pf = findViewById(R.id.btn_postfeed);
        Button addCategory_btn = findViewById(R.id.btn_addCat);
        mAuth = FirebaseAuth.getInstance();

         btn_pf.setOnClickListener(view ->{
             Intent in1 = new Intent(getApplicationContext(), Admin_PostFeed.class);
             startActivity(in1);
         });

        btn_addPost.setOnClickListener(view -> {
            Intent in2 = new Intent(getApplicationContext(), Admin_AddPost.class);
            startActivity(in2);
        });
        btn_viewCat.setOnClickListener(view -> {
            Intent in3 = new Intent(getApplicationContext(), Admin_ViewCategory.class);
            startActivity(in3);
        });
        btn_viewPost.setOnClickListener(view -> {
            Intent in4 = new Intent(getApplicationContext(), Admin_ViewPost.class);
            startActivity(in4);
        });
         logout.setOnClickListener(view -> {
            mAuth.signOut();
             Toast.makeText(getApplicationContext(), "Log Out Successfully", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }
}