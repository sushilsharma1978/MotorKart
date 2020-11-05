package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.VehicleAdapter;
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

public class VehicleContact extends AppCompatActivity {
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid;
    BottomNavigationView btnav_view04;
    ImageView iv_contact_backk;
    EditText ed_sub_ct,ed_query_ct;
    Button btn_contactus;
    String selected_vehicleid;
    Spinner spinner_ct_veh;
    ProgressBar pb_contactus;
    List<String> vehiclelist=new ArrayList<>();
    List<String> vehiclelistid=new ArrayList<>();
    String contact_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/sendMail";
    String myvehicles="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getVechiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_contact);
        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");

        spinner_ct_veh=findViewById(R.id.spinner_ct_veh);
        btn_contactus=findViewById(R.id.btn_contactus);
        btnav_view04=findViewById(R.id.btnav_view04);
        ed_sub_ct=findViewById(R.id.ed_sub_ct);
        ed_query_ct=findViewById(R.id.ed_query_ct);
        pb_contactus=findViewById(R.id.pb_contactus);
        iv_contact_backk=findViewById(R.id.iv_contact_backk);

        iv_contact_backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getVehicles();

        spinner_ct_veh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_vehicleid = vehiclelistid.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(ed_query_ct.getText().toString().isEmpty() || ed_sub_ct.getText().toString().isEmpty() ||
                spinner_ct_veh.getSelectedItem().equals("Select your vehicle")){
                    Snackbar.make(v,"Please fill all fields",Snackbar.LENGTH_LONG).show();

                }
                else {
                    pb_contactus.setVisibility(View.VISIBLE);
                    RequestQueue rq= Volley.newRequestQueue(VehicleContact.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, contact_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");

                                if(status.equals("success")){
                                    pb_contactus.setVisibility(View.GONE);

                                    Snackbar.make(v,"Email has been sent successfully",Snackbar.LENGTH_LONG).show();
ed_query_ct.setText("");
ed_sub_ct.setText("");
spinner_ct_veh.setSelection(0);
                                }
                                else {
                                    pb_contactus.setVisibility(View.GONE);

                                    Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_contactus.setVisibility(View.GONE);

                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pb_contactus.setVisibility(View.GONE);

                            Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_LONG).show();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("registration_num",selected_vehicleid);
                            map.put("subject",ed_sub_ct.getText().toString());
                            map.put("message",ed_query_ct.getText().toString());
                            map.put("CID",cid);
                            return map;
                        }
                    };
                    rq.add(sr);
                }

            }
        });

        btnav_view04.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void getVehicles() {
        vehiclelist.clear();
        vehiclelist.add("Select your vehicle");
        vehiclelistid.clear();
        vehiclelistid.add("0");
        RequestQueue rq= Volley.newRequestQueue(VehicleContact.this);
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VehicleContact.this,
                                android.R.layout.simple_spinner_item, vehiclelist);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ct_veh.setAdapter(dataAdapter);



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
                    Intent intent2=new Intent(getApplicationContext(),Profile.class);
                    intent2.putExtra("nav_item",String.valueOf(item.getItemId()));
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
}
