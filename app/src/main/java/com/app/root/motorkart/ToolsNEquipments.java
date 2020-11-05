package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.root.motorkart.adapter.EngineAdapter;

public class ToolsNEquipments extends AppCompatActivity {
ImageView iv_tools;
RecyclerView rv_tools;

    EngineAdapter engineAdapter;
    Integer[] toollist={R.drawable.tool_one,R.drawable.tool_two,R.drawable.tool_three,R.drawable.tool_four};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_nequipments);

        rv_tools=findViewById(R.id.rv_tools);
        iv_tools=findViewById(R.id.iv_tools);


        engineAdapter=new EngineAdapter(ToolsNEquipments.this,toollist);
        rv_tools.setLayoutManager(new GridLayoutManager(ToolsNEquipments.this,2));
        rv_tools.setAdapter(engineAdapter);


        iv_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    }
