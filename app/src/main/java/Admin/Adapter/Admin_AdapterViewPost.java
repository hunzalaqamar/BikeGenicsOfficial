package Admin.Adapter;

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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bikegenics.R;

import java.util.List;

import Admin.Activities.Admin_EditPost;
import Admin.DTO.Admin_DTOViewPost;

public class Admin_AdapterViewPost extends RecyclerView.Adapter<Admin_AdapterViewPost.MyViewHolder> {

    private List<Admin_DTOViewPost> ADViewPost;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView viewPost_userImage, viewPost_postImage;
        public TextView viewPost_userName, viewPost_dateTime;
        public Button viewPost_editPost_btn;
        public EditText viewPost_postDesc;

        public MyViewHolder(View view) {
            super(view);
            viewPost_editPost_btn = view.findViewById(R.id.viewPost_editPost_btn);
            viewPost_userImage = (ImageView) view.findViewById(R.id.viewPost_userImage);
            viewPost_postImage = (ImageView) view.findViewById(R.id.user_viewpost_postImage);
            viewPost_userName = (TextView) view.findViewById(R.id.viewPost_userName);
            viewPost_dateTime = (TextView) view.findViewById(R.id.viewPost_dateTime);
            viewPost_postDesc = (EditText) view.findViewById(R.id.user_viewpost_postdesc);

            viewPost_editPost_btn.setOnClickListener(view1 -> {
                ((Activity)context).startActivity(new Intent(context, Admin_EditPost.class));
            });
        }

        @Override
        public void onClick(View view) {
        }
    }

    public Admin_AdapterViewPost(Context cont, List<Admin_DTOViewPost> ADViewPost) {
        this.ADViewPost = ADViewPost;
        this.context = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_viewpost_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Admin_DTOViewPost ADVP = ADViewPost.get(position);
        Glide.with(context).load(ADVP.getProfileImage().toString()).into(holder.viewPost_userImage);
        holder.viewPost_userName.setText(ADVP.getFullName());
        holder.viewPost_dateTime.setText(ADVP.getPostTime());
        holder.viewPost_postDesc.setText(ADVP.getPostDescription());
        Glide.with(context).load(ADVP.getPostImage()).into(holder.viewPost_postImage);
    }

    @Override
    public int getItemCount() {
        return ADViewPost.size();
    }
}