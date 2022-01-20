package Admin.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bikegenics.R;

import java.util.List;

import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOViewCategory;

public class Admin_AdapterPostFeed extends RecyclerView.Adapter<Admin_AdapterPostFeed.MyViewHolder> {

    private List<Admin_DTOPostFeed> ADPostFeed;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public   ImageView image_post_show, image_user_show;
        public  Button delete_btn,contact_btn;
        public  TextView fullname_txt,date_time_txt;
        public  EditText desc_txt;
        public MyViewHolder(View view) {
            super(view);

            image_user_show = view.findViewById(R.id.postfeed_image_user_show);
            image_post_show = view.findViewById(R.id.user_postfeed_postImage);
            fullname_txt    = view.findViewById(R.id.postfeed_fullname_txt);
            date_time_txt   = view.findViewById(R.id.postfeed_date_time_txt);
            desc_txt        = view.findViewById(R.id.user_postfeed_postdesc);
            delete_btn      = view.findViewById(R.id.user_postfeed_deletebtn);
            contact_btn      = view.findViewById(R.id.user_postfeed_contactBtn);
            contact_btn.setOnClickListener(view1 -> {
                Toast.makeText(context, "Choot ka button", Toast.LENGTH_SHORT).show();
            });
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
        holder.image_user_show.setImageResource(ADPC.getUser_image());
        holder.fullname_txt.setText(ADPC.getUserName());
        holder.date_time_txt.setText(ADPC.getDateTime());
        holder.desc_txt.setText(ADPC.getDescription());
        holder.image_post_show.setImageResource(ADPC.getPost_Image());

    }
    @Override
    public int getItemCount() {
        return ADPostFeed.size();
    }
}