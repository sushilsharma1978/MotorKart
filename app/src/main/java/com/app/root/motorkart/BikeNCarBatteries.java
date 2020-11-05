package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.root.motorkart.adapter.EngineAdapter;

public class BikeNCarBatteries extends AppCompatActivity {
ImageView iv_batteries;
RecyclerView rv_batteries;

    EngineAdapter engineAdapter;
    Integer[] batterieslist={R.drawable.carbatteries_one,R.drawable.carbatteries_two,
            R.drawable.carbatteries_three,R.drawable.car_battery_four};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_ncar_batteries);

        iv_batteries=findViewById(R.id.iv_batteries);
        rv_batteries=findViewById(R.id.rv_batteries);

        engineAdapter=new EngineAdapter(BikeNCarBatteries.this,batterieslist);
        rv_batteries.setLayoutManager(new GridLayoutManager(BikeNCarBatteries.this,2));
        rv_batteries.setAdapter(engineAdapter);

        iv_batteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
