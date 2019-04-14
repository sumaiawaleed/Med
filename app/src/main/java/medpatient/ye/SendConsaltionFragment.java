package medpatient.ye;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;



import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import medpatient.ye.ClassAdapter.AdapterDepartment;
import medpatient.ye.ClassAdapter.AdapterDoctor;
import medpatient.ye.ClassData.DepartmentData;
import medpatient.ye.ClassData.DoctorData;
import medpatient.ye.ClassData.LoadData;
import medpatient.ye.appsettings.SaveSetting;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendConsaltionFragment extends Fragment {

    LoadData loadData;
    ArrayList<DepartmentData> departmentData;
    ArrayList<DoctorData> doctorData;
    AlertDialog alertDialog;
    ArrayList<DepartmentData> relDep;
    ArrayList<DoctorData> relDoc;
    public static String dep_id, u_id_doc; // well save the id of selected dep and doc ..
    public static int PICKFILE_REQUEST_CODE = 1;
    Button selectedDep, selectedDoc, send_cons, load_files;

    RadioGroup visibility_radio, gender_radio, allergie_radio, medicine_radio, illness_radio;
    EditText weightED, birthdayED, cons_contentED, allergic_ex, medicine_ex, illness_ex;
    File[] cons_attach;

    public static Fragment newInstance() {
        return new SendConsaltionFragment();
    }

    public SendConsaltionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        departmentData = loadData.getAllDepartment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_consaltion, container, false);
        selectedDep = view.findViewById(R.id.btnDepartment);
        selectedDoc = view.findViewById(R.id.selectedDoc);
        selectedDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (departmentData.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.wait, Toast.LENGTH_LONG).show();
                } else {
                    showAlertDialog(departmentData);
                }
            }
        });

        selectedDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doctorData.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.wait, Toast.LENGTH_LONG).show();
                } else {
                    showAlertDialog_Doc(doctorData);
                }
            }
        });
        departmentData = new ArrayList<>();
        doctorData = new ArrayList<>();

        send_cons = view.findViewById(R.id.send_cons);

        send_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_consultation();
            }
        });

        visibility_radio = view.findViewById(R.id.visibility_radio);
        gender_radio = view.findViewById(R.id.gender_radio);
        allergie_radio = view.findViewById(R.id.allergie_radio);
        weightED = view.findViewById(R.id.weight);
        birthdayED = view.findViewById(R.id.birthday);
        cons_contentED = view.findViewById(R.id.cons_content);
        allergic_ex = view.findViewById(R.id.allergic_ex);

        load_files = view.findViewById(R.id.load_files);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        enable_load();


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enable_load();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    public void enable_load() {
        load_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withRequestCode(PICKFILE_REQUEST_CODE)
                        .start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK) {

            cons_attach[cons_attach.length] = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));


            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(extension);
    }

    private void send_consultation() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                File f;

                MultipartBody.Builder builder= new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

               builder
                        .addFormDataPart("user_id", SaveSetting.getData(getActivity(), SaveSetting.USERID,SaveSetting.MyPREFERENCES))
                        .addFormDataPart("key", SaveSetting.getData(getActivity(), SaveSetting.KEY,SaveSetting.MyPREFERENCES))
                        .addFormDataPart("u_id_doc", u_id_doc)
                        .addFormDataPart("is_public", visibility_radio.getCheckedRadioButtonId() == R.id.con_pubic ? "1" : "0")
                        .addFormDataPart("gender", gender_radio.getCheckedRadioButtonId() == R.id.male ? "1" : "2")
                        .addFormDataPart("weight", weightED.getText().toString())
                        .addFormDataPart("cons_content", birthdayED.getText().toString())
                        .addFormDataPart("birthday", birthdayED.getText().toString())
                        .addFormDataPart("medicine", medicine_radio.getCheckedRadioButtonId() == R.id.medicine_yes ? medicine_ex.getText().toString() : "")
                        .addFormDataPart("illness", illness_radio.getCheckedRadioButtonId() == R.id.illness_yes ? illness_ex.getText().toString() : "")
                        .addFormDataPart("allergies", allergie_radio.getCheckedRadioButtonId() == R.id.allergie_yes ? allergic_ex.getText().toString() : "");
                for (int i = 0; i < cons_attach.length ; i++){
                    f = cons_attach[i];

                    if(f.exists()){
                        String content_type = getMimeType(f.getPath());
                        final MediaType MEDIA_TYPE = MediaType.parse(content_type);
                        builder.addFormDataPart("cons_attach[]",f.getName(), RequestBody.create(MEDIA_TYPE,f));
                    }
                }

                RequestBody requestBody = builder.build();

                Request request = new Request.Builder()
                        .url("url......")
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient.Builder().build();

                Call call = client.newCall(request);


                call.enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {


                    }


                });

            }
        });

        t.start();

    }


    //---------------------------
    // this is for show Alert and Search for dep
    private void showAlertDialog(final ArrayList<DepartmentData> data) {
        relDep = data;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.ticket_dialog_search_dep, null);
        final EditText txtEdit = view1.findViewById(R.id.txtEdit);
        final Button searchBtn = view1.findViewById(R.id.searchBtn);
        final RecyclerView recyclerView = view1.findViewById(R.id.recyclerView);

        alertDialog.setTitle(R.string.SelectOne);
        alertDialog.setView(view1);
        setRecyclerView(recyclerView, relDep);// to setRecyclerView
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtEdit.getText().toString().isEmpty()) {
                    final ArrayList<DepartmentData> newDep = new ArrayList<>();
                    for (DepartmentData item : data) {
                        if (item.getDepNameAr().equals(txtEdit.getText().toString())
                                || item.getDepNameEn().equals(txtEdit.getText().toString())) {
                            newDep.add(item);
                        }
                    }

                    if (newDep.size() > 0) {
                        relDep = newDep;
                        setRecyclerView(recyclerView, newDep);
                    } else {
                        setRecyclerView(recyclerView, data);
                        Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_LONG).show();
                    }

                } else
                    Toast.makeText(getActivity(), R.string.noDataInsert, Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                doctorData.clear();
                MyProgressDialog.show(getActivity());
                doctorData = loadData.getAllDoctor(dep_id,"0","");
            }
        });

    }

    // too set RecyclerView for dep
    private void setRecyclerView(RecyclerView recyclerView, ArrayList data) {
        AdapterDepartment adapter = new AdapterDepartment(getActivity(), data, selectedDep, alertDialog);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }
