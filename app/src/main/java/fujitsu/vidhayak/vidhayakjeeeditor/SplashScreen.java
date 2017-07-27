package fujitsu.vidhayak.vidhayakjeeeditor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread splashthread = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startActivity(new Intent(SplashScreen.this, DashBoard.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                    finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        splashthread.start();

    }
    @Override
    public void onBackPressed() {

    }





    }


