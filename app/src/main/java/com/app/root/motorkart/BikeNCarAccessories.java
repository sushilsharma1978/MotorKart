package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.root.motorkart.adapter.EngineAdapter;

public class BikeNCarAccessories extends AppCompatActivity {
RecyclerView rv_bike;
ImageView iv_bike;
    EngineAdapter engineAdapter;
    Integer[] accessorieslist={R.drawable.bike_one,R.drawable.bike_two,R.drawable.bike_three,R.drawable.bike_four};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_ncar_accessories);

        rv_bike=findViewById(R.id.rv_bike);
        iv_bike=findViewById(R.id.iv_bike);

        engineAdapter=new EngineAdapter(BikeNCarAccessories.this,accessorieslist);
        rv_bike.setLayoutManager(new GridLayoutManager(BikeNCarAccessories.this,2));
        rv_bike.setAdapter(engineAdapter);

        iv_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
