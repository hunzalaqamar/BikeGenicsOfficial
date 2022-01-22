package Admin.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bikegenics.R;

import java.util.List;

import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOViewCategory;

public class Admin_AdapterPostFeed extends RecyclerView.Adapter<Admin_AdapterPostFeed.MyViewHolder> {

    private List<Admin_DTOPostFeed> ADPostFeed;
    private Context context;
    public Button contact_btn;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image_post_show, image_user_show;
        public TextView fullname_txt, date_time_txt;
        public EditText desc_txt;

        public MyViewHolder(View view) {
            super(view);
            image_user_show = view.findViewById(R.id.postfeed_image_user_show);
            image_post_show = view.findViewById(R.id.user_postfeed_postImage);
            date_time_txt = view.findViewById(R.id.postfeed_date_time_txt);
            fullname_txt = view.findViewById(R.id.fullname_txt_postfeed);
            desc_txt = view.findViewById(R.id.user_postfeed_postdesc);
            contact_btn = view.findViewById(R.id.Postfeed_contact_btn);
            image_user_show.setClipToOutline(true);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public Admin_AdapterPostFeed(Context cont, List<Admin_DTOPostFeed> ADPostFeed) {
        this.ADPostFeed = ADPostFeed;
        this.context = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_postfeed_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Admin_DTOPostFeed ADPC = ADPostFeed.get(position);
        Glide.with(context).load(ADPC.getProfileImage().toString()).dontAnimate().error(R.drawable.ic_no_image).into(holder.image_user_show);
        holder.fullname_txt.setText(ADPC.getFullName());
        holder.date_time_txt.setText(ADPC.getPostTime());
        holder.desc_txt.setText(ADPC.getPostDescription());
        Glide.with(context).load(ADPC.getPostImage().toString()).dontAnimate().error(R.drawable.ic_no_image).into(holder.image_post_show);

        contact_btn.setOnClickListener(view1 -> {
            Toast.makeText(context, ADPC.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return ADPostFeed.size();
    }
}