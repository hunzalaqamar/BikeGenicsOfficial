package User.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;

import User.Adapter.User_AdapterViewPost;
import User.DTO.User_DTOViewPost;

public class User_ViewPost extends AppCompatActivity {
    List<User_DTOViewPost> ViewPostList = new ArrayList<>();
    List <String> postIds = new ArrayList<>();
    RecyclerView recyclerView;
    User_AdapterViewPost mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    Sprite doubleBounce;
    ProgressBar progressBar;
    ArrayList<String> temppost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_post);
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);
        TextView txt_back = findViewById(R.id.txt_back);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
        temppost = new ArrayList<>();

        if (mAuth != null) {
try{

    getUserDetails();
            txt_back.setOnClickListener(view -> {
                Intent in = new Intent(getApplicationContext(), User_Home.class);
                startActivity(in);
                overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
            });

            btn.setOnClickListener(view -> {
                Intent in = new Intent(getApplicationContext(), User_AddPost.class);
                startActivity(in);
                overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
            });
    recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view_ViewPost);
    mAdapter = new User_AdapterViewPost(User_ViewPost.this, ViewPostList);
    RecyclerView.LayoutManager mLayoutManager = new
            LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);

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
catch (Exception  e)
            {
                e.getStackTrace();
            }
        }


        else
        {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    private void getUserPosts() {
        db.collection("users").document(fUser.getEmail().toString()).collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                try{
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            ViewPostList.add(dc.getDocument().toObject(User_DTOViewPost.class));
                            postIds.add(dc.getDocument().getId());
                        }
                    }

                    for(int i=0; i<ViewPostList.size();i++){
                        for(int j=0; j<temppost.size();j++){
                            if(temppost.get(j).toString().contains("https")){
                                ViewPostList.get(i).setProfileImage(temppost.get(j).toString());


                            }
                            if(temppost.get(j).toString().matches("[a-zA-Z ]+")){
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
    private void getUserDetails(){
        db.collection("users").document(fUser.getEmail().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (Map.Entry<String,Object> entry : document.getData().entrySet()){
                            temppost.add(entry.getValue().toString());
                        }

                        for(int i=0;i<temppost.size();i++){
                            Log.d("temppost", temppost.get(i).toString());
                        }

                        getUserPosts();

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