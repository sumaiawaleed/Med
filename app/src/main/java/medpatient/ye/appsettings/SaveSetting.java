package medpatient.ye.appsettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import medpatient.ye.DoctorActivity;
import medpatient.ye.LoginActivity;

public class SaveSetting {
    Context context;


    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs3";
    public static String USERID = "0";
    public static String USERTOKEN = "0";
    public static String USERTYPE = "0";
    public static String USERNAME = "0";
    public static String ServerURL = "http://www.sooqazal.com/medcare/";
    public static String KEY = "acb8f67925ab60e2dff0ab535a56cfe8a26f565e";

    /* Doctor Filter */
    public static String DOC_PREFERENCES = "doctor_preference";
    public static String QUERY = "query";
    public static String DEPART_ID = "depart_id";
    public static String PRICE = "price";

    public SaveSetting(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void SaveData() {
        try {
            SharedPreferences.Editor editorUSERID = sharedPreferences.edit();
            editorUSERID.putString("USERID", String.valueOf(USERID));
            editorUSERID.commit();

            SharedPreferences.Editor editorUSERNAME = sharedPreferences.edit();
            editorUSERNAME.putString("USERNAME", String.valueOf(USERNAME));
            editorUSERNAME.commit();

            SharedPreferences.Editor editorUSERTOKEN = sharedPreferences.edit();
            editorUSERTOKEN.putString("USERTOKEN", String.valueOf(USERTOKEN));
            editorUSERTOKEN.commit();

            SharedPreferences.Editor editorUSERTYPE = sharedPreferences.edit();
            editorUSERTYPE.putString("USERTYPE", String.valueOf(USERTYPE));
            editorUSERTYPE.commit();

            LoadData();
        } catch (Exception e) {
        }
    }

    public void LoadData() {
        String TempUserID = sharedPreferences.getString("USERID", "empty");
        String TempUserToken = sharedPreferences.getString("USERTOKEN", "empty");
        String TempUserName = sharedPreferences.getString("USERNAME", "empty");
        String TempUSERTYPE = sharedPreferences.getString("USERTYPE", "empty");

        if (!TempUserID.equals("empty")) {
            USERID = TempUserID;
            USERTOKEN = TempUserToken;
            USERNAME = TempUserName;
            USERTYPE = TempUSERTYPE;

            if (!USERID.equals("0")) {
                Intent intent = new Intent(context, DoctorActivity.class);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }


    public static String getData(Context context,String key,String prefName){
        SharedPreferences  sd = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        return sd.getString(key,"");

    }

    public static void setData(Context context,String key,String value,String prefName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DOC_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
        editor.putString(key, value);
        editor.commit();
    }

}
