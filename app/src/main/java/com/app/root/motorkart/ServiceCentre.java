package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.app.root.motorkart.adapter.ServiceCenterAdapter;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.app.root.motorkart.modelclass.StoreModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCentre extends AppCompatActivity implements SaveBookmark {
ImageView iv_service_cntr;
    SharedPreferences sp_login;
    String user_location;
    SharedPreferences.Editor ed_login;
RecyclerView rv_service_center;
String vid_as_tid;
String tl_name,cid;
ProgressBar pb_servicecntr;
LinearLayout ll2;
ImageView iv_nostore2;
TextView tool_svc;
BottomNavigationView btnav_view4;
EditText ed_search_store3;
ServiceCenterAdapter serviceCenterAdapter;
List<StoreDetailModel> storeModelList=new ArrayList<>();
    String servicecentrloc_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreByLocation";
    String savebookmark_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/saveStoresAsBookmark";

String servicecenter_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreById";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_centre);

        sp_login=getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login=sp_login.edit();
        user_location=sp_login.getString("user_location","");
        cid = sp_login.getString("cid", "");

        vid_as_tid=getIntent().getStringExtra("tid");
        tl_name=getIntent().getStringExtra("name");
        iv_service_cntr=findViewById(R.id.iv_service_cntr);
        rv_service_center=findViewById(R.id.rv_service_center);
        tool_svc=findViewById(R.id.tool_svc);
        btnav_view4=findViewById(R.id.btnav_view4);
        iv_nostore2=findViewById(R.id.iv_nostore2);
        ll2=findViewById(R.id.ll2);
        pb_servicecntr=findViewById(R.id.pb_servicecntr);
        ed_search_store3=findViewById(R.id.ed_search_store3);

        tool_svc.setText(tl_name);

        ed_search_store3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serviceCenterAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Toast.makeText(this, ""+vid_as_tid, Toast.LENGTH_SHORT).show();

        pb_servicecntr.setVisibility(View.VISIBLE);
        RequestQueue rq= Volley.newRequestQueue(ServiceCentre.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, servicecentrloc_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);

                        StoreDetailModel storeDetailModel=new StoreDetailModel(jsonObject1.getString("sid"),
                                jsonObject1.getString("cid"),jsonObject1.getString("name"),
                                jsonObject1.getString("email"),jsonObject1.getString("phone"),
                                jsonObject1.getString("store_num"),
                                jsonObject1.getString("instt_charge"),jsonObject1.getString("discount"),
                                jsonObject1.getString("ratings"),jsonObject1.getString("address"),
                                jsonObject1.getString("state_id"),jsonObject1.getString("city_id"),
                                jsonObject1.getString("area_id"),jsonObject1.getString("location"),
                                jsonObject1.getString("timings"),jsonObject1.getString("isDeleted"),
                                jsonObject1.getString("status"));
                        storeModelList.add(storeDetailModel);

                    }

                    rv_service_center.setLayoutManager(new LinearLayoutManager(ServiceCentre.this,RecyclerView.VERTICAL,false));
                    serviceCenterAdapter=new ServiceCenterAdapter(ServiceCentre.this,storeModelList,
                            tl_name,vid_as_tid,ServiceCentre.this);
                    rv_service_center.setAdapter(serviceCenterAdapter);
                    pb_servicecntr.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    iv_nostore2.setVisibility(View.VISIBLE);
                    rv_service_center.setVisibility(View.GONE);
                    ll2.setBackgroundColor(Color.WHITE);
                    pb_servicecntr.setVisibility(View.GONE);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceCentre.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pb_servicecntr.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("vid",vid_as_tid);
                map.put("location",user_location);

                return map;
            }
        };
        rq.add(srq);

        btnav_view4.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        iv_service_cntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    @Override
    public void addbookmark(final String status, final String sid) {
        RequestQueue rq= Volley.newRequestQueue(ServiceCentre.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, savebookmark_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("1")){
                        Toast.makeText(ServiceCentre.this, "Bookmarked successfully", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceCentre.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("cid",cid);
                map.put("sid",sid);
                map.put("status",status);

                return map;
            }
        };
        rq.add(srq);
    }
}
