package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.BrandAdapter;
import com.app.root.motorkart.adapter.FuelAdapter;
import com.app.root.motorkart.adapter.ModelAdapter;
import com.app.root.motorkart.interfaces.SElectVehicle;
import com.app.root.motorkart.modelclass.BrandModel;
import com.app.root.motorkart.modelclass.FuelModel;
import com.app.root.motorkart.modelclass.Modelmodel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectVehicleBrand extends AppCompatActivity implements SElectVehicle {
RecyclerView rv_brand,rv_model,rv_fuel;
String vehicle;
ImageView iv_brand;
TextView txt_vehicle,tl_vhicle;
LinearLayout ll_brand,ll_model,ll_fuel;
BrandAdapter brandAdapter;
ProgressBar pb_brands;
TextView txt_fuel,txt_brand,txt_model;
List<BrandModel> brandModelList=new ArrayList<>();
List<Modelmodel> modelList=new ArrayList<>();
FuelAdapter fuelAdapter;
ModelAdapter modelAdapter;
Button btn_reg_vehicle;
BottomNavigationView btnav_view02;
String brandid,fuelid,modelid,mainCategId,brandname,modelname,fuelname;
List<FuelModel> fuelModelsList=new ArrayList<>();
String brand_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/rootCategory";
String fuel_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/Fuel";
String model_url="http://demo.digitalsolutionsplanet.com/motorkart/api/register_android/VehicleModel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle_brand);

        vehicle=getIntent().getStringExtra("vehiclee");
        mainCategId=getIntent().getStringExtra("vehicleeid");

        Log.e("msg", "SelectVehicleBranddd: "+vehicle+","+mainCategId );

        rv_brand=findViewById(R.id.rv_brand);
        tl_vhicle=findViewById(R.id.tl_vhicle);
        txt_vehicle=findViewById(R.id.txt_vehicle);
        rv_model=findViewById(R.id.rv_model);
        rv_fuel=findViewById(R.id.rv_fuel);
        ll_brand=findViewById(R.id.ll_brand);
        ll_model=findViewById(R.id.ll_model);
        ll_fuel=findViewById(R.id.ll_fuel);
        txt_fuel=findViewById(R.id.txt_fuel);
        txt_brand=findViewById(R.id.txt_brand);
        txt_model=findViewById(R.id.txt_model);
        pb_brands=findViewById(R.id.pb_brands);
        iv_brand=findViewById(R.id.iv_brand);
        btn_reg_vehicle=findViewById(R.id.btn_reg_vehicle);
        btnav_view02=findViewById(R.id.btnav_view02);

        ll_model.setEnabled(false);
        ll_fuel.setEnabled(false);

        getBrands();

        ll_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_reg_vehicle.setVisibility(View.GONE);
                getBrands();
            }
        });

        ll_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnav_view02.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }



    private void getBrands() {
        pb_brands.setVisibility(View.VISIBLE);
        modelList.clear();
        fuelModelsList.clear();
        brandModelList.clear();
        txt_vehicle.setText("SELECT YOUR CAR BRAND");
        tl_vhicle.setText("Select Brand");
        ll_brand.setBackgroundColor(Color.BLACK);
        ll_model.setBackgroundColor(getResources().getColor(R.color.grey));
        ll_fuel.setBackgroundColor(getResources().getColor(R.color.grey));

        txt_brand.setTextColor(Color.WHITE);
        txt_model.setTextColor(Color.BLACK);
        txt_fuel.setTextColor(Color.BLACK);

        rv_brand.setVisibility(View.VISIBLE);

        rv_fuel.setVisibility(View.GONE);
        rv_model.setVisibility(View.GONE);
        RequestQueue rq= Volley.newRequestQueue(SelectVehicleBrand.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, brand_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);

                        BrandModel brandModel=new BrandModel(jsonObject1.getString("brand_name"),
                                jsonObject1.getString("image"),jsonObject1.getString("brand_id"));
                        brandModelList.add(brandModel);

                    }

                    rv_brand.setLayoutManager(new GridLayoutManager(SelectVehicleBrand.this,2));
                    brandAdapter=new BrandAdapter(SelectVehicleBrand.this,brandModelList,SelectVehicleBrand.this);
                    rv_brand.setAdapter(brandAdapter);
                    pb_brands.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_brands.setVisibility(View.GONE);
                   // Toast.makeText(SelectVehicleBrand.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectVehicleBrand.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                pb_brands.setVisibility(View.GONE);


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("select",vehicle);
                return map;
            }
        };
        rq.add(srq);
    }

    @Override
    public void selectvehiclebrand(int position, final String brand_id,String brand_name) {
        brandid=brand_id;
        brandname=brand_name;
        ll_model.setEnabled(true);

        pb_brands.setVisibility(View.VISIBLE);

        txt_vehicle.setText("SELECT YOUR CAR MODEL");
        tl_vhicle.setText("Select Model");

        modelList.clear();
        fuelModelsList.clear();
        brandModelList.clear();

        rv_model.setVisibility(View.VISIBLE);
        rv_fuel.setVisibility(View.GONE);
        rv_brand.setVisibility(View.GONE);

        ll_model.setBackgroundColor(Color.BLACK);
        ll_fuel.setBackgroundColor(getResources().getColor(R.color.grey));
        ll_brand.setBackgroundColor(getResources().getColor(R.color.grey));

        txt_model.setTextColor(Color.WHITE);
        txt_fuel.setTextColor(Color.BLACK);
        txt_brand.setTextColor(Color.BLACK);

        RequestQueue rq= Volley.newRequestQueue(SelectVehicleBrand.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, model_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);

                        Modelmodel modelmodel=new Modelmodel(jsonObject1.getString("brand_name"),
                                jsonObject1.getString("brand_id"), jsonObject1.getString("image"));
                        modelList.add(modelmodel);

                    }

                    rv_model.setLayoutManager(new GridLayoutManager(SelectVehicleBrand.this,2));
                    modelAdapter=new ModelAdapter(SelectVehicleBrand.this,modelList,SelectVehicleBrand.this);
                    rv_model.setAdapter(modelAdapter);
                    pb_brands.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_brands.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectVehicleBrand.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                pb_brands.setVisibility(View.GONE);


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("brand_id",brandid);
                return map;
            }
        };
        rq.add(srq);

    }

    @Override
    public void selectvehiclemodel(int position,String model_id,String model_name) {
        modelid=model_id;
        modelname=model_name;
        ll_fuel.setEnabled(true);

        pb_brands.setVisibility(View.VISIBLE);

        txt_vehicle.setText("SELECT YOUR CAR FUEL TYPE");
        tl_vhicle.setText("Select Fuel");


        modelList.clear();
        fuelModelsList.clear();
        brandModelList.clear();
        rv_fuel.setVisibility(View.VISIBLE);

        rv_brand.setVisibility(View.GONE);
        rv_model.setVisibility(View.GONE);

        ll_fuel.setBackgroundColor(Color.BLACK);
        ll_model.setBackgroundColor(getResources().getColor(R.color.grey));
        ll_brand.setBackgroundColor(getResources().getColor(R.color.grey));

        txt_fuel.setTextColor(Color.WHITE);
        txt_model.setTextColor(Color.BLACK);
        txt_brand.setTextColor(Color.BLACK);

        RequestQueue rq= Volley.newRequestQueue(SelectVehicleBrand.this);
        StringRequest srq=new StringRequest(StringRequest.Method.GET, fuel_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("fuel_type");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);

                        FuelModel fuelModel=new FuelModel(jsonObject1.getString("type"),
                                jsonObject1.getString("id"));
                        fuelModelsList.add(fuelModel);

                    }
                    rv_fuel.setLayoutManager(new GridLayoutManager(SelectVehicleBrand.this,2));
                    fuelAdapter=new FuelAdapter(SelectVehicleBrand.this,fuelModelsList,SelectVehicleBrand.this);
                    rv_fuel.setAdapter(fuelAdapter);
                    pb_brands.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_brands.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectVehicleBrand.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pb_brands.setVisibility(View.GONE);



            }
        });
        rq.add(srq);
    }

    @Override
    public void selectvehiclefuel(int position,String fuel_id,String fuel_name) {
        fuelid=fuel_id;
fuelname=fuel_name;
        Intent i=new Intent(SelectVehicleBrand.this,VehicleRegistration.class);
        i.putExtra("brand_Id",brandid);
        i.putExtra("mainCat",mainCategId);
        i.putExtra("model_Id",modelid);
        i.putExtra("fuel_type",fuelid);
        i.putExtra("brandName",brandname);
        i.putExtra("modelName",modelname);
        i.putExtra("fuelName",fuelname);
        i.putExtra("vehicleType",vehicle);
        startActivity(i);
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
