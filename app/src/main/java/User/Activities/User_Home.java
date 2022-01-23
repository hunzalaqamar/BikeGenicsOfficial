package User.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikegenics.LoginActivity;
import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Admin.Activities.Admin_PostFeed;
import Admin.Adapter.Admin_AdapterPostFeed;
import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOPostFeed2;

public class User_Home extends AppCompatActivity {
    List<Admin_DTOPostFeed> PostfeedList;
    RecyclerView recyclerView;
    Admin_AdapterPostFeed mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    Sprite doubleBounce;
    TextView txt_back;
    ProgressBar progressBar;
    Admin_DTOPostFeed2 adminDetails;
    int indexPL, indexUL;
    BottomNavigationView bnv;
    FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
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
        bnv = findViewById(R.id.bottomNavigationView);
        btn = (FloatingActionButton) findViewById(R.id.fab);
        txt_back = findViewById(R.id.txt_back);

        if (fUser != null) {
            try {
                progressBar.setVisibility(View.VISIBLE);
                getUserPosts();
                getAdminPosts();

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_PostFeed);
                mAdapter = new Admin_AdapterPostFeed(User_Home.this, PostfeedList);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(User_Home.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

                txt_back.setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                });

                btn.setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), User_AddPost.class));
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


            } catch (Exception e) {
                e.printStackTrace();
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
                                    try {
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
                                    } catch (Exception e) {
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

    private void getAdminDetails() {
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
                                    try {
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
                                    } catch (Exception e) {
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