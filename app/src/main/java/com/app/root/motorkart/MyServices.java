package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.app.root.motorkart.adapter.ServiceAdapter;
import com.app.root.motorkart.modelclass.ServicesModel;
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

public class MyServices extends AppCompatActivity {
    RecyclerView rv_services;
ImageView iv_services;
ProgressBar pb_services;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid;
    BottomNavigationView btnav_view7;
    ServiceAdapter serviceAdapter;
    LinearLayout lll;
    ImageView iv_nostore4;
    int mMenuId;
    List<ServicesModel> servicesModelList=new ArrayList<>();
    String myservices="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getServices";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);
        sp_login=getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");
       // Toast.makeText(this, ""+cid, Toast.LENGTH_SHORT).show();

        rv_services=findViewById(R.id.rv_services);
        iv_services=findViewById(R.id.iv_services);
        pb_services=findViewById(R.id.pb_services);
        lll=findViewById(R.id.lll1);
        btnav_view7=findViewById(R.id.btnav_view7);
        iv_nostore4=findViewById(R.id.iv_nostore4);

        /*try{
            mMenuId = Integer.parseInt(getIntent().getStringExtra("nav_item"));
            MenuItem menuItem = btnav_view7.getMenu().getItem(2);
            boolean isChecked = menuItem.getItemId() == mMenuId;
            menuItem.setChecked(isChecked);
        }
        catch (Exception e){

        }*/


        getServices();

        iv_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getServices() {
        servicesModelList.clear();
        pb_services.setVisibility(View.VISIBLE);
        RequestQueue rq= Volley.newRequestQueue(MyServices.this);
        StringRequest sr=new StringRequest(Request.Method.POST, myservices, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if(status.equals("success")){
                        JSONArray jsonArray=jsonObject.getJSONArray("result");
                        for(int x=0;x<jsonArray.length();x++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(x);
                            ServicesModel servicesModel=new ServicesModel(jsonObject1.getString("id"),
                                    jsonObject1.getString("cust_id"),jsonObject1.getString("vid"),
                                    jsonObject1.getString("storeId"),jsonObject1.getString("message"),
                                    jsonObject1.getString("status"),jsonObject1.getString("isDeleted"),
                                    jsonObject1.getString("created_on"),jsonObject1.getString("registration_num"),
                                    jsonObject1.getString("mainCategId"));
                            servicesModelList.add(servicesModel);

                        }
                        rv_services.setLayoutManager(new LinearLayoutManager(MyServices.this,RecyclerView.VERTICAL,false));
                        serviceAdapter=new ServiceAdapter(MyServices.this,servicesModelList);
                        rv_services.setAdapter(serviceAdapter);
                        pb_services.setVisibility(View.GONE);


                    }
                    else {
                        //Toast.makeText(MyVehicles.this, "No vehicles", Toast.LENGTH_SHORT).show();
                       // Snackbar.make(lll,"No services",Snackbar.LENGTH_LONG).show();
                        iv_nostore4.setVisibility(View.VISIBLE);
                        rv_services.setVisibility(View.GONE);
                        pb_services.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_services.setVisibility(View.GONE);

                 //   Snackbar.make(lll,"1:"+e.toString(),Snackbar.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_services.setVisibility(View.GONE);

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

        btnav_view7.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
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
