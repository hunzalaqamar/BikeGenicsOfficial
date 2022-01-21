package Admin.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikegenics.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Admin.Activities.Admin_Home;
import Admin.Activities.Admin_ViewCategory;
import Admin.DTO.Admin_DTOViewCategory;

public class Admin_AdapterViewCategory extends RecyclerView.Adapter<Admin_AdapterViewCategory.MyViewHolder> {

    private List<Admin_DTOViewCategory> ADCatView;
    private Context context;
    public Button btnEdit, btnDelete, btn_ok, btn_cancel;
    EditText alertcategoryName;
    ProgressBar progressBar;
    Sprite doubleBounce;
    FirebaseFirestore db;
    String catKey;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView categoryName;

        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.viewCategory_categoryName_txt);
            btnEdit = (Button) view.findViewById(R.id.viewCategory_editCategory_btn);
            btnDelete = (Button) view.findViewById(R.id.viewCategory_delete_btn);
            db = FirebaseFirestore.getInstance();

        }

        @Override
        public void onClick(View view) {
        }
    }
    public Admin_AdapterViewCategory(Context cont, List<Admin_DTOViewCategory> ADCatView) {
        this.ADCatView = ADCatView;
        this.context = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_viewcategory_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Admin_DTOViewCategory ADC = ADCatView.get(position);
        holder.categoryName.setText(ADC.getCategoryName());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.alert_custom, viewGroup, false);
                btn_ok = dialogView.findViewById(R.id.alertbuttonOk);
                btn_cancel = dialogView.findViewById(R.id.alertbuttonCancel);
                alertcategoryName = (EditText) dialogView.findViewById(R.id.alertcategoryName);
                progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
                doubleBounce = new Wave();
                progressBar.setIndeterminateDrawable(doubleBounce);
                progressBar.setVisibility(View.GONE);

                catKey  = ADCatView.get(holder.getAdapterPosition()).getCategoryName().toLowerCase(Locale.ROOT);
                alertcategoryName.setText(ADCatView.get(holder.getAdapterPosition()).getCategoryName());

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertcategoryName.getText().toString().equals("")) {
                            Toast.makeText(context, "Please Enter Required Information", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            btn_ok.setEnabled(false);
                            btn_cancel.setEnabled(false);
                            alertcategoryName.setEnabled(false);
                            catKey = catKey.replaceAll("\\s+","");
                            Map<String,Object> updates = new HashMap<>();
                            updates.put(catKey, FieldValue.delete());

                            db.collection("admin").document("category").update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    catKey = alertcategoryName.getText().toString().toLowerCase(Locale.ROOT);
                                    catKey = catKey.replaceAll("\\s+","");

                                    Map<String, Object> category = new HashMap<>();
                                    category.put(catKey, alertcategoryName.getText().toString());
                                    db.collection("admin").document("category")
                                            .set(category, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(context, "Category Updated Successfully", Toast.LENGTH_SHORT).show();
                                                    btn_ok.setEnabled(true);
                                                    btn_cancel.setEnabled(true);
                                                    alertcategoryName.setEnabled(true);
                                                    alertDialog.dismiss();
                                                    ((Activity)context).finish();
                                                    ((Activity)context).startActivity(new Intent(context, Admin_ViewCategory.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(context, "Category Not Updated" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    btn_ok.setEnabled(true);
                                                    btn_cancel.setEnabled(true);
                                                    alertcategoryName.setEnabled(true);
                                                    alertDialog.dismiss();
                                                }
                                            });
                                }
                            });


                        }
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catKey  = ADCatView.get(holder.getAdapterPosition()).getCategoryName().toLowerCase(Locale.ROOT);
                catKey = catKey.replaceAll("\\s+","");
                Map<String,Object> updates = new HashMap<>();
                updates.put(catKey, FieldValue.delete());

                db.collection("admin").document("category").update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Category Deleted Successfully", Toast.LENGTH_SHORT).show();
                        ((Activity)context).finish();
                        ((Activity)context).startActivity(new Intent(context, Admin_ViewCategory.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Category Not Deleted" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return ADCatView.size();
    }
}