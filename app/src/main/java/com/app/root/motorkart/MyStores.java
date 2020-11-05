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
import android.view.MenuItem;
import android.view.View;
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
import com.app.root.motorkart.adapter.MyStoreDetailAdapter;
import com.app.root.motorkart.adapter.PartStoreDetailAdapter;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStores extends AppCompatActivity implements SaveBookmark {
    ImageView iv_store_back;
    LinearLayout ll1;
    ImageView iv_nostore1;
    SharedPreferences sp_login;
    String user_location,cid;
    ProgressBar pb_partstore;
    SharedPreferences.Editor ed_login;
    BottomNavigationView btnav_view1;
    RecyclerView rv_partstore;
    MyStoreDetailAdapter storeDetailAdapter;
    TextView tv_st_number, tv_st_inst, tv_st_dsct;
    List<StoreDetailModel> storeDetailModelList = new ArrayList<>();
    String getBookmarked_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getBookMarkedStores";
    String savebookmark_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/saveStoresAsBookmark";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stores);
        sp_login = getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login = sp_login.edit();
        user_location = sp_login.getString("user_location", "");
        cid = sp_login.getString("cid", "");




        iv_store_back = findViewById(R.id.iv_mystore_back);
        btnav_view1 = findViewById(R.id.btnav_view10);

        rv_partstore = findViewById(R.id.rv_mystore);
        iv_nostore1 = findViewById(R.id.iv_nostoree1);
        pb_partstore = findViewById(R.id.pb_mypartstore);
        ll1 = findViewById(R.id.ll20);

        iv_store_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pb_partstore.setVisibility(View.VISIBLE);

        getmyStores();


        btnav_view1.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void getmyStores() {
        storeDetailModelList.clear();
        RequestQueue rq = Volley.newRequestQueue(MyStores.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, getBookmarked_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                        StoreDetailModel storeDetailModel = new StoreDetailModel(jsonObject1.getString("sid"),
                                jsonObject1.getString("cid"), jsonObject1.getString("name"),
                                jsonObject1.getString("email"), jsonObject1.getString("phone"),
                                jsonObject1.getString("store_num"),
                                jsonObject1.getString("instt_charge"), jsonObject1.getString("discount"),
                                jsonObject1.getString("ratings"), jsonObject1.getString("address"),
                                jsonObject1.getString("state_id"), jsonObject1.getString("city_id"),
                                jsonObject1.getString("area_id"), jsonObject1.getString("location"),
                                jsonObject1.getString("timings"), jsonObject1.getString("isDeleted"),
                                jsonObject1.getString("status"));
                        storeDetailModelList.add(storeDetailModel);

                    }

                    rv_partstore.setLayoutManager(new LinearLayoutManager(MyStores.this, RecyclerView.VERTICAL, false));
                    storeDetailAdapter = new MyStoreDetailAdapter(MyStores.this,
                            storeDetailModelList,MyStores.this);
                    rv_partstore.setAdapter(storeDetailAdapter);
                    pb_partstore.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    iv_nostore1.setVisibility(View.VISIBLE);
                    rv_partstore.setVisibility(View.GONE);
                    ll1.setBackgroundColor(Color.WHITE);
                    pb_partstore.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyStores.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pb_partstore.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cid", cid);
                return map;
            }
        };
        rq.add(srq);

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
                    Intent intent5 = new Intent(getApplicationContext(), QrcodeScanner.class);
                    intent5.putExtra("nav_item", String.valueOf(item.getItemId()));
                    startActivity(intent5);
                    return true;


                case R.id.navigation_profile:
                    Intent intent2 = new Intent(getApplicationContext(), Profile.class);
                    intent2.putExtra("nav_item", String.valueOf(item.getItemId()));
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void addbookmark(final String status, final String sid) {
        RequestQueue rq= Volley.newRequestQueue(MyStores.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, savebookmark_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("0")){
                        Toast.makeText(MyStores.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                        getmyStores();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(WashingPoints.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();

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
