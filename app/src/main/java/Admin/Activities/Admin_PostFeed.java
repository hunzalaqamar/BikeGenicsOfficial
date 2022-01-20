package Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikegenics.R;
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

import Admin.Adapter.Admin_AdapterPostFeed;
import Admin.Adapter.Admin_AdapterViewCategory;
import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOViewCategory;

public class Admin_PostFeed extends AppCompatActivity {
    List<Admin_DTOPostFeed> PostfeedList = new ArrayList<>();
    RecyclerView recyclerView;
    Admin_AdapterPostFeed mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
EditText desc_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_postfeed);
        TextView txt_back = findViewById(R.id.txt_back);
         desc_txt = findViewById(R.id.user_postfeed_postdesc);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        desc_txt.setEnabled(false);

        txt_back.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), AdminHome.class);
            startActivity(in);
        });
//        contact_btn.setOnClickListener(view ->{
//            new AlertDialog.Builder(this)
//                    .setTitle("Contact Information")
//                    .setMessage("")////show contact code here
//                    .setNegativeButton(android.R.string.no, null)
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface arg0, int arg1) {
//
//                        }
//
//                    }).create().show();
//        });
        if(fUser != null){
            prepareMovieData();
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_PostFeed);
            mAdapter = new Admin_AdapterPostFeed(getApplicationContext(), PostfeedList);
            RecyclerView.LayoutManager mLayoutManager = new
                    LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);



        }
        else{
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        }

    }

    private void prepareMovieData() {
     /*   DocumentReference docRef = db.collection("admin").document("category");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("I am data", "DocumentSnapshot data: " + document.getData());

                        for (Map.Entry<String,Object> entry : document.getData().entrySet()){
                            Log.d("data in map", entry.getValue().toString());

                            Admin_DTOViewCategory cat = new Admin_DTOViewCategory(entry.getValue().toString());
                            CategoryName.add(cat);
                        }

                        for(int i=0; i<CategoryName.size();i++){
                            Log.d("data in categoryname", CategoryName.get(i).toString());
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