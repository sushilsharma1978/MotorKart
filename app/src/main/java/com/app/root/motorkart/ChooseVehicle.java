package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.BrandAdapter;
import com.app.root.motorkart.adapter.ChooseVehicleAdapter;
import com.app.root.motorkart.modelclass.BrandModel;
import com.app.root.motorkart.modelclass.ChooseVehiclemodel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseVehicle extends AppCompatActivity {
ImageView iv_vehicle;
RecyclerView rv_choosevehicles;
ProgressBar pb_ch_vehicle;
BottomNavigationView btnav_view01;
List<ChooseVehiclemodel> chooseVehiclemodelList=new ArrayList<>();
ChooseVehicleAdapter chooseVehicleAdapter;
    String selectvehicle_url="http://demo.digitalsolutionsplanet.com/motorkart/api/register_android/getCategory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vehicle);

        iv_vehicle=findViewById(R.id.iv_vehicle);
        pb_ch_vehicle=findViewById(R.id.pb_ch_vehicle);
        rv_choosevehicles=findViewById(R.id.rv_choosevehicles);
        btnav_view01=findViewById(R.id.btnav_view01);


        iv_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getCategories();

    }

    private void getCategories() {
        pb_ch_vehicle.setVisibility(View.VISIBLE);
        RequestQueue rq= Volley.newRequestQueue(ChooseVehicle.this);
        StringRequest srq=new StringRequest(StringRequest.Method.GET, selectvehicle_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);

                        ChooseVehiclemodel chooseVehiclemodel=new ChooseVehiclemodel(jsonObject1.getString("category_id"),
                                jsonObject1.getString("name"),jsonObject1.getString("image"));
                        chooseVehiclemodelList.add(chooseVehiclemodel);

                    }

                    rv_choosevehicles.setLayoutManager(new LinearLayoutManager(ChooseVehicle.this,RecyclerView.VERTICAL,false));
                    chooseVehicleAdapter=new ChooseVehicleAdapter(ChooseVehicle.this,chooseVehiclemodelList);
                    rv_choosevehicles.setAdapter(chooseVehicleAdapter);
                    pb_ch_vehicle.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_ch_vehicle.setVisibility(View.GONE);
                    Toast.makeText(ChooseVehicle.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChooseVehicle.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                pb_ch_vehicle.setVisibility(View.GONE);


            }
        });
        rq.add(srq);

        btnav_view01.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
