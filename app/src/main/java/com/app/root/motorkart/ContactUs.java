package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUs extends AppCompatActivity {
EditText ed_ct_name,ed_ct_email,ed_ct_mobile,ed_ct_query;
Button btn_contact;
ImageView iv_contact;
String contact_url="";
TextView tv_info_email;
Spinner spinner_category1;

    ImageView iv_inquire_back;
    EditText ed_subject1,ed_query1;
    Button btn_inquire1;
    String cid,selected_vehicleid;
    BottomNavigationView btnav_view21;
    ProgressBar pb_enquire1;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
String categoryid;
Spinner spinner_ct_veh1;
    List<String> categorieslist=new ArrayList<>();
    List<String> categorieslistid=new ArrayList<>();
    List<String> vehiclelist=new ArrayList<>();
    List<String> vehiclelistid=new ArrayList<>();
    String storeurl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getCateg";
    String inquire_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/stoteServiceEnquiry";
    String myvehicles="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getVechiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ed_ct_name=findViewById(R.id.ed_ct_name);
        ed_ct_email=findViewById(R.id.ed_ct_email);
        ed_ct_mobile=findViewById(R.id.ed_ct_mobile);
        ed_ct_query=findViewById(R.id.ed_ct_query);
        btn_contact=findViewById(R.id.btn_contact);
        iv_contact=findViewById(R.id.iv_contact);
        tv_info_email=findViewById(R.id.tv_info_email);

        ed_subject1=findViewById(R.id.ed_subject1);
        ed_query1=findViewById(R.id.ed_query1);
        btn_inquire1=findViewById(R.id.btn_inquire1);
        pb_enquire1=findViewById(R.id.pb_enquire1);
        btnav_view21=findViewById(R.id.btnav_view21);
        spinner_category1=findViewById(R.id.spinner_category1);
        spinner_ct_veh1=findViewById(R.id.spinner_ct_veh1);

        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");

        getCategoriesList();

        getVehicles();

        tv_info_email.setPaintFlags(tv_info_email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        iv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        spinner_category1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryid = categorieslistid.get(position);
                Log.e("msg", "onCreatecid211: "+categoryid);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_ct_veh1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_vehicleid = vehiclelistid.get(position);
                Log.e("msg", "onCreatecid2111: "+selected_vehicleid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_inquire1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if( ed_query1.getText().toString().isEmpty() &&
                        ed_subject1.getText().toString().isEmpty())
                {

                    ed_query1.setError("Enter query");
                    ed_subject1.setError("Enter subject");
                }

                else if(ed_query1.getText().toString().isEmpty()){
                    ed_query1.setError("Enter query");

                }
                else if( ed_subject1.getText().toString().isEmpty()){
                    ed_subject1.setError("Enter subject");

                }
                else {
                    btn_inquire1.setEnabled(false);
                    pb_enquire1.setVisibility(View.VISIBLE);
                    RequestQueue rq= Volley.newRequestQueue(ContactUs.this);
                    StringRequest srq=new StringRequest(StringRequest.Method.POST, inquire_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                String enquiry_id=jsonObject.getString("id");
                                if(status.equals("success")){
                                    pb_enquire1.setVisibility(View.GONE);
                                    //Toast.makeText(Enquiry.this, "", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(v,"Your query has been submitted",Snackbar.LENGTH_SHORT).show();
                                    ed_query1.setText("");

                                    ed_subject1.setText("");
                                    btn_inquire1.setEnabled(true);
                                }
                                //Toast.makeText(Enquiry.this, ""+enquiry_id, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_enquire1.setVisibility(View.GONE);

                                // Toast.makeText(PartStore_Location.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT).show();

                            // Toast.makeText(Enquiry.this, "", Toast.LENGTH_SHORT).show();
                            btn_inquire1.setEnabled(true);
                            pb_enquire1.setVisibility(View.GONE);


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("vid",selected_vehicleid);
                            map.put("cid",categoryid);
                            map.put("subject",ed_subject1.getText().toString());
                            map.put("msg",ed_query1.getText().toString());
                            map.put("CID",cid);
                            return map;
                        }
                    };
                    rq.add(srq);
                }



            }
        });

        btnav_view21.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void getCategoriesList() {
        categorieslist.clear();
        categorieslistid.clear();
        RequestQueue rq= Volley.newRequestQueue(ContactUs.this);
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


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ContactUs.this,
                            android.R.layout.simple_spinner_item, categorieslist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_category1.setAdapter(dataAdapter);
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

    private void getVehicles() {
        vehiclelist.clear();
        vehiclelistid.clear();
        RequestQueue rq= Volley.newRequestQueue(ContactUs.this);
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ContactUs.this,
                                android.R.layout.simple_spinner_item, vehiclelist);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ct_veh1.setAdapter(dataAdapter);
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
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    return true;
            }
            return false;
        }
    };

}
