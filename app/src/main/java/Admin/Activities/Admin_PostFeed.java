package Admin.Activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import Admin.Adapter.Admin_AdapterPostFeed;
import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOViewPost;

public class Admin_PostFeed extends AppCompatActivity {
    List<Admin_DTOPostFeed> PostfeedList;
    RecyclerView recyclerView;
    Admin_AdapterPostFeed mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseFirestore db;
    Sprite doubleBounce;
    ProgressBar progressBar;
    EditText desc_txt;
    ArrayList<String> userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_postfeed);
        TextView txt_back = findViewById(R.id.txt_back);
        desc_txt = findViewById(R.id.user_postfeed_postdesc);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
        userdata = new ArrayList<>();
        PostfeedList = new ArrayList<>();


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
        if (fUser != null) {
            try {
                getPosts();
                txt_back.setOnClickListener(view -> {
                    Intent in = new Intent(getApplicationContext(), Admin_Home.class);
                    startActivity(in);
                });

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_PostFeed);
                mAdapter = new Admin_AdapterPostFeed(getApplicationContext(), PostfeedList);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(getApplicationContext());
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

    private void getPosts() {
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
//                            Log.d("doc id users", dc.getDocument().getId().toString());
//                            Log.d("docs users", dc.getDocument().toString());

                            for (Map.Entry<String, Object> entry : dc.getDocument().getData().entrySet()) {
                                userdata.add(entry.getValue().toString());
                            }

                            db.collection("users").document(dc.getDocument().getId().toString()).collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    try{
                                        for (int i=0; i<value.size();i++) {
                                            DocumentChange dc = value.getDocumentChanges().get(i);
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                                Log.d("doc id in user", dc.getDocument().getId().toString());
//                                                Log.d("docs in user", dc.getDocument().toString());
                                                PostfeedList.add(dc.getDocument().toObject(Admin_DTOPostFeed.class));

                                                Log.d("post feed here", PostfeedList.get(i).getPhoneNumber());
                                                Log.d("post feed here", PostfeedList.get(i).getPostCategory());
                                                Log.d("post feed here", PostfeedList.get(i).getPostImage());
                                                Log.d("post feed here", PostfeedList.get(i).getPostTime());
                                                Log.d("post feed here", PostfeedList.get(i).getPostDescription());
                                                Log.d("post feed here", PostfeedList.get(i).getFullName());
                                                Log.d("post feed here", PostfeedList.get(i).getProfileImage());

                                                for (int j = 0; j < userdata.size(); j++) {
                                                    if (userdata.get(j).toString().contains("https")) {
                                                        PostfeedList.get(i).setProfileImage(userdata.get(j).toString());
                                                    }
                                                    if (userdata.get(j).toString().matches("[a-zA-Z ]+")) {
                                                        PostfeedList.get(i).setFullName(userdata.get(j).toString());
                                                    }
                                                    if (userdata.get(j).toString().length() > 2 && userdata.get(j).toString().length() < 11 ) {
                                                        PostfeedList.get(i).setPhoneNumber(userdata.get(j).toString());
                                                    }
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                    }
//                    mAdapter.notifyDataSetChanged();

                    Log.d("post feed size", String.valueOf(PostfeedList.size()));
//                    for(int i=0; i<PostfeedList.size();i++){
//                        Log.d("postfeed here" , PostfeedList.get(i).toString());
//                    }





//                    for(int i=0; i<ViewPostList.size();i++){
//                        for(int j=0; j<temppost.size();j++){
//                            if(temppost.get(j).toString().contains("https")){
//                                ViewPostList.get(i).setProfileImage(temppost.get(j).toString());
//                            }
//                            if(temppost.get(j).toString().matches("[a-zA-Z]+")){
//                                ViewPostList.get(i).setFullName(temppost.get(j).toString());
//                            }
//                        }
//                        ViewPostList.get(i).setPostId(postIds.get(i).toString());
//                    }
//
//                    mAdapter.notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}