package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.ServiceCenterAdapter;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Enquiry extends AppCompatActivity {
ImageView iv_inquire_back;
EditText ed_subject,ed_query;
Button btn_inquire;
String vid,cid;
BottomNavigationView btnav_view2;
ProgressBar pb_enquire;
Spinner spinner_ct_veh2;
Spinner spinner_category1,spinner_subcategory2;
    String selected_vehicleid;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;

    String categoryid;
    List<String> categorieslist=new ArrayList<>();
    List<String> subcategorieslist=new ArrayList<>();
    List<String> categorieslistid=new ArrayList<>();
    List<String> subcategorieslistid=new ArrayList<>();
    List<String> vehiclelist=new ArrayList<>();
    List<String> vehiclelistid=new ArrayList<>();
    String inquiryname,user_location,subcatname,subcatid;
    String servicecenters_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreByLocation";

    String inquire_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/stoteServiceEnquiry";
    String myvehicles="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getVechiles";
    String storeurl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getCateg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        iv_inquire_back=findViewById(R.id.iv_inquire_back);
        ed_subject=findViewById(R.id.ed_subject);
        ed_query=findViewById(R.id.ed_query);
        btn_inquire=findViewById(R.id.btn_inquire);
        pb_enquire=findViewById(R.id.pb_enquire);
        btnav_view2=findViewById(R.id.btnav_view2);
        spinner_ct_veh2=findViewById(R.id.spinner_ct_veh2);
        spinner_category1=findViewById(R.id.spinner_category2);
        spinner_subcategory2=findViewById(R.id.spinner_subcategory2);

        vid=getIntent().getStringExtra("vid");
        inquiryname=getIntent().getStringExtra("serviceinquiry_name");
        subcatname=getIntent().getStringExtra("subcatname");
        subcatid=getIntent().getStringExtra("subcatnameid");

        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");
        user_location=sp_login.getString("user_location","");

        iv_inquire_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getVehicles();
        getCategoriesList();



        spinner_ct_veh2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_vehicleid = vehiclelistid.get(position);
                Log.e("msg", "onCreatecid11: "+selected_vehicleid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_category1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryid = categorieslistid.get(position);
                Log.e("msg", "onCreatecid111: "+categoryid);
                getSubCategoriesList(categoryid);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_subcategory2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcatid = subcategorieslistid.get(position);
                Log.e("msg", "onCreatecid111: "+subcatid);
               // Toast.makeText(Enquiry.this, "1:"+subcatid, Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(ed_query.getText().toString().isEmpty() &&
                ed_subject.getText().toString().isEmpty())
                {
                    ed_query.setError("Enter query");
                    ed_subject.setError("Enter subject");
                }

                else if(ed_query.getText().toString().isEmpty()){
                    ed_query.setError("Enter query");

                }
                else if( ed_subject.getText().toString().isEmpty()){
                    ed_subject.setError("Enter subject");

                }
                else {
                    btn_inquire.setEnabled(false);
                    pb_enquire.setVisibility(View.VISIBLE);
                    RequestQueue rq= Volley.newRequestQueue(Enquiry.this);
                    StringRequest srq=new StringRequest(StringRequest.Method.POST, inquire_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                String enquiry_id=jsonObject.getString("id");
                                if(status.equals("success")){
                                    pb_enquire.setVisibility(View.GONE);
                                    //Toast.makeText(Enquiry.this, "", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(v,"Your query has been submitted",Snackbar.LENGTH_SHORT).show();
                                    ed_query.setText("");
                                    ed_subject.setText("");
                                    btn_inquire.setEnabled(true);
                                }
                                //Toast.makeText(Enquiry.this, ""+enquiry_id, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_enquire.setVisibility(View.GONE);

                                // Toast.makeText(PartStore_Location.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT).show();

                           // Toast.makeText(Enquiry.this, "", Toast.LENGTH_SHORT).show();
                            btn_inquire.setEnabled(true);
                            pb_enquire.setVisibility(View.GONE);


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("subcatid",subcatid);
                            map.put("vid",selected_vehicleid);
                            map.put("cid",categoryid);
                            map.put("subject",ed_subject.getText().toString());
                            map.put("msg",ed_query.getText().toString());
                            map.put("CID",cid);

                            return map;
                        }
                    };
                    rq.add(srq);
                }



            }
        });

        btnav_view2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void getVehicles() {
        vehiclelist.clear();
        vehiclelistid.clear();
        RequestQueue rq= Volley.newRequestQueue(Enquiry.this);
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

                            vehiclelist.add(jsonObject1.getString("registration_num"));
                            vehiclelistid.add( jsonObject1.getString("id"));

                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Enquiry.this,
                                android.R.layout.simple_spinner_item, vehiclelist);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ct_veh2.setAdapter(dataAdapter);



                    }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


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

    private void getCategoriesList() {
        categorieslist.clear();
        categorieslistid.clear();
        RequestQueue rq= Volley.newRequestQueue(Enquiry.this);
        StringRequest sr=new StringRequest(Request.Method.GET, storeurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("msg", "StoreResponse1: "+response.toString() );

                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);
                        categorieslist.add( jsonObject1.getString("name"));
                        categorieslistid.add( jsonObject1.getString("tid"));
                    }


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Enquiry.this,
                            android.R.layout.simple_spinner_item, categorieslist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_category1.setAdapter(dataAdapter);


                    try{
                        ArrayAdapter adapter = (ArrayAdapter) spinner_category1.getAdapter();
                        int spinnerPosition = adapter.getPosition(inquiryname);
                        Log.e("msg", "SERVICECENTERNAME12: "+spinnerPosition);
                        spinner_category1.setSelection(spinnerPosition);
                    }
                    catch (Exception e){
                        Log.e("msg", "SERVICECENTERNAME1: "+e.toString() );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(BookBikeService.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(BookBikeService.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();


                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(sr);
    }

    private void getSubCategoriesList(final String id) {
        subcategorieslist.clear();
        subcategorieslistid.clear();
        RequestQueue rq= Volley.newRequestQueue(Enquiry.this);
        StringRequest sr=new StringRequest(Request.Method.POST, servicecenters_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("msg", "StoreResponse1: "+response.toString() );

                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);
                        subcategorieslist.add( jsonObject1.getString("name"));
                        subcategorieslistid.add( jsonObject1.getString("sid"));
                    }


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Enquiry.this,
                            android.R.layout.simple_spinner_item, subcategorieslist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_subcategory2.setAdapter(dataAdapter);


                    try{
                        ArrayAdapter adapter = (ArrayAdapter) spinner_subcategory2.getAdapter();
                        int spinnerPosition = adapter.getPosition(subcatname);
                        //Toast.makeText(Enquiry.this, ""+subcatid, Toast.LENGTH_SHORT).show();
                        Log.e("msg", "SERVICECENTERNAME12: "+spinnerPosition);
                        spinner_subcategory2.setSelection(spinnerPosition);
                    }
                    catch (Exception e){
                        Toast.makeText(Enquiry.this, "e:"+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("msg", "SERVICECENTERNAME1: "+e.toString() );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(BookBikeService.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                 Toast.makeText(Enquiry.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();


                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("vid",id);
                map.put("location",user_location);

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
                    startActivity(intent5);
                    return true;

                case R.id.navigation_profile:
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    return true;
            }
            return false;
        }
    };
}
//"vid": "2",
//        "cid": "2",
//        "subject": "cbfdb",
//        "msg": "dfsgvsdbgvdfsb",
//        "CID": "2"