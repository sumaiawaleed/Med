package medpatient.ye.ClassAdapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import java.util.ArrayList;

import medpatient.ye.ClassData.DepartmentData;
import medpatient.ye.R;
import medpatient.ye.SendConsaltionFragment;

public class AdapterDepartment extends RecyclerView.Adapter<AdapterDepartment.ViewHolder> {

    Context cxt;
    ArrayList<DepartmentData> departmentData ;
    Button selectedDep ;
    AlertDialog alertDialog ;
    public AdapterDepartment(Context activity, ArrayList<DepartmentData> data, Button selectedDep,
                             AlertDialog alertDialog) {
    cxt=activity;
    departmentData=data;
    this.selectedDep=selectedDep;
    this.alertDialog=alertDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new  ViewHolder(LayoutInflater.from(cxt).inflate(R.layout.cos_dep, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(departmentData.get(position));
    }

    @Override
    public int getItemCount() {
        return departmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView txtName;
        DepartmentData mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtDep);
            itemView.setOnClickListener(this);
        }

        public void bind(DepartmentData departmentData){
            mItem = departmentData;
            txtName.setText(departmentData.getDepNameEn()+" | "+departmentData.getDepNameAr());

        }

        @Override
        public void onClick(View view) {
           selectedDep.setText(mItem.getDepNameEn()+" | "+mItem.getDepNameAr());
           SendConsaltionFragment.dep_id = mItem.getDepID();
           alertDialog.cancel();
        }
    }
}
