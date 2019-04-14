package medpatient.ye.ClassData;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import medpatient.ye.MyProgressDialog;
import medpatient.ye.R;
import medpatient.ye.appsettings.SaveSetting;

public class LoadData {

    ArrayList<DoctorData> doctorData = new ArrayList<>();
    ArrayList<DepartmentData> departmentData = new ArrayList<>();

    Map<String, String> map = new HashMap<String, String>();
    Context context;

    public LoadData(Context context) {
        this.context = context;
    }

    public ArrayList<DoctorData> getAllDoctor(String dep_id,String price,String query) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURL + "General/get_all_doctors?depart_id=" + dep_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response :", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("doctors");
                            int i = 0;
                            doctorData = new ArrayList<>();
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                doctorData.add(new DoctorData(
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
                            Toast.makeText(context, R.string.noData, Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.getMessage().toString());
                Toast.makeText(context, R.string.internetError, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        Toast.makeText(context, "Size: "+doctorData.size(), Toast.LENGTH_SHORT).show();

        return doctorData;
    }

    public ArrayList<DepartmentData> getAllDepartment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURL + "General",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response :", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("departments");
                            int i = 0;

                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);

                                departmentData.add(new DepartmentData(
                                        p.getString("depart_id"),
                                        p.getString("depart_name"),
                                        p.getString("depart_en_name"),
                                        p.getString("is_active"),
                                        p.getString("img_url")
                                ));

                                i++;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, R.string.errorLogin, Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.getMessage().toString());
                Toast.makeText(context, R.string.internetError, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        Toast.makeText(context, "Size: "+departmentData.size(), Toast.LENGTH_SHORT).show();
        return departmentData;
    }

}
