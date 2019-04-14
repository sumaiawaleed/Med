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

import medpatient.ye.ClassData.DoctorData;
import medpatient.ye.R;

public class AdapterDoctor extends RecyclerView.Adapter<AdapterDoctor.ViewHolder> {

    Context cxt;
    ArrayList<DoctorData> doctorData ;
    Button selectedDoc ;
    AlertDialog alertDialog ;
    String u_id_doc;
    public AdapterDoctor(Context activity, ArrayList<DoctorData> data, Button selectedDep,
                         AlertDialog alertDialog, String u_id_doc) {
        cxt=activity;
        doctorData=data;
        this.selectedDoc=selectedDep;
        this.u_id_doc=u_id_doc;
        this.alertDialog=alertDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new  ViewHolder(LayoutInflater.from(cxt).inflate(R.layout.cos_doc, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(doctorData.get(position));
    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView user_name,cons_price,txtDep;
        DoctorData mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            cons_price = itemView.findViewById(R.id.cons_price);
            txtDep = itemView.findViewById(R.id.txtDep);
            itemView.setOnClickListener(this);
        }

        public void bind(DoctorData doctorData){
            mItem = doctorData;
            user_name.setText(mItem.getUser_name());
            cons_price.setText("Price : "+mItem.getCons_price());
            txtDep.setText(mItem.getDepart_en_name()+" | "+mItem.getDepart_name());
        }

        @Override
        public void onClick(View view) {
            selectedDoc.setText(mItem.getUser_name());
            u_id_doc = mItem.getU_id_doc();
            alertDialog.cancel();
        }
    }
}
