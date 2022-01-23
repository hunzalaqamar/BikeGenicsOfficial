package User.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bikegenics.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;

import User.Activities.User_EditPost;
import User.Activities.User_ViewPost;
import User.DTO.User_DTOViewPost;


public class User_AdapterViewPost extends RecyclerView.Adapter<User_AdapterViewPost.MyViewHolder> {

    private List<User_DTOViewPost> UserViewPost;
    private Context context;
    public Button viewPost_editPost_btn, viewPost_delete_btn;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    FirebaseUser fUser;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView viewPost_userImage, viewPost_postImage;
        public TextView viewPost_userName, viewPost_dateTime;
        public EditText viewPost_postDesc;

        public MyViewHolder(View view) {
            super(view);
            viewPost_editPost_btn = view.findViewById(R.id.user_viewPost_editPost_btn);
            viewPost_delete_btn = view.findViewById(R.id.user_viewPost_delete_btn);
            viewPost_userImage = (ImageView) view.findViewById(R.id.user_viewPost_userImage);
            viewPost_postImage = (ImageView) view.findViewById(R.id.user_viewpost_postImage);
            viewPost_userName = (TextView) view.findViewById(R.id.user_viewPost_userName);
            viewPost_dateTime = (TextView) view.findViewById(R.id.user_viewPost_dateTime);
            viewPost_postDesc = (EditText) view.findViewById(R.id.user_viewpost_postdesc);
            mAuth = FirebaseAuth.getInstance();
            fUser = mAuth.getCurrentUser();

            db = FirebaseFirestore.getInstance();
            storage = FirebaseStorage.getInstance();
            viewPost_userImage.setClipToOutline(true);


            viewPost_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "This is post delete", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }

    public User_AdapterViewPost(Context cont, List<User_DTOViewPost> UserViewPost) {
        this.UserViewPost = UserViewPost;
        this.context = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_viewpost_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User_DTOViewPost ADVP = UserViewPost.get(position);
        Glide.with(context).load(ADVP.getProfileImage().toString()).dontAnimate().error(R.drawable.ic_no_image).into(holder.viewPost_userImage);
        holder.viewPost_userName.setText(ADVP.getFullName());
        holder.viewPost_dateTime.setText(ADVP.getPostTime());
        holder.viewPost_postDesc.setText(ADVP.getPostDescription());
        Glide.with(context).load(ADVP.getPostImage()).dontAnimate().error(R.drawable.ic_no_image).into(holder.viewPost_postImage);

        viewPost_editPost_btn.setOnClickListener(view1 -> {
            User_DTOViewPost post = UserViewPost.get(holder.getAdapterPosition());
            Gson gson = new Gson();
            String myJson = gson.toJson(post);
            Intent in = new Intent(context, User_EditPost.class);
            in.putExtra("postinformation", myJson);
            ((Activity)context).startActivity(in);
        });
        viewPost_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("users").document(fUser.getEmail().toString()).collection("posts").document(ADVP.getPostId().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        StorageReference storageReference = storage.getReferenceFromUrl(ADVP.getPostImage());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                                ((Activity)context).startActivity(new Intent(context, User_ViewPost.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Post Not Deleted" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return UserViewPost.size();
    }
}