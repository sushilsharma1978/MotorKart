package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.LocationStoreImagesAdapter;
import com.app.root.motorkart.adapter.RateAdapter;
import com.app.root.motorkart.adapter.TopSlider;
import com.app.root.motorkart.modelclass.RateListModel;
import com.app.root.motorkart.modelclass.StoreImages;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PartStore_Location extends AppCompatActivity implements OnMapReadyCallback {
    //RecyclerView rv_part_loc;
    LocationStoreImagesAdapter locationStoreImagesAdapter;
    String storeid, store_num, email, phone, timings;
    LinearLayout txtt;
    BottomNavigationView btnav_view3;
    String location, address, exactlocation, location_coordinates;
    double loc_lat, loc_lng;
    RecyclerView rv_ratelist;
    RateAdapter rateAdapter;
    GoogleMap googleMap;
    ImageView iv_partloc_back;
    SupportMapFragment mapFragment;
    ViewPager vw_storeimages;
    ImageView iv_ratelist;
    CircleIndicator indicator2;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    Dialog ratedialog;
    TextView tv_loc_cntrno, tv_loc_address, txt_tool2, tv_loc_timing, tv_loc_mobile, tv_loc_email;
    List<StoreImages> storeImagesList = new ArrayList<>();
    List<RateListModel> rateList = new ArrayList<>();
    int mMenuId;
    String storeimages_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreImages";
    String storeimagesByNum_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreImagesByNumber";
    String storeDeatilById_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreDetailsById";
    String storeRateList_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreRateListById";
    String storeRateListByNum_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreRateListByNumber";
    String storeDeatilByNum_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreDetailsByStoreNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_store__location);

        //rv_part_loc=findViewById(R.id.rv_part_loc);
        tv_loc_cntrno = findViewById(R.id.tv_loc_cntrno);
        iv_ratelist = findViewById(R.id.iv_ratelist);
        tv_loc_address = findViewById(R.id.tv_loc_address);
        txt_tool2 = findViewById(R.id.txt_tool2);
        btnav_view3 = findViewById(R.id.btnav_view3);
        iv_partloc_back = findViewById(R.id.iv_partloc_back);
        tv_loc_timing = findViewById(R.id.tv_loc_timing);
        tv_loc_mobile = findViewById(R.id.tv_loc_mobile);
        tv_loc_email = findViewById(R.id.tv_loc_email);
        vw_storeimages = findViewById(R.id.vw_storeimages);
        indicator2 = findViewById(R.id.indicator2);

        txtt = findViewById(R.id.txtt);

        try {
            mMenuId = Integer.parseInt(getIntent().getStringExtra("nav_item"));
            MenuItem menuItem = btnav_view3.getMenu().getItem(4);
            boolean isChecked = menuItem.getItemId() == mMenuId;
            menuItem.setChecked(isChecked);
        } catch (Exception e) {

        }


        storeid = getIntent().getStringExtra("store_id");

        // storename=getIntent().getStringExtra("store_name");
        if (getIntent().hasExtra("store_num")) {
            store_num = getIntent().getStringExtra("store_num");
            getStoreInfoByNum(store_num);
            getStoreImagesByNum(store_num);
        } else {
            getPartStoreLocation();
            getStoreImages();
        }


        ratedialog = new Dialog(PartStore_Location.this);
        ratedialog.setContentView(R.layout.dialog_ratelist);
        rv_ratelist = ratedialog.findViewById(R.id.rv_ratelist);

        iv_ratelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("store_num")) {
                    store_num = getIntent().getStringExtra("store_num");
                    getStoreRateListByNum(store_num);
                } else {
                    getStoreRateList();
                }
            }
        });


        btnav_view3.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        iv_partloc_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fg_map);
        mapFragment.getMapAsync(this);

    }

    private void getStoreRateList() {
        rateList.clear();
        RequestQueue rq = Volley.newRequestQueue(PartStore_Location.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, storeRateList_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonArray.length() > 0) {
                        ratedialog.show();
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            RateListModel rateListModel = new RateListModel(jsonObject1.getString("option_id"),
                                    jsonObject1.getString("store_id"), jsonObject1.getString("name"),
                                    jsonObject1.getString("price"), jsonObject1.getString("status"),
                                    jsonObject1.getString("isDeleted"), jsonObject1.getString("created_on"));

                            rateList.add(rateListModel);
                        }
                        rateAdapter = new RateAdapter(PartStore_Location.this, rateList);
                        rv_ratelist.setLayoutManager(new LinearLayoutManager(PartStore_Location.this, RecyclerView.VERTICAL, false));
                        rv_ratelist.setAdapter(rateAdapter);
                    } else {
                        Toast.makeText(PartStore_Location.this, "No RateList", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(PartStore_Location.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(PartStore_Location.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", storeid);
                return map;
            }
        };
        rq.add(srq);
    }

    private void getStoreRateListByNum(final String store_num) {
        rateList.clear();
        RequestQueue rq = Volley.newRequestQueue(PartStore_Location.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, storeRateListByNum_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonArray.length() > 0) {
                        ratedialog.show();
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            RateListModel rateListModel = new RateListModel(jsonObject1.getString("option_id"),
                                    jsonObject1.getString("store_id"), jsonObject1.getString("name"),
                                    jsonObject1.getString("price"), jsonObject1.getString("status"),
                                    jsonObject1.getString("isDeleted"), jsonObject1.getString("created_on"));

                            rateList.add(rateListModel);
                        }
                        rateAdapter = new RateAdapter(PartStore_Location.this, rateList);
                        rv_ratelist.setLayoutManager(new LinearLayoutManager(PartStore_Location.this, RecyclerView.VERTICAL, false));
                        rv_ratelist.setAdapter(rateAdapter);
                    } else {
                        Toast.makeText(PartStore_Location.this, "No RateList", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(PartStore_Location.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(PartStore_Location.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("storeNumber", store_num);
                return map;
            }
        };
        rq.add(srq);
    }

    private void getPartStoreLocation() {
        // Toast.makeText(this, ""+storeid, Toast.LENGTH_SHORT).show();
        RequestQueue rq = Volley.newRequestQueue(PartStore_Location.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, storeDeatilById_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Toast.makeText(PartStore_Location.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            address = jsonObject1.getString("address");
                            // location ="30.7040,76.7296";
                            location = jsonObject1.getString("location");
                            location_coordinates = jsonObject1.getString("location_coordinates");
                            email = jsonObject1.getString("email");
                            phone = jsonObject1.getString("phone");
                            timings = jsonObject1.getString("timings");
                            txt_tool2.setText(jsonObject1.getString("name"));
                            tv_loc_address.setText(address);
                            tv_loc_mobile.setText(phone);
                            tv_loc_timing.setText(timings);
                            tv_loc_email.setText(email);
                            tv_loc_cntrno.setText(jsonObject1.getString("store_num"));
                            //Toast.makeText(PartStore_Location.this, ""+location_coordinates, Toast.LENGTH_SHORT).show();
                            //  exactlocation = address +","+location;
                            // exactlocation = "28°24'51.6\"N 77°49'32.4\"E";


                            //double strLongitude = Double.parseDouble(Location.convert(Double.parseDouble("77°49'32.4"), Location.FORMAT_DEGREES));
                            // double strLatitude = Double.parseDouble(Location.convert(Double.parseDouble("28°24'51.6"), Location.FORMAT_DEGREES));
//loc_lat= Double.parseDouble("30.7040");
//loc_lng= Double.parseDouble("76.7296");

                            try {
                                String[] ans = location_coordinates.split(",");
                                loc_lat = Double.parseDouble(ans[0]);
                                loc_lng = Double.parseDouble(ans[1]);

                                LatLng latlng = new LatLng(loc_lat, loc_lng);
                                googleMap.addMarker(new MarkerOptions().position(latlng).title(address));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                                Log.e("msg", "onResponseLocation: " + location);
                            } catch (Exception e) {
                                // Toast.makeText(PartStore_Location.this, ""+ e.toString(), Toast.LENGTH_SHORT).show();

                            }
                            //getLatLngFromAddress(exactlocation);
                        }
                    } else {
                        txtt.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(PartStore_Location.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(PartStore_Location.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", storeid);
                return map;
            }
        };

        rq.add(srq);
    }

    private void getStoreInfoByNum(final String store_num) {
        // Toast.makeText(this, ""+storeid, Toast.LENGTH_SHORT).show();
        RequestQueue rq = Volley.newRequestQueue(PartStore_Location.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, storeDeatilByNum_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Toast.makeText(PartStore_Location.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            address = jsonObject1.getString("address");
                            // location ="30.7040,76.7296";
                            location = jsonObject1.getString("location");
                            location_coordinates = jsonObject1.getString("location_coordinates");
                            email = jsonObject1.getString("email");
                            phone = jsonObject1.getString("phone");
                            timings = jsonObject1.getString("timings");
                            txt_tool2.setText(jsonObject1.getString("name"));
                            tv_loc_address.setText(address);
                            tv_loc_mobile.setText(phone);
                            tv_loc_timing.setText(timings);
                            tv_loc_email.setText(email);
                            tv_loc_cntrno.setText(jsonObject1.getString("store_num"));
                            //Toast.makeText(PartStore_Location.this, ""+location_coordinates, Toast.LENGTH_SHORT).show();
                            //  exactlocation = address +","+location;
                            // exactlocation = "28°24'51.6\"N 77°49'32.4\"E";


                            //double strLongitude = Double.parseDouble(Location.convert(Double.parseDouble("77°49'32.4"), Location.FORMAT_DEGREES));
                            // double strLatitude = Double.parseDouble(Location.convert(Double.parseDouble("28°24'51.6"), Location.FORMAT_DEGREES));
//loc_lat= Double.parseDouble("30.7040");
//loc_lng= Double.parseDouble("76.7296");

                            try {
                                String[] ans = location_coordinates.split(",");
                                loc_lat = Double.parseDouble(ans[0]);
                                loc_lng = Double.parseDouble(ans[1]);

                                LatLng latlng = new LatLng(loc_lat, loc_lng);
                                googleMap.addMarker(new MarkerOptions().position(latlng).title(address));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                                Log.e("msg", "onResponseLocation: " + location);
                            } catch (Exception e) {
                                // Toast.makeText(PartStore_Location.this, ""+ e.toString(), Toast.LENGTH_SHORT).show();

                            }
                            //getLatLngFromAddress(exactlocation);
                        }
                    } else {
                        txtt.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(PartStore_Location.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(PartStore_Location.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("storeNumber", store_num);
                return map;
            }
        };

        rq.add(srq);
    }


    private void getStoreImages() {
        RequestQueue rq = Volley.newRequestQueue(PartStore_Location.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, storeimages_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");


                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            StoreImages storeImages = new StoreImages(jsonObject1.getString("gid"),
                                    jsonObject1.getString("store_id"), jsonObject1.getString("image"));
                            storeImagesList.add(storeImages);

                        }

                        locationStoreImagesAdapter = new LocationStoreImagesAdapter(getApplicationContext(),
                                storeImagesList);
                        vw_storeimages.setAdapter(locationStoreImagesAdapter);
                        indicator2.setViewPager(vw_storeimages);

                        NUM_PAGES = storeImagesList.size();

                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;
                                }
                                vw_storeimages.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 3000, 3000);
                    } else {
                        txtt.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(PartStore_Location.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(PartStore_Location.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", storeid);
                return map;
            }
        };
        rq.add(srq);
    }

    private void getStoreImagesByNum(final String store_num) {
        RequestQueue rq = Volley.newRequestQueue(PartStore_Location.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, storeimagesByNum_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");


                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            StoreImages storeImages = new StoreImages(jsonObject1.getString("gid"),
                                    jsonObject1.getString("store_id"), jsonObject1.getString("image"));
                            storeImagesList.add(storeImages);

                        }

                        locationStoreImagesAdapter = new LocationStoreImagesAdapter(getApplicationContext(),
                                storeImagesList);
                        vw_storeimages.setAdapter(locationStoreImagesAdapter);
                        indicator2.setViewPager(vw_storeimages);

                        NUM_PAGES = storeImagesList.size();

                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;
                                }
                                vw_storeimages.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 3000, 3000);
                    } else {
                        txtt.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(PartStore_Location.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(PartStore_Location.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("storeNumber", store_num);
                return map;
            }
        };
        rq.add(srq);
    }


    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            Toast.makeText(this, "Enable Location", Toast.LENGTH_SHORT).show();
        }
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //Edit the following as per you needs

    }

    public void getLatLngFromAddress(String exactlocation) {
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(exactlocation, 1);


            Address address = adresses.get(0);
            if (adresses.size() > 0) {
                double latitude = adresses.get(0).getLatitude();
                double longitude = adresses.get(0).getLongitude();

                LatLng latlng = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(latlng).title(exactlocation));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            }

          /*  for(Address add : adresses){

                 longitude = add.getLongitude();
                     latitude = add.getLatitude();
                Log.e("msg", "getLatLngFromAddress: "+latitude+","+longitude );

                //
                }*/
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("msg", "getLatLngFromAddress1: " + e.toString());

        }
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

}
