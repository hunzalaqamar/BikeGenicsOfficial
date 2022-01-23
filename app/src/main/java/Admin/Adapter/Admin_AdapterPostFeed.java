package Admin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.List;

import Admin.Activities.Admin_Home;
import Admin.DTO.Admin_DTOPostFeed;
import Admin.DTO.Admin_DTOViewCategory;

public class Admin_AdapterPostFeed extends RecyclerView.Adapter<Admin_AdapterPostFeed.MyViewHolder> {

    private List<Admin_DTOPostFeed> ADPostFeed;
    private Context context;
    public Button contact_btn;
    View dialogView;
    ViewGroup viewGroup;


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
            viewGroup = view.findViewById(android.R.id.content);
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
            dialogView = LayoutInflater.from(view1.getContext()).inflate(R.layout.alert_custom_postfeed, viewGroup, false);
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            Button btn_call = dialogView.findViewById(R.id.btn_call);
            EditText contactnumber = (EditText) dialogView.findViewById(R.id.contactnumber);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();


            contactnumber.setText(ADPC.getPhoneNumber());
            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+ADPC.getPhoneNumber()));
                        ((Activity)context).startActivity(intent);
                        alertDialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return ADPostFeed.size();
    }
}