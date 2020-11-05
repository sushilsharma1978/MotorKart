package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.root.motorkart.adapter.EngineAdapter;

public class EngineOil extends AppCompatActivity {
RecyclerView rv_engine;
ImageView iv_engine;
EngineAdapter engineAdapter;
Integer[] enginelist={R.drawable.engine_one,R.drawable.engine_two,R.drawable.engine_three,R.drawable.engine_four};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_oil);

        iv_engine=findViewById(R.id.iv_engine);
        rv_engine=findViewById(R.id.rv_engine);

        engineAdapter=new EngineAdapter(EngineOil.this,enginelist);
        rv_engine.setLayoutManager(new GridLayoutManager(EngineOil.this,2));
        rv_engine.setAdapter(engineAdapter);

        iv_engine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
