package com.app.root.motorkart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.root.motorkart.adapter.VehicleAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
Button btn_login,tv_home;
TextView tv_register,tv_lg_mobile,tv_forgotpassword;
ProgressBar pb_login;
EditText login_mobile,login_password;
SharedPreferences sp_login;
    LocationTrack locationTrack;
SharedPreferences.Editor ed_login;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
String loginurl="http://demo.digitalsolutionsplanet.com/motorkart/api/register_android/check";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       btn_login=findViewById(R.id.btnn_login);
        tv_register=findViewById(R.id.tv_register);
        login_mobile=findViewById(R.id.login_mobile);
        login_password=findViewById(R.id.login_password);
        tv_lg_mobile=findViewById(R.id.tv_lg_mobile);
        pb_login=findViewById(R.id.pb_login);
        tv_home=findViewById(R.id.tv_home);
        tv_forgotpassword=findViewById(R.id.tv_forgotpassword);

        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    LoginActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                            LoginActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



       btn_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v) {

        if(login_mobile.getText().toString().isEmpty() && login_password.getText().toString().isEmpty()){
            login_mobile.setError("Enter email");
           login_password.setError("Enter password");
        }
        else if(login_mobile.getText().toString().isEmpty()){
            login_mobile.setError("Enter email");

        }
        else if(login_password.getText().toString().isEmpty()){
            login_password.setError("Enter password");

        }


        else {
        pb_login.setVisibility(View.VISIBLE);
        btn_login.setEnabled(false);
        RequestQueue rq= Volley.newRequestQueue(LoginActivity.this);
        StringRequest sr=new StringRequest(Request.Method.POST, loginurl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject=new JSONObject(response);
                String status=jsonObject.getString("status");

                if(status.equals("success")){
                    pb_login.setVisibility(View.GONE);
                    String isCheckVehicle=jsonObject.getString("isCheckVehicle");
                    String id=jsonObject.getString("id");
                    ed_login.putString("cid",id);
                    ed_login.putString("save_login","true");
                    ed_login.commit();

                    Intent intent=new Intent(LoginActivity.this,HomeScreeeen.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Snackbar snackbar=Snackbar.make(v,"Incorrect username or password..",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    pb_login.setVisibility(View.GONE);
                    btn_login.setEnabled(true);


                }

            } catch (JSONException e) {
                e.printStackTrace();
                pb_login.setVisibility(View.GONE);
                Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                snackbar.show();
                btn_login.setEnabled(true);

            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Snackbar snackbar=Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT);
            snackbar.show();
            btn_login.setEnabled(true);

            pb_login.setVisibility(View.GONE);

            //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> map=new HashMap<>();
            map.put("email",login_mobile.getText().toString());
            map.put("password",login_password.getText().toString());
            return map;
        }
    };
    rq.add(sr);
}

           }
       });

        tv_lg_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();

            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,LoginMobile.class));
            }
        });

        tv_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));

            }
        });

        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_login.putString("save_login","false");
                ed_login.commit();
                startActivity(new Intent(LoginActivity.this,HomeScreeeen.class));

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if(isGPSEnabled(getApplicationContext())){}
            else { Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);}


        }
    }
    public boolean isGPSEnabled(Context mContext)
    {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



   /* public void runTimePermission(){
        locationTrack = new LocationTrack(LoginActivity.this);


        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            Log.e("msg", "runTimePermissionLOcation: "+"Longitude:" + Double.toString(longitude) +
                    "\nLatitude:" + Double.toString(latitude) );

            *//*Geocoder geocoder;
            List<Address> addresses=new ArrayList<>();
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                Log.e("msg", "runTimePermissionLOcation1: "+state );
                spinner_location.setText(state);
                //ed_login.putString("user_location",state);
               // ed_login.commit();

                sendUserLocation(latitude,longitude,state);

            } catch (IOException e) {
                e.printStackTrace();
            }*//*

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            Log.e("latitude", "latitude--" + latitude);

            try {
                Log.e("latitude", "inside latitude--" + latitude);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    Toast.makeText(getApplicationContext(), ""+city, Toast.LENGTH_SHORT).show();

                    ed_login.putString("user_location",city);
                    ed_login.commit();
                   // sendUserLocation(latitude,longitude,city);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(locationTrack, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_EXTERNAL_STORAGE){
            runTimePermission();
        }
    }*/
}
