package User.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User_AddPost extends AppCompatActivity {
    ImageView choose_image;
    Button add_post,cancel_btn;
    TextView txt_back;
    EditText product_name,product_desc;
    Spinner spinner_category;
    Uri image, downloadUri;
    Sprite doubleBounce;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    String description, cat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_post);
        txt_back = findViewById(R.id.txt_back);
        choose_image = findViewById(R.id.user_addPost_postImage);
        product_desc = findViewById(R.id.user_product_desc_txt);
        add_post = findViewById(R.id.user_Btn_add_post);
        cancel_btn = findViewById(R.id.user_btn_cancel);
        spinner_category = findViewById(R.id.user_category_spinner);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        doubleBounce = new Wave();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);

        TextView txt_back = findViewById(R.id.txt_back);
if(firebaseUser != null)
{
    try
    {  progressBar.setVisibility(View.VISIBLE);
        initializeSpinner();
        choose_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mGetContent.launch("image/*");

            }
        });


        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Home.class);
            startActivity(in);
            User_AddPost.this.finish();
            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });
        cancel_btn.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), User_Home.class);
            startActivity(in);
            User_AddPost.this.finish();
            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });


        bnv.setBackground(null);
        bnv.getMenu().getItem(2).setEnabled(false);
        bnv.getMenu().getItem(2).setChecked(true);


        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent vin = new Intent(getApplicationContext(), User_Home.class);
                    startActivity(vin);
                    User_AddPost.this.finish();
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    break;
                case R.id.search:
                    Intent in = new Intent(getApplicationContext(), User_Search.class);
                    startActivity(in);
                    User_AddPost.this.finish();
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    break;
                case R.id.viewPost:
                    Intent in1 = new Intent(getApplicationContext(), User_ViewPost.class);
                    startActivity(in1);
                    User_AddPost.this.finish();
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    break;
                case R.id.settings:
                    Intent sin = new Intent(getApplicationContext(), User_Settings.class);
                    startActivity(sin);
                    User_AddPost.this.finish();
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                    break;

            }
            return true;
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = product_desc.getText().toString();
                cat = spinner_category.getSelectedItem().toString();
                if (TextUtils.isEmpty(description)) {
                    product_desc.setError("Product Description Cannot be Empty");
                    product_desc.requestFocus();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                } else if (cat.equals("Please Select")) {
                    Toast.makeText(User_AddPost.this, "Please Enter Required Information", Toast.LENGTH_SHORT).show();
                    enableField();
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    disableFields();
                    uploadimage();
                }
            }
        });
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}
else
{
    Toast.makeText(User_AddPost.this, "Please Sign In Again", Toast.LENGTH_SHORT).show();
}
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
                            choose_image.setImageURI(result);
                            image = result;
                        }
                    }
                });
    }
    private void initializeSpinner() {
        disableFields();
        DocumentReference docRef = db.collection("admin").document("category");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    enableField();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> categories = new ArrayList<String>();
                        categories.add("Please Select");

                        for (Map.Entry<String,Object> entry : document.getData().entrySet()){
                            categories.add(entry.getValue().toString());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(User_AddPost.this, R.layout.spinner_row, categories);
                        spinner_category.setAdapter(spinnerArrayAdapter);

                    } else {
                        Log.d("I am data", "No such document");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Task not Successfull " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Task not Successfull", "get failed with ", task.getException());
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
    private void disableFields() {
        product_desc.setEnabled(false);
        spinner_category.setEnabled(false);
        cancel_btn.setEnabled(false);
        add_post.setEnabled(false);
        txt_back.setEnabled(false);
    }
    private void clearFields() {
        product_desc.setText("");
        spinner_category.setSelection(0);
        choose_image.setImageResource(R.drawable.ic_upload);
    }

    private void enableField() {
        product_desc.setEnabled(true);
        spinner_category.setEnabled(true);
        cancel_btn.setEnabled(true);
        add_post.setEnabled(true);
        txt_back.setEnabled(true);
    }

    private void uploadimage() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(doubleBounce);
        disableFields();

        if (image != null) {
            StorageReference reference = storage.getReference().child("image/" + UUID.randomUUID().toString());
            UploadTask uploadTask = reference.putFile(image);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        addPost();
                    } else {
                        Toast.makeText(getApplicationContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        enableField();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
            enableField();
            progressBar.setVisibility(View.GONE);
        }
    }
    private void addPost() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        Map<String, Object> post = new HashMap<>();
        post.put("postCategory", cat);
        post.put("postDescription", description);
        post.put("postImage", downloadUri.toString());
        post.put("postTime", formatter.format(date));

        try {
            db.collection("users").document(firebaseUser.getEmail().toString()).collection("posts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Post Added Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    enableField();
                    clearFields();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Post Not Added" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    enableField();
                }
            });
            add_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    description = product_desc.getText().toString();
                    cat = spinner_category.getSelectedItem().toString();
                    if (TextUtils.isEmpty(description)) {
                        product_desc.setError("Product Description Cannot be Empty");
                        product_desc.requestFocus();
                        enableField();
                        progressBar.setVisibility(View.GONE);
                    } else if (cat.equals("Please Select")) {
                        Toast.makeText(User_AddPost.this, "Please Enter Required Information", Toast.LENGTH_SHORT).show();
                        enableField();
                        progressBar.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        disableFields();
                        uploadimage();
                    }
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}