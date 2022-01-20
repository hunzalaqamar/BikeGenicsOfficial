package Admin.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bikegenics.R;

import java.util.List;

import Admin.DTO.Admin_DTOViewCategory;

public class Admin_AdapterViewCategory extends RecyclerView.Adapter<Admin_AdapterViewCategory.MyViewHolder> {

    private List<Admin_DTOViewCategory> ADCatView;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView categoryName;
        public Button btnEdit;
        public Button btnDelete;
        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.viewCategory_categoryName_txt);
            btnEdit = (Button) view.findViewById(R.id.viewCategory_editCategory_btn);
            btnDelete = (Button) view.findViewById(R.id.viewCategory_delete_btn);

            btnEdit.setOnClickListener(view1 -> {
                Toast.makeText(context, "Choot ka button", Toast.LENGTH_SHORT).show();
            });
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
    }
    @Override
    public int getItemCount() {
        return ADCatView.size();
    }
}