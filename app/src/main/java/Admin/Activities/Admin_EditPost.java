package Admin.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.bumptech.glide.Glide;
import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.nio.channels.Selector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Admin.DTO.Admin_DTOViewPost;

public class Admin_EditPost extends AppCompatActivity {
Button edit_post_save_changes_btn,edit_post_cancel;
EditText edit_post_product_desc;
Spinner edit_post_selector_category;
ImageView edit_post_post_image;
    Sprite doubleBounce;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    Uri image, downloadUri;
    String description, cat;
    Admin_DTOViewPost ob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_post);

        TextView txt_back = findViewById(R.id.txt_back);

        edit_post_save_changes_btn = findViewById(R.id.edit_post_save_changes_btn);
        edit_post_selector_category = findViewById(R.id.edit_post_selector_category);
        edit_post_cancel = findViewById(R.id.edit_post_cancel_btn);
        edit_post_post_image = findViewById(R.id.edit_post_image_show);
        edit_post_product_desc = findViewById(R.id.edit_post_desc);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        doubleBounce = new Wave();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        progressBar.setIndeterminateDrawable(doubleBounce);


        if(firebaseUser != null){
            try{
                Gson gson = new Gson();
                ob = gson.fromJson(getIntent().getStringExtra("postinformation"), Admin_DTOViewPost.class);
                loadData();
                initializeSpinner();

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

                edit_post_save_changes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description = edit_post_product_desc.getText().toString();
                        cat = edit_post_selector_category.getSelectedItem().toString();
                        if (TextUtils.isEmpty(description)) {
                            edit_post_product_desc.setError("Product Description Cannot be Empty");
                            edit_post_product_desc.requestFocus();
                            enableField();
                            progressBar.setVisibility(View.GONE);
                        } else if (cat.equals("Please Select")) {
                            Toast.makeText(Admin_EditPost.this, "Please Enter Required Information", Toast.LENGTH_SHORT).show();
                            enableField();
                            progressBar.setVisibility(View.GONE);
                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            disableFields();
                            uploadimage();
                        }
                    }
                });

                edit_post_post_image.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mGetContent.launch("image/*");
                    }
                });

                edit_post_cancel.setOnClickListener(view->{
                    Admin_EditPost.this.finish();
                    startActivity(new Intent(getApplicationContext(), Admin_ViewPost.class));
                });

                txt_back.setOnClickListener(view -> {
                    Admin_EditPost.this.finish();
                    startActivity(new Intent(getApplicationContext(), Admin_ViewPost.class));
                });


            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void loadData(){
        disableFields();
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(Admin_EditPost.this).load(ob.getPostImage().toString()).dontAnimate().error(R.drawable.ic_no_image).into(edit_post_post_image);
        edit_post_product_desc.setText(ob.getPostDescription());

        enableField();
        progressBar.setVisibility(View.GONE);
    }

    private void disableFields() {
        edit_post_save_changes_btn.setEnabled(false);
        edit_post_selector_category.setEnabled(false);
        edit_post_cancel.setEnabled(false);
        edit_post_post_image.setEnabled(false);
        edit_post_product_desc.setEnabled(false);
    }

    private void enableField() {
        edit_post_save_changes_btn.setEnabled(true);
        edit_post_selector_category.setEnabled(true);
        edit_post_cancel.setEnabled(true);
        edit_post_post_image.setEnabled(true);
        edit_post_product_desc.setEnabled(true);
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
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Admin_EditPost.this, R.layout.spinner_row, categories);
                        edit_post_selector_category.setAdapter(spinnerArrayAdapter);

                        for (int i = 0; i < edit_post_selector_category.getCount(); i++) {
                            if (edit_post_selector_category.getItemAtPosition(i).toString().equalsIgnoreCase(ob.getPostCategory())) {
                                edit_post_selector_category.setSelection(i);
                                break;
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No such document" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Task not Successfull " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
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
            db.collection("admin").document("posts").collection("posts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    progressBar.setVisibility(View.GONE);
                    enableField();
                    deletePreviousPost();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Post Not Edited" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    enableField();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void deletePreviousPost() {
        db.collection("admin").document("posts").collection("posts").document(ob.getPostId().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                StorageReference storageReference = storage.getReferenceFromUrl(ob.getPostImage());
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Post Edited Successfully", Toast.LENGTH_SHORT).show();
                        Admin_EditPost.this.finish();
                        startActivity(new Intent(getApplicationContext(), Admin_ViewPost.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Post Not Edited" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}