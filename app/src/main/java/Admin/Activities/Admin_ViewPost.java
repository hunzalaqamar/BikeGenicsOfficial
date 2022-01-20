package Admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikegenics.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Admin.Adapter.Admin_AdapterViewPost;
import Admin.DTO.Admin_DTOViewPost;

public class Admin_ViewPost extends AppCompatActivity {
Button viewPost_editPost_btn;
EditText viewPost_postDesc;
    List<Admin_DTOViewPost> ViewPostList = new ArrayList<>();
    RecyclerView recyclerView;
    Admin_AdapterViewPost mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_post);
        viewPost_postDesc = findViewById(R.id.user_viewpost_postdesc);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        viewPost_postDesc.setEnabled(false);

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), Admin_Home.class);
            startActivity(in);
        });
        viewPost_editPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Admin_EditPost.class);
                startActivity(in);
            }
        });
        if(fUser != null){
            prepareMovieData();
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ViewPost);
            mAdapter = new Admin_AdapterViewPost(getApplicationContext(), ViewPostList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);



        }
        else{
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        }
    }
    private void prepareMovieData() {
       /* DocumentReference docRef = db.collection("admin").document("category");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("I am data", "DocumentSnapshot data: " + document.getData());

                        for (Map.Entry<String,Object> entry : document.getData().entrySet()){
                            Log.d("data in map", entry.getValue().toString());

                            Admin_DTOViewPost cat = new Admin_DTOViewPost(entry.getValue().toString());
                            ViewPostList.add(cat);
                        }

                        for(int i=0; i<ViewPostList.size();i++){
                            Log.d("data in categoryname", ViewPostList.get(i).toString());
                        }
                        mAdapter.notifyDataSetChanged();

                    } else {
                        Log.d("I am data", "No such document");
                    }
                } else {
                    Log.d("Task not Successfull", "get failed with ", task.getException());
                }
            }
        });*/
    }
}