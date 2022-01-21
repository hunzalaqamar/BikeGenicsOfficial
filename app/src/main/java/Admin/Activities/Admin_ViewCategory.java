package Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Admin.Adapter.Admin_AdapterViewCategory;
import Admin.DTO.Admin_DTOViewCategory;

public class Admin_ViewCategory extends AppCompatActivity {
    List<Admin_DTOViewCategory> CategoryName = new ArrayList<>();
    RecyclerView recyclerView;
    Admin_AdapterViewCategory mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    ProgressBar progressBar;
    Sprite doubleBounce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_category);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        doubleBounce = new Wave();

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(doubleBounce);

        if(fUser != null){
            getCategories();
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view1a);
            mAdapter = new Admin_AdapterViewCategory(Admin_ViewCategory.this, CategoryName);
            RecyclerView.LayoutManager mLayoutManager = new
                    LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            TextView txt_back = findViewById(R.id.txt_back);
            txt_back.setOnClickListener(view -> {
                startActivity(new Intent(getApplicationContext(), Admin_Home.class));
            });

        }
        else{
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategories() {
        DocumentReference docRef = db.collection("admin").document("category");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("I am data", "DocumentSnapshot data: " + document.getData());

                        for (Map.Entry<String,Object> entry : document.getData().entrySet()){
                            Log.d("data in map", entry.getValue().toString());

                            Admin_DTOViewCategory cat = new Admin_DTOViewCategory(entry.getValue().toString());
                            CategoryName.add(cat);
                        }

//                        for(int i=0; i<CategoryName.size();i++){
//                            Log.d("data in categoryname", CategoryName.get(i).toString());
//                        }
                        mAdapter.notifyDataSetChanged();

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

}