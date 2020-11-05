package com.app.root.motorkart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.LocationSpinnerAdapter;
import com.app.root.motorkart.fragments.BookingStatusFragment;
import com.app.root.motorkart.fragments.HomeFragment;
import com.app.root.motorkart.fragments.ProfileFragment;
import com.app.root.motorkart.fragments.ServiceBookFragment;
import com.app.root.motorkart.notifications.GCMRegistrationIntentService;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class HomeScreeeen extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        BookingStatusFragment.OnFragmentInteractionListener, ServiceBookFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    ViewPager vw_slider;
    ImageView iv_menuu, imageView_header;
    TextView spinner_location;
    //Integer[] imageslist={R.drawable.slider_image,R.drawable.app_logo_orange};
    BottomNavigationView navigationView4;
    NavigationView navigationView3;
    DrawerLayout drawer;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid;
    TextView tv_change_location;
    LocationSpinnerAdapter locationSpinnerAdapter;
    TextView tv_emaill, tv_firstt;
    String show_userdata = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/showUserData";
    String getLocation_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getLocation";
    String user_location_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/updateLocation";
    String email, name;
    List<String> locationlist = new ArrayList<>();
    List<String> locationlistid = new ArrayList<>();
    LocationTrack locationTrack;
    Button btn_nav_logout;
    TextView btn_nav_changepwd;
    AlertDialog.Builder builder;
    String token;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String currentVersion,newVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

//updateApp();
        drawer = findViewById(R.id.drawer_layout);
        navigationView3 = findViewById(R.id.nav_view3);
        navigationView4 = findViewById(R.id.nav_view4);
        iv_menuu = findViewById(R.id.iv_menuu);
        spinner_location = findViewById(R.id.spinner_location);
        tv_change_location = findViewById(R.id.tv_change_location);
        btn_nav_logout = navigationView3.findViewById(R.id.btn_nav_logout);
        btn_nav_changepwd = navigationView3.findViewById(R.id.btn_nav_changepwd);

        tv_change_location.setPaintFlags(tv_change_location.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        spinner_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreeeen.this, ChangeLocationActivity.class));
            }
        });


        View headerView = navigationView3.getHeaderView(0);
        tv_firstt = (TextView) headerView.findViewById(R.id.nav_name);
        tv_emaill = (TextView) headerView.findViewById(R.id.nav_emaill);
        imageView_header = (ImageView) headerView.findViewById(R.id.imageView_header);


        sp_login = getSharedPreferences("LOGINDETAILS", MODE_PRIVATE);
        ed_login = sp_login.edit();
        cid = sp_login.getString("cid", "");


        btn_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreeeen.this);
                builder.setMessage("Do you want to Logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ed_login.clear();
                                ed_login.commit();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.show();
            }
        });
        btn_nav_changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });

        navigationView4.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigationView3.setNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, new HomeFragment())
                .commit();


        iv_menuu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!drawer.isDrawerOpen(Gravity.START)) drawer.openDrawer(Gravity.START);
                else drawer.closeDrawer(Gravity.END);
            }
        });


        spinner_location.setText("Delhi");
//        ed_login.putString("user_location","Delhi");
//        ed_login.commit();
        showUserData();

        //getLocation();
        try {
            if (!sp_login.getString("user_location", "").equals("")) {
                spinner_location.setText(sp_login.getString("user_location", ""));
//        ed_login.putString("user_location",sp_login.getString("user_location",""));
//        ed_login.commit();
            } else {
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCurrentLocation(HomeScreeeen.this);
                            // runTimePermission();
                        }
                    }, 100);

                } catch (Exception e) {
                    // spinner_location.setText(sp_login.getString("user_location",""));
                    Log.e("msg", "ResponseLocation: " + e.toString());
                    // Toast.makeText(locationTrack, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {


        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().
                        equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    token = intent.getStringExtra("token");
                    ed_login.putString("SAVETOKEN", token);
                    ed_login.commit();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //sendTokenToServer(sp_login.getString("SAVETOKEN",""));

                        }
                    }, 2000);
                    //sendFCMPush();

                    //Displaying the token as toast
                    // Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    Log.d("msg", "onReceiveToken2: " + token);
                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                //  Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                // Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(HomeScreeeen.this, GCMRegistrationIntentService.class);
            startService(itent);
        }


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    HomeScreeeen.this,Manifest.permission.CAMERA) ){
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(HomeScreeeen.this,
                        new String[]{Manifest.permission.CAMERA},REQUEST_EXTERNAL_STORAGE);

            }
        }

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetVersionCode().execute();


    }

    private void updateApp() {
        try {
            String curversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String newVersion = curversion;
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) .IQ1z0d .htlgb")
                    .first()
                    .ownText();
            Log.e("NewVersion" , curversion);
            Log.e("NewVersion1" , newVersion);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("NewVersion2" , e.toString());

        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;


            switch (item.getItemId()) {
                case R.id.navigation_home:


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new HomeFragment())
                            .commit();
                    return true;


                    case R.id.navigation_qr:
                    Intent intent3 = new Intent(getApplicationContext(), QrcodeScanner.class);
                    intent3.putExtra("nav_item", String.valueOf(item.getItemId()));
                    startActivity(intent3);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_screen2, menu);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onPause() {
        super.onPause();
        drawer.closeDrawer(Gravity.LEFT);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
           /* case R.id.nav_wallet:
                Toast.makeText(this, "Wallet", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_assurance:
                Toast.makeText(this, "Assurance", Toast.LENGTH_SHORT).show();

                return true;*/

            case R.id.nav_pick:
                startActivity(new Intent(HomeScreeeen.this, PickNDrop.class));
                return true;

            case R.id.nav_services:
                Intent intent1 = new Intent(getApplicationContext(), MyServices.class);
                intent1.putExtra("nav_item", String.valueOf(menuItem.getItemId()));
                startActivity(intent1);
                return true;

            case R.id.nav_orders:
                startActivity(new Intent(HomeScreeeen.this, PickNDrop.class));
                return true;

            case R.id.navigation_status:

                //Toast.makeText(getApplicationContext(), "2:", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BookingStatus.class);
               // intent.putExtra("nav_item", String.valueOf(menuItem.getItemId()));
                startActivity(intent);
                return true;

            case R.id.navigation_service:
                Intent intent4 = new Intent(getApplicationContext(), MyServices.class);
               // intent4.putExtra("nav_item", String.valueOf(menuItem.getItemId()));
                startActivity(intent4);
                return true;


            case R.id.nav_vehicles:
                startActivity(new Intent(HomeScreeeen.this, MyVehicles.class));
                return true;

            case R.id.nav_road:
                startActivity(new Intent(HomeScreeeen.this, RoadSideAssistance.class));
                return true;

            case R.id.nav_rsa:
                startActivity(new Intent(HomeScreeeen.this, RSAStatus.class));

                return true;

            case R.id.nav_addstore:
                startActivity(new Intent(HomeScreeeen.this, AddStore.class));
                return true;


            case R.id.nav_mystore:
                startActivity(new Intent(HomeScreeeen.this, MyStores.class));
                return  true;

            case R.id.nav_contact:
                startActivity(new Intent(HomeScreeeen.this, ContactUs.class));
                return true;
            case R.id.nav_about:
                startActivity(new Intent(HomeScreeeen.this, AboutUs.class));
                return true;


        }
        return false;

    }

    private void showUserData() {
        RequestQueue rq = Volley.newRequestQueue(HomeScreeeen.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, show_userdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String image = jsonObject.getString("image");
                        try {
                            if (image.equals("null")) {
                                imageView_header.setImageResource(R.drawable.profileimage);
                            } else {
                                Glide.with(getApplicationContext()).load(
                                        "http://demo.digitalsolutionsplanet.com/motorkart/uploads/avtar/" + image).into(imageView_header);

                            }

                            //Picasso.with(Profile.this).load("http://demo.digitalsolutionsplanet.com/motorkart/uploads/avtar/"+image).into(upload_image);

                        } catch (Exception e) {
                            //  Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("msg", "onResponse11111111111: " + e.toString());
                            imageView_header.setImageResource(R.drawable.profileimage);
                        }

                        name = jsonObject.getString("first_name");
                        email = jsonObject.getString("email");
                        tv_firstt.setText(name);
                        tv_emaill.setText(email);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("CID", cid);
                return map;
            }
        };
        rq.add(srq);
    }


    private void sendUserLocation(final double latitude, final double longitude, final String city) {
        RequestQueue rq = Volley.newRequestQueue(HomeScreeeen.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, user_location_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(locationTrack, "runTimePermissionLOcation3" + e.toString(), Toast.LENGTH_SHORT).show();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("lat_long", latitude + "," + longitude);
                map.put("mobile_location", city);
                map.put("CID", cid);
                return map;
            }
        };
        rq.add(srq);
    }


    @Override
    public void onBackPressed() {
//        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
//            this.drawer.closeDrawer(GravityCompat.START);
//        } else {
        //super.onBackPressed();
        builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to exit?").setTitle("Exit");

        //Setting message manually and performing action on button click

        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Exit");
        alert.show();
        //    }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    public void getCurrentLocation(HomeScreeeen context) {
        // when you need location
        // if inside activity context = this;

        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        //  Log.d("Location", "my location is " + location.toString());
                        //Toast.makeText(getApplicationContext(), ""+location.latitude, Toast.LENGTH_SHORT).show();

                        double longitude = location.longitude;
                        double latitude = location.latitude;
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(HomeScreeeen.this, Locale.getDefault());

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
                                spinner_location.setText(city);

                                ed_login.putString("user_location", city);
                                ed_login.commit();
                                sendUserLocation(latitude, longitude, city);
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void runTimePermission() {
        locationTrack = new LocationTrack(HomeScreeeen.this);


        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            Log.e("msg", "runTimePermissionLOcation: " + "Longitude:" + Double.toString(longitude) +
                    "\nLatitude:" + Double.toString(latitude));

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
                    spinner_location.setText(city);

                    ed_login.putString("user_location", city);
                    ed_login.commit();
                    sendUserLocation(latitude, longitude, city);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }



    class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" +
                        getApplicationContext().getPackageName()+ "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                Log.e("msg", "CurrentVersion1"+currentVersion+",New Version: "+newVersion );

                return newVersion;
            } catch (Exception e) {
                Log.e("msg", "CurrentVersion2"+e.toString() );

                return newVersion;

            }


        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Log.e("msg", "CurrentVersion"+currentVersion+",New Version: "+newVersion );
            if (Double.parseDouble(currentVersion)< Double.parseDouble(newVersion)) {
                //show dialog
                new androidx.appcompat.app.AlertDialog.Builder(HomeScreeeen.this)
                        .setTitle("Updated app available!")
                        .setMessage("Want to update app?")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                final String appPackageName = getPackageName();
                                // getPackageName() from Context or Activity object
                                try {
                                   // Toast.makeText(getApplicationContext(), "App is in BETA version cannot update", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        })
                        .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                               // new MyAsyncTask().execute();
                            }
                        })
                        .setIcon(R.drawable.error)
                        .show();

            }
        }
    }
}
