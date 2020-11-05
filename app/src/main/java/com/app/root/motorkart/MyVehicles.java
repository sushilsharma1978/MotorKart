package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.app.root.motorkart.adapter.VehicleAdapter;
import com.app.root.motorkart.interfaces.DeleteVehicle;
import com.app.root.motorkart.modelclass.VehicleModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyVehicles extends AppCompatActivity implements DeleteVehicle {
RecyclerView rv_vehicles;
VehicleAdapter vehicleAdapter;
ImageView iv_myvehicles;
LinearLayout lll;
List<VehicleModel> vehicleList=new ArrayList<>();
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid,checklogin;
    Button btnn_login_vehicle;
    ProgressBar pb_vehicles;
    LinearLayout iv_addvehicle,ll_vehicles;
    BottomNavigationView btnav_view003;
String myvehicles="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getVechiles";
String deletevehicle="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getDeleteVehicle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        rv_vehicles=findViewById(R.id.rv_vehicles);
        iv_myvehicles=findViewById(R.id.iv_myvehicles);
        pb_vehicles=findViewById(R.id.pb_vehicles);
        lll=findViewById(R.id.lll);
        iv_addvehicle=findViewById(R.id.iv_addvehicle);
        btnav_view003=findViewById(R.id.btnav_view003);
        ll_vehicles=findViewById(R.id.ll_vehicles);
        btnn_login_vehicle=findViewById(R.id.btnn_login_vehicle);

        getVehicles();

        iv_myvehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sp_login=getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");
        checklogin=sp_login.getString("save_login","");

        if(checklogin.equals("false")){
            btnn_login_vehicle.setVisibility(View.VISIBLE);
            ll_vehicles.setVisibility(View.GONE);
        }
        else {
            ll_vehicles.setVisibility(View.VISIBLE);
            btnn_login_vehicle.setVisibility(View.GONE);
        }

        iv_addvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyVehicles.this,ChooseVehicle.class));
            }
        });

        btnn_login_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyVehicles.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnav_view003.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void getVehicles() {
        vehicleList.clear();
        pb_vehicles.setVisibility(View.VISIBLE);
        RequestQueue rq= Volley.newRequestQueue(MyVehicles.this);
        StringRequest sr=new StringRequest(Request.Method.POST, myvehicles, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if(status.equals("success")){
                        JSONArray jsonArray=jsonObject.getJSONArray("result");
                        for(int x=0;x<jsonArray.length();x++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(x);
                            VehicleModel vehicleModel=new VehicleModel(jsonObject1.getString("id"),
                                    jsonObject1.getString("cid"),jsonObject1.getString("mainCategId"),
                                    jsonObject1.getString("brandId"),jsonObject1.getString("modelId"),
                                    jsonObject1.getString("fuelType"),jsonObject1.getString("registration_num"),
                                    jsonObject1.getString("name"),jsonObject1.getString("status"),
                                    jsonObject1.getString("isDeleted"),jsonObject1.getString("modified_on"),
                                    jsonObject1.getString("created_on"),jsonObject1.getString("model_name"),
                                    jsonObject1.getString("brand_name"));
                            vehicleList.add(vehicleModel);

                        }
                        rv_vehicles.setLayoutManager(new LinearLayoutManager(MyVehicles.this,RecyclerView.VERTICAL,false));
                        vehicleAdapter=new VehicleAdapter(MyVehicles.this,vehicleList,MyVehicles.this);
                        rv_vehicles.setAdapter(vehicleAdapter);
                        pb_vehicles.setVisibility(View.GONE);


                    }
                    else {
                        //Toast.makeText(MyVehicles.this, "No vehicles", Toast.LENGTH_SHORT).show();
                        Snackbar.make(lll,"No vehicles",Snackbar.LENGTH_LONG).show();

                        pb_vehicles.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_vehicles.setVisibility(View.GONE);

                    //Snackbar.make(lll,"1:"+e.toString(),Snackbar.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_vehicles.setVisibility(View.GONE);

                Snackbar.make(lll,"Check Internet Connection",Snackbar.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("CID",cid);
                return map;
            }
        };
        rq.add(sr);
    }

    @Override
    public void deletevehicle(final String id) {
        pb_vehicles.setVisibility(View.VISIBLE);

        RequestQueue rq= Volley.newRequestQueue(MyVehicles.this);
        StringRequest sr=new StringRequest(Request.Method.POST, deletevehicle, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("result");

                    if(status.equals("success")){
getVehicles();
                    }
                    else {
                        pb_vehicles.setVisibility(View.GONE);
                        Snackbar.make(lll,"Something went wrong..Please try again later..",Snackbar.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_vehicles.setVisibility(View.GONE);
                   // Snackbar.make(lll,"2:"+e.toString(),Snackbar.LENGTH_LONG).show();

                    // Toast.makeText(PartStore_Location.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_vehicles.setVisibility(View.GONE);

                Snackbar.make(lll,"Check Internet Connection",Snackbar.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("id",id);
                return map;
            }
        };
        rq.add(sr);


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
