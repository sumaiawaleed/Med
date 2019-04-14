package medpatient.ye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import medpatient.ye.appsettings.SaveSetting;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    SaveSetting sv = new SaveSetting(Welcome.this);
                    sv.LoadData();
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
