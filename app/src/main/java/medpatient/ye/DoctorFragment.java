package medpatient.ye;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import medpatient.ye.ClassAdapter.AdapterDoctor;
import medpatient.ye.ClassData.DoctorData;
import medpatient.ye.ClassData.LoadData;
import medpatient.ye.appsettings.SaveSetting;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment {

    private RecyclerView recyclerView;
    private CAdapter adapter ;
    LoadData loadData;
    private ArrayList<DoctorData> doctorsList;
    private RequestQueue dRequestQueu;
    private DoctorData doc;
    public static String URL = SaveSetting.ServerURL+"get_all_doctors";

    public static Fragment newInstance() {
        return new DoctorFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        buildQuery();
        loadData = new LoadData(getActivity());
        doctorsList = new ArrayList<>();
        dRequestQueu = Volley.newRequestQueue(getActivity());
        recyclerView = view.findViewById(R.id.contentRec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAllDoctor("0","0","0");

        Toast.makeText(getActivity(), "URL: " + URL, Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment


        return view;
    }

    class CAdapter extends RecyclerView.Adapter<CAdapter.CViewholder> {
        int resources=0;
        Context context;
        List<DoctorData> docList;

        public CAdapter() {
        }

        public CAdapter(List<DoctorData> docList , Context context, int resources) {
            this.docList = docList;
            this.context=context;
            this.resources = resources;
        }

        @NonNull
        @Override
        public CViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CViewholder viewholder=new CViewholder(LayoutInflater.from(context).inflate(resources,parent,false)) ;
            return viewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull CViewholder holder, int position) {
            holder.vName.setText(docList.get(position).getUser_name());
            holder.vPhone.setText(String.valueOf(docList.get(position).getEmail()));
            holder.price.setText(docList.get(position).getCons_price()+" $");
            holder.img(docList.get(position).getImg_url(),docList.get(position).getUser_name());

        }

        @Override
        public int getItemCount() {
            return docList.size();
        }

        public void filterList(ArrayList<DoctorData> filter) {
            notifyDataSetChanged();
        }


        public class CViewholder extends RecyclerView.ViewHolder {

            TextView vName , vPhone ,text,price;
            ImageView photo;
            FrameLayout frameLayout;


            public CViewholder(View itemView) {
                super(itemView);
                vName = itemView.findViewById(R.id.tvName);
                vPhone = itemView.findViewById(R.id.tvPhon);
                photo = itemView.findViewById(R.id.cusImage);
                price = itemView.findViewById(R.id.price);
                text=itemView.findViewById(R.id.text);
                frameLayout= itemView.findViewById(R.id.temp_framlayout);
                photo.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }
            @SuppressLint("ResourceAsColor")
            public  void img(String r ,String name){
                if(r.equals("")){
                    Random random = new Random();
                    int red = random.nextInt(255 -0 +1)+0;
                    int blue = random.nextInt(255 -0 +1)+0;
                    int green = random.nextInt(255 -0 +1)+0;

                    name = name.toUpperCase();
                    photo.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(name.charAt(0)+"");
                    frameLayout.setBackgroundColor(Color.rgb(red,green,blue));
                } else {
                    photo.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                    Picasso.with(getActivity())
                            .load(r)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(photo);
                }
            }

        }
    }

    public void getAllDoctor(String dep_id,String price,String query) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURL + "General/get_all_doctors?depart_id=" + dep_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response :", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("doctors");
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                doctorsList.add(new DoctorData(
                                        p.getString("u_id_doc"),
                                        p.getString("about"),
                                        p.getString("depart_id"),
                                        p.getString("address"),
                                        p.getString("cons_price"),
                                        p.getString("user_name"),
                                        p.getString("email"),
                                        p.getString("place"),
                                        p.getString("period"),
                                        p.getString("qualification"),
                                        p.getString("img_url"),
                                        p.getString("depart_name"),
                                        p.getString("depart_en_name"),
                                        p.getString("is_active")));
                                i++;
                            }
                            MyProgressDialog.hide();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.getMessage().toString());
                Toast.makeText(getActivity(), R.string.internetError, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        Toast.makeText(getActivity(), "Size: "+doctorsList.size(), Toast.LENGTH_SHORT).show();

    }



    public void buildQuery() {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            URL += "?depart_id=" + SaveSetting.getData(getActivity(),SaveSetting.DEPART_ID,SaveSetting.DOC_PREFERENCES);
            URL += "&prise=" + SaveSetting.getData(getActivity(),SaveSetting.PRICE,SaveSetting.DOC_PREFERENCES);
            URL += "?query=" + SaveSetting.getData(getActivity(),SaveSetting.QUERY,SaveSetting.DOC_PREFERENCES);

        }


    }
}


