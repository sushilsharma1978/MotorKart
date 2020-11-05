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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.app.root.motorkart.adapter.WashingPointsAdapter;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.app.root.motorkart.modelclass.WashingPointModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WashingPoints extends AppCompatActivity implements SaveBookmark {
ImageView iv_washing_pts;
LinearLayout ll3;
ProgressBar pb_washngpts;
    SharedPreferences sp_login;
    String user_location,cid;
    ImageView iv_nostore3;
    SharedPreferences.Editor ed_login;
RecyclerView rv_washingpts;
WashingPointsAdapter washingPointsAdapter;
String vid_as_tid,tl_name;
EditText ed_search_store2;
BottomNavigationView btnav_view5;
    String savebookmark_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/saveStoresAsBookmark";

List<WashingPointModel> washingptslist=new ArrayList<>();
    String washingptsbyId_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreById";
    String washingptloc_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreByLocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washing_points);
        iv_washing_pts=findViewById(R.id.iv_washing_pts);
        rv_washingpts=findViewById(R.id.rv_washingpts);
        btnav_view5=findViewById(R.id.btnav_view5);
        iv_nostore3=findViewById(R.id.iv_nostore3);
        pb_washngpts=findViewById(R.id.pb_washngpts);
        ed_search_store2=findViewById(R.id.ed_search_store2);
        ll3=findViewById(R.id.ll3);
        sp_login=getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login=sp_login.edit();
        user_location=sp_login.getString("user_location","");
        cid = sp_login.getString("cid", "");


        vid_as_tid=getIntent().getStringExtra("tid");
        tl_name=getIntent().getStringExtra("name");


        iv_washing_pts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        pb_washngpts.setVisibility(View.VISIBLE);
        RequestQueue rq= Volley.newRequestQueue(WashingPoints.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, washingptloc_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);

                        WashingPointModel storeDetailModel=new WashingPointModel(jsonObject1.getString("sid"),
                                jsonObject1.getString("cid"),jsonObject1.getString("name"),
                                jsonObject1.getString("email"),jsonObject1.getString("phone"),
                                jsonObject1.getString("store_num"),
                                jsonObject1.getString("instt_charge"),jsonObject1.getString("discount"),
                                jsonObject1.getString("ratings"),jsonObject1.getString("address"),
                                jsonObject1.getString("state_id"),jsonObject1.getString("city_id"),
                                jsonObject1.getString("area_id"),jsonObject1.getString("location"),
                                jsonObject1.getString("timings"),jsonObject1.getString("isDeleted"),
                                jsonObject1.getString("status"),jsonObject1.getString("created_on"));
                        washingptslist.add(storeDetailModel);

                    }

                    rv_washingpts.setLayoutManager(new LinearLayoutManager(WashingPoints.this, RecyclerView.VERTICAL,false));
                    washingPointsAdapter=new WashingPointsAdapter(WashingPoints.this,
                            washingptslist,vid_as_tid,tl_name,WashingPoints.this);
                    rv_washingpts.setAdapter(washingPointsAdapter);
                    pb_washngpts.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    iv_nostore3.setVisibility(View.VISIBLE);
                    rv_washingpts.setVisibility(View.GONE);
                    ll3.setBackgroundColor(Color.WHITE);
                    pb_washngpts.setVisibility(View.GONE);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WashingPoints.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pb_washngpts.setVisibility(View.GONE);

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

        ed_search_store2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                washingPointsAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnav_view5.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
        RequestQueue rq= Volley.newRequestQueue(WashingPoints.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, savebookmark_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("1")){
                        Toast.makeText(WashingPoints.this, "Bookmarked successfully", Toast.LENGTH_SHORT).show();
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