//---------------------------

    //---------------------------
    // this is for show Alert and Search for doc
    private void showAlertDialog_Doc(final ArrayList<DoctorData> data) {
        relDoc = data;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.ticket_dialog_search_dep, null);
        final EditText txtEdit = view1.findViewById(R.id.txtEdit);
        txtEdit.setHint("Enter Name Or Price ..");
        final Button searchBtn = view1.findViewById(R.id.searchBtn);
        final RecyclerView recyclerView = view1.findViewById(R.id.recyclerView);

        alertDialog.setTitle(R.string.SelectOne);
        alertDialog.setView(view1);
        setRecyclerView_Doc(recyclerView, relDoc);// to setRecyclerView
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtEdit.getText().toString().isEmpty()) {
                    final ArrayList<DoctorData> newDoc = new ArrayList<>();
                    for (DoctorData item : data) {
                        if (item.getUser_name().equals(txtEdit.getText().toString())
                                || item.getCons_price().equals(txtEdit.getText().toString())) {
                            newDoc.add(item);
                        }
                    }

                    if (newDoc.size() > 0) {
                        relDoc = newDoc;
                        setRecyclerView_Doc(recyclerView, newDoc);
                    } else {
                        setRecyclerView_Doc(recyclerView, data);
                        Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_LONG).show();
                    }

                } else
                    Toast.makeText(getActivity(), R.string.noDataInsert, Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();

    }

    // too set RecyclerView for doc
    private void setRecyclerView_Doc(RecyclerView recyclerView, ArrayList data) {
        AdapterDoctor adapter = new AdapterDoctor(getActivity(), data, selectedDoc, alertDialog, u_id_doc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
//---------------------------







}
