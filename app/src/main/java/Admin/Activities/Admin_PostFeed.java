package Admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import Admin.Adapter.Admin_AdapterPostFeed;
import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOPostFeed2;
import Admin.DTO.Admin_DTOViewPost;

public class Admin_PostFeed extends AppCompatActivity {
    List<Admin_DTOPostFeed> PostfeedList;
    RecyclerView recyclerView;
    Admin_AdapterPostFeed mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    TextView txt_back;
    Sprite doubleBounce;
    ProgressBar progressBar;
    Admin_DTOPostFeed2 adminDetails;
    int indexPL, indexUL;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_postfeed);
        txt_back = findViewById(R.id.txt_back);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        PostfeedList = new ArrayList<>();
        indexPL = 0;
        indexUL = 0;
        adminDetails = new Admin_DTOPostFeed2();

        if (fUser != null) {
            try {
                progressBar.setVisibility(View.VISIBLE);
                getUserPosts();
                getAdminPosts();

                txt_back.setOnClickListener(view -> {
                    Admin_PostFeed.this.finish();
                    startActivity(new Intent(getApplicationContext(), Admin_Home.class));
                });

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_PostFeed);
                mAdapter = new Admin_AdapterPostFeed(Admin_PostFeed.this, PostfeedList);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(Admin_PostFeed.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please Login Again", Toast.LENGTH_SHORT).show();
        }

    }

    private void getAdminPosts() {
        getAdminDetails();
        db.collection("admin").document("posts").collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    for (DocumentChange admindc : value.getDocumentChanges()) {
                        Log.d("iteration=>", String.valueOf(indexUL));
                        if (admindc.getType() == DocumentChange.Type.ADDED) {
                            db.collection("admin").document("posts").collection("posts").document(admindc.getDocument().getId().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    try{
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                if (admindc.getType() == DocumentChange.Type.ADDED) {
                                                    PostfeedList.add(admindc.getDocument().toObject(Admin_DTOPostFeed.class));
                                                    PostfeedList.get(indexPL).setFullName(adminDetails.getFullName());
                                                    PostfeedList.get(indexPL).setPhoneNumber(adminDetails.getPhoneNumber());
                                                    PostfeedList.get(indexPL).setProfileImage(adminDetails.getProfileImage());
                                                    indexPL++;
                                                    mAdapter.notifyDataSetChanged();
                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), "No such document " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Task not Successfull " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                        }
                                    }catch (Exception e){
                                        Toast.makeText(getApplicationContext(), "Task not Successfull " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    private void getAdminDetails(){
        db.collection("admin").document("admin@bg.com").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        adminDetails = document.toObject(Admin_DTOPostFeed2.class);
                    } else {
                        Log.d("I am data", "No such document");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Task not Successfull " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void getUserPosts() {
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    for (DocumentChange userdc : value.getDocumentChanges()) {
                        if (userdc.getType() == DocumentChange.Type.ADDED) {
                            db.collection("users").document(userdc.getDocument().getId().toString()).collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    try{
                                        for (DocumentChange dc : value.getDocumentChanges()) {
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                PostfeedList.add(dc.getDocument().toObject(Admin_DTOPostFeed.class));
                                                PostfeedList.get(indexPL).setFullName(userdc.getDocument().toObject(Admin_DTOPostFeed2.class).getFullName());
                                                PostfeedList.get(indexPL).setPhoneNumber(userdc.getDocument().toObject(Admin_DTOPostFeed2.class).getPhoneNumber());
                                                PostfeedList.get(indexPL).setProfileImage(userdc.getDocument().toObject(Admin_DTOPostFeed2.class).getProfileImage());
                                                indexPL++;
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}