package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehicleRegistration extends AppCompatActivity {
ImageView iv_registerv;
TextView tv_v_model,tv_v_brand,tv_v_fuel,tv_v_type;
EditText ed_rgno,ed_rgname;
Button btnn_add_vehicle;
    String regVehicle_url="http://demo.digitalsolutionsplanet.com/motorkart/api/register_android/insertVehRegistration";
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid,brandid,fuelid,mainCategId,modelid,modelname,brandname,fuelname,vehicleType;
    ProgressBar pb_vreg;
    BottomNavigationView btnav_view06;
    String vehicle_typeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");

        brandid=getIntent().getStringExtra("brand_Id");
        mainCategId=getIntent().getStringExtra("mainCat");
        modelid=getIntent().getStringExtra("model_Id");
        fuelid=getIntent().getStringExtra("fuel_type");
        brandname=getIntent().getStringExtra("brandName");
        modelname=getIntent().getStringExtra("modelName");
        fuelname=getIntent().getStringExtra("fuelName");
        vehicleType=getIntent().getStringExtra("vehicleType");


        if(vehicleType.equals("Four Wheeler")){
            vehicle_typeid="95";
        }
        else {
            vehicle_typeid="1";
        }


        iv_registerv=findViewById(R.id.iv_registerv);
        ed_rgno=findViewById(R.id.ed_rgno);
        ed_rgname=findViewById(R.id.ed_rgname);
        btnn_add_vehicle=findViewById(R.id.btnn_add_vehicle);
        pb_vreg=findViewById(R.id.pb_vreg);
        btnav_view06=findViewById(R.id.btnav_view06);
        tv_v_model=findViewById(R.id.tv_v_model);
        tv_v_brand=findViewById(R.id.tv_v_brand);
        tv_v_fuel=findViewById(R.id.tv_v_fuel);
        tv_v_type=findViewById(R.id.tv_v_type);

        tv_v_model.setText(modelname);
        tv_v_brand.setText(brandname);
        tv_v_fuel.setText(fuelname);
        tv_v_type.setText(vehicleType);

        iv_registerv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnn_add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_rgname.getText().toString().isEmpty() || ed_rgno.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please fill all fields",Snackbar.LENGTH_LONG).show();
                }
                else {
                    btnn_add_vehicle.setEnabled(false);
                    pb_vreg.setVisibility(View.VISIBLE);
                    RequestQueue rq= Volley.newRequestQueue(VehicleRegistration.this);
                    StringRequest srq=new StringRequest(StringRequest.Method.POST, regVehicle_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.equals("success")){
                                    pb_vreg.setVisibility(View.GONE);
                                    Toast.makeText(VehicleRegistration.this, "Vehicle registered successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), MyVehicles.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_vreg.setVisibility(View.GONE);
                                btnn_add_vehicle.setEnabled(true);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(VehicleRegistration.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                            pb_vreg.setVisibility(View.GONE);
                            btnn_add_vehicle.setEnabled(true);


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("mainCateg",mainCategId);
                            map.put("brandId",brandid);
                            map.put("modelId",modelid);
                            map.put("fuelType",fuelid);
                            map.put("CID",cid);
                            map.put("registration_num",ed_rgno.getText().toString());
                            map.put("name",ed_rgname.getText().toString());
                            map.put("vehicle_type",vehicle_typeid);
                            return map;
                        }
                    };
                    rq.add(srq);
                }


            }
        });

        btnav_view06.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VehicleRegistration.this,SelectVehicleBrand.class));
        finish();
    }
}
