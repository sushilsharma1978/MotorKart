package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash_screen);

        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cid.equals("")){
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashScreen.this, HomeScreeeen.class));
                    finish();
                }

            }
        },3000);
    }
}
