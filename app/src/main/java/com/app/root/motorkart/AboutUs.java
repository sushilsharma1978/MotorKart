package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutUs extends AppCompatActivity {
ImageView iv_about;
BottomNavigationView btnav_view05;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        iv_about=findViewById(R.id.iv_about);
        btnav_view05=findViewById(R.id.btnav_view05);

        iv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnav_view05.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(), HomeScreeeen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;

                case R.id.navigation_qr:
                    Intent intent5=new Intent(getApplicationContext(),QrcodeScanner.class);
                    intent5.putExtra("nav_item",String.valueOf(item.getItemId()));
                    startActivity(intent5);
                    return true;

                case R.id.navigation_profile:
                    Intent intent2=new Intent(getApplicationContext(),Profile.class);
                    intent2.putExtra("nav_item",String.valueOf(item.getItemId()));
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
}
