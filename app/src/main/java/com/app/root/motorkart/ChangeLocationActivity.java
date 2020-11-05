package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.CityListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeLocationActivity extends AppCompatActivity {
    ImageView iv_loc_back;
    ProgressBar pb_ch_loc;
    TextView tv_currentloc;
    RecyclerView rv_location;
    CityListAdapter cityListAdapter;
    LocationTrack locationTrack;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String citylisturl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllCities";
    List<String> citylist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        iv_loc_back=findViewById(R.id.iv_loc_back);
        rv_location=findViewById(R.id.rv_location);
        pb_ch_loc=findViewById(R.id.pb_ch_loc);
        tv_currentloc=findViewById(R.id.tv_currentloc);
        iv_loc_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        pb_ch_loc.setVisibility(View.VISIBLE);

        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();

       foo(ChangeLocationActivity.this);
        /*pb_ch_loc.setVisibility(View.VISIBLE);
        citylist.clear();
        if(!sp_login.getString("user_location","").equals("")){
            citylist.add(sp_login.getString("user_location",""));
            apiCity();
        }
        else {

        }*/

        tv_currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  runTimePermission();
                foo(ChangeLocationActivity.this);
            }
        });
    }

    public void foo(ChangeLocationActivity context) {
        // when you need location
        // if inside activity context = this;
        pb_ch_loc.setVisibility(View.VISIBLE);
        citylist.clear();
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        //  Log.d("Location", "my location is " + location.toString());
                        //Toast.makeText(getApplicationContext(), ""+location.latitude, Toast.LENGTH_SHORT).show();

                        double longitude = location.longitude;
                        double latitude = location.latitude;
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(ChangeLocationActivity.this, Locale.getDefault());

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
                                citylist.add(city);
                                apiCity();



                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void runTimePermission(){
        pb_ch_loc.setVisibility(View.VISIBLE);
        citylist.clear();
        locationTrack = new LocationTrack(ChangeLocationActivity.this);


        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            Log.e("msg", "runTimePermissionLOcation: "+"Longitude:" + Double.toString(longitude) +
                    "\nLatitude:" + Double.toString(latitude) );

            /*Geocoder geocoder;
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
            }*/

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
                    citylist.add(city);
                    apiCity();



                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


    private void apiCity() {
        int MY_SOCKET_TIMEOUT_MS=3000;
        RequestQueue requestQueue= Volley.newRequestQueue(ChangeLocationActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, citylisturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    JSONArray jsonArray=jsonObject1.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject=jsonArray.getJSONObject(x);
                        String city=jsonObject.getString("city_name");
                        citylist.add(city);
                    }

                    rv_location.setLayoutManager(new LinearLayoutManager(ChangeLocationActivity.this,
                            LinearLayoutManager.VERTICAL,false));
                    cityListAdapter=new CityListAdapter(ChangeLocationActivity.this,citylist);
                    rv_location.setAdapter(cityListAdapter);
                    pb_ch_loc.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();

                    pb_ch_loc.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangeLocationActivity.this, "Check Internet Connection",
                        Toast.LENGTH_SHORT).show();
                pb_ch_loc.setVisibility(View.GONE);

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


}
