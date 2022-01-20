package Admin.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bikegenics.R;

public class Admin_AddPost extends AppCompatActivity {
ImageView choose_image;
Button add_post,cancel_btn;
EditText product_name,product_desc;
Spinner spinner_category;
    Uri image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_post);

        TextView txt_back = findViewById(R.id.txt_back);
         choose_image = findViewById(R.id.user_addPost_postImage);
         product_name = findViewById(R.id.product_name_txt);
         product_desc = findViewById(R.id.product_desc_txt);
         add_post = findViewById(R.id.btn_addPost);
         cancel_btn = findViewById(R.id.btn_cancel);
         spinner_category = findViewById(R.id.category_spinner);


         txt_back.setOnClickListener(view -> {
                Intent in = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(in);
        });

            choose_image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mGetContent.launch("image/*");
                }
            });



        cancel_btn.setOnClickListener(view->{

            Intent in2 = new Intent(getApplicationContext(), AdminHome.class);
            startActivity(in2);

        });
    }

    //choose image working code
    ActivityResultLauncher<String> mGetContent;

    {
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            choose_image.setImageURI(result);
                            image = result;
                        }
                    }
                });
    }
}
