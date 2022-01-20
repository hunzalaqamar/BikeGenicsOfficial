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
import android.widget.TextView;

import com.example.bikegenics.R;

import java.nio.channels.Selector;

public class Admin_EditPost extends AppCompatActivity {
Button edit_post_save_changes_btn,edit_post_cancel;
EditText edit_post_product_name,edit_post_product_desc;
Selector edit_post_selector_category;
ImageView edit_post_post_image;
    Uri image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_post);

        TextView txt_back = findViewById(R.id.txt_back);

        edit_post_save_changes_btn = findViewById(R.id.edit_post_save_changes_btn);
        edit_post_cancel = findViewById(R.id.edit_post_cancel_btn);
        edit_post_post_image = findViewById(R.id.edit_post_image_show);
        edit_post_product_desc = findViewById(R.id.edit_post_desc);
        edit_post_product_name = findViewById(R.id.edit_post_product_name);

        edit_post_post_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mGetContent.launch("image/*");
            }
        });
        edit_post_cancel.setOnClickListener(view->{
            Intent in = new Intent(getApplicationContext(), Admin_ViewPost.class);
            startActivity(in);

        });
        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), AdminHome.class);
            startActivity(in);
        });



    }
    ActivityResultLauncher<String> mGetContent;

    {
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            edit_post_post_image.setImageURI(result);
                            image = result;
                        }
                    }
                });
    }
}