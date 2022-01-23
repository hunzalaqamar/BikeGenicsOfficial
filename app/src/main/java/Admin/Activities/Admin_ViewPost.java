package Admin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Admin.Adapter.Admin_AdapterViewPost;
import Admin.DTO.Admin_DTOViewPost;

public class Admin_ViewPost extends AppCompatActivity {
    List<Admin_DTOViewPost> ViewPostList = new ArrayList<>();
    List <String> postIds = new ArrayList<>();
    RecyclerView recyclerView;
    Admin_AdapterViewPost mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    Sprite doubleBounce;
    ProgressBar progressBar;
    ArrayList<String> temppost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_post);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
        temppost = new ArrayList<>();


        if (fUser != null) {
            try {
                getAdminDetails();
                TextView txt_back = findViewById(R.id.txt_back);
                txt_back.setOnClickListener(view -> {
                    Admin_ViewPost.this.finish();
                    startActivity(new Intent(getApplicationContext(), Admin_Home.class));
                });
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ViewPost);
                mAdapter = new Admin_AdapterViewPost(Admin_ViewPost.this, ViewPostList);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAdminPosts() {
                db.collection("admin").document("posts").collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        try{
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    ViewPostList.add(dc.getDocument().toObject(Admin_DTOViewPost.class));
                                    postIds.add(dc.getDocument().getId());
                                }
                            }

                            for(int i=0; i<ViewPostList.size();i++){
                                for(int j=0; j<temppost.size();j++){
                                    if(temppost.get(j).toString().contains("https")){
                                        ViewPostList.get(i).setProfileImage(temppost.get(j).toString());
                                    }
                                    if(temppost.get(j).toString().matches("[a-zA-Z]+")){
                                        ViewPostList.get(i).setFullName(temppost.get(j).toString());
                                    }
                                }
                                ViewPostList.get(i).setPostId(postIds.get(i).toString());
                            }

                            mAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                        }catch (Exception ex){
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
                        for (Map.Entry<String,Object> entry : document.getData().entrySet()){
                            temppost.add(entry.getValue().toString());
                        }

                        getAdminPosts();

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
}