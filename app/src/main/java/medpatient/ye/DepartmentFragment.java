package medpatient.ye;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import medpatient.ye.ClassAdapter.AdapterDepartment;
import medpatient.ye.ClassData.DepartmentData;
import medpatient.ye.ClassData.LoadData;

public class DepartmentFragment extends Fragment {

    ArrayList<DepartmentData> departmentData;
    LoadData loadData;

    public DepartmentFragment() {
        // Required empty public constructor
    }


    public static DepartmentFragment newInstance() {
        return new DepartmentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadData = new LoadData(getActivity());
        departmentData = new ArrayList<>();
        departmentData = loadData.getAllDepartment();

        View view = inflater.inflate(R.layout.fragment_department, container, false);


            Toast.makeText(getActivity(), ""+departmentData.size(), Toast.LENGTH_SHORT).show();
            final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            AdapterDepartmentPage adapter = new AdapterDepartmentPage(departmentData);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);


        return view;
    }


    public class AdapterDepartmentPage extends RecyclerView.Adapter<ViewHolderPage>{

        ArrayList<DepartmentData> departmentData;

        public AdapterDepartmentPage(ArrayList<DepartmentData> departmentData) {
            this.departmentData = departmentData;
        }

        @NonNull
        @Override
        public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new  ViewHolderPage(LayoutInflater.from(getActivity()).inflate(R.layout.custom_department_page, parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
            holder.bind(departmentData.get(position));
        }

        @Override
        public int getItemCount() {
            return departmentData.size();
        }
    }

    public class ViewHolderPage extends RecyclerView.ViewHolder{
        ImageView dept_img;
        public ViewHolderPage(View itemView) {
            super(itemView);
            dept_img = itemView.findViewById(R.id.dept_img);
            //dept_img.setBackground(R.drawable.placeholder_thumb);
        }

        public void bind(DepartmentData dept){
            Picasso.with(getActivity())
                    .load(dept.getImg_url())
                    .placeholder(R.drawable.clover1)
                    .error(R.drawable.clover)
                    .into(dept_img);
        }
    }


}
