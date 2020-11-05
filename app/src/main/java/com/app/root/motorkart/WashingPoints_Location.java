package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class WashingPoints_Location extends AppCompatActivity {
RecyclerView rv_washing_loc;
ImageView iv_washing_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washing_points__location);

        rv_washing_loc=findViewById(R.id.rv_washing_loc);
        iv_washing_back=findViewById(R.id.iv_washing_back);

        iv_washing_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
