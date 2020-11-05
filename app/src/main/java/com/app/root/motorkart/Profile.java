package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Profile extends AppCompatActivity {
    ImageView iv_profileback, iv_editp, iv_logoutp, iv_state;
    EditText ed_first, ed_last, ed_mobile, ed_email,ed_address;
    //ProgressBar pb_profile;
    CircleImageView upload_image;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid, checklogin;
    private File imagefile = null;
    String profilepath;
    private MultipartBody.Part file;
    String stateName, cityName, areaName,address;
    BottomNavigationView btnav_view0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    TextView tv_st, tv_ci;
    Button btn_updateprofile;
    int mMenuId;
    Spinner spinner_area, spinner_city, spinner_state;
    List<String> statelist = new ArrayList<>();
    List<String> statelistid = new ArrayList<>();
    private ApiInterface apiInterface;
    ProgressDialog pb_profile;
    final int PIC_CROP = 4;
    LinearLayout ll_profile;
    Button btnn_login_profile;
    List<String> citylist = new ArrayList<>();
    List<String> citylistid = new ArrayList<>();
    TextView tv_ar;
    List<String> arealist = new ArrayList<>();
    List<String> arealistid = new ArrayList<>();
    ArrayAdapter<String> statedataAdapter, citydataAdapter, areadataAdapter;
    String stateId = "0", cityId = "0", areaId = "0";
    String show_userdata = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/showUserData";
    String update_data = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/updateProfile";
    String get_statesurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getallstates";
    String get_citiesurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllCitiesviasid";
    String get_areasurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllAreasviaid";
boolean isedit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        ed_first = findViewById(R.id.ed_first);
        ed_last = findViewById(R.id.ed_last);
        ed_mobile = findViewById(R.id.ed_phone);
        ed_email = findViewById(R.id.ed_emaill);
        ed_address = findViewById(R.id.ed_address);
        iv_profileback = findViewById(R.id.iv_profile);
        iv_state = findViewById(R.id.iv_state);
        //pb_profile=findViewById(R.id.pb_profile);
        iv_editp = findViewById(R.id.iv_editp);
        iv_logoutp = findViewById(R.id.iv_logoutp);
        btnav_view0 = findViewById(R.id.btnav_view0);
        btn_updateprofile = findViewById(R.id.btn_updateprofile);
        spinner_area = findViewById(R.id.spinner_area);
        spinner_city = findViewById(R.id.spinner_city);
        spinner_state = findViewById(R.id.spinner_state);
        upload_image = findViewById(R.id.upload_image);
        tv_st = findViewById(R.id.tv_st);
        tv_ci = findViewById(R.id.tv_ci);
        tv_ar = findViewById(R.id.tv_ar);
        ll_profile = findViewById(R.id.ll_profile);
        btnn_login_profile = findViewById(R.id.btnn_login_profile);
        pb_profile = new ProgressDialog(Profile.this);
        pb_profile.setMessage("Loading..");
        pb_profile.setCancelable(false);
        pb_profile.setCanceledOnTouchOutside(false);


        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        try {
            mMenuId = Integer.parseInt(getIntent().getStringExtra("nav_item"));
            MenuItem menuItem = btnav_view0.getMenu().getItem(1);
            boolean isChecked = menuItem.getItemId() == mMenuId;
            menuItem.setChecked(isChecked);
        } catch (Exception e) {

        }


        sp_login = getSharedPreferences("LOGINDETAILS", MODE_PRIVATE);
        ed_login = sp_login.edit();
        cid = sp_login.getString("cid", "");
        checklogin = sp_login.getString("save_login", "");

        if (checklogin.equals("false")) {
            btnn_login_profile.setVisibility(View.VISIBLE);
            ll_profile.setVisibility(View.GONE);
        } else {

            pb_profile.show();
            ll_profile.setVisibility(View.VISIBLE);
            btnn_login_profile.setVisibility(View.GONE);
        }

        iv_profileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnn_login_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        iv_editp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                isedit=true;
                btn_updateprofile.setVisibility(View.VISIBLE);
                spinner_state.setVisibility(View.VISIBLE);
                spinner_city.setVisibility(View.VISIBLE);
                spinner_area.setVisibility(View.VISIBLE);
                tv_ci.setVisibility(View.GONE);
                tv_ar.setVisibility(View.GONE);
                tv_st.setVisibility(View.GONE);
                iv_state.setVisibility(View.GONE);


                try {
                    if(sp_login.getString("profile_uri", "").equals("")){}
                    else {
                        Uri uri = Uri.parse(sp_login.getString("profile_uri", ""));
                        imagefile = new File(uri.getPath());
                        Log.e("msg", "onActivityResultoooo2: " + imagefile);
                    }

                } catch (Exception e) {
                }


                // upload_image.setImageURI(imagefile.getUri());
//                if(stateName.equals("null"))

                try {
                    ArrayAdapter myAdap = (ArrayAdapter) spinner_state.getAdapter();
                    int spinnerPosition = myAdap.getPosition(stateName);
                    stateId = statelistid.get(spinnerPosition);
                    //Toast.makeText(Profile.this, ""+stateId, Toast.LENGTH_SHORT).show();
                    spinner_state.setSelection(spinnerPosition);
                } catch (Exception e) {
                    // Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("msg", "SERVICECENTERNAME155: " + e.toString());
                }
                if (!stateId.equals("0") && isedit==true ) {
                    getCities(stateId);

                }


                // iv_state.setVisibility(View.VISIBLE);

               /* if(cityName.equals("null")){ }
                else {
                    ArrayAdapter myAdap1 = (ArrayAdapter) spinner_city.getAdapter();
                    int spinnerPosition1 = myAdap1.getPosition(cityName);
                    spinner_city.setSelection(spinnerPosition1);
                }

                if(areaName.equals("null")){
                } else {
                    ArrayAdapter myAdap2 = (ArrayAdapter) spinner_area.getAdapter();
                    int spinnerPosition2 = myAdap2.getPosition(areaName);
                    spinner_area.setSelection(spinnerPosition2);
                }*/


            }
        });

        iv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        iv_logoutp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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

        btn_updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                pb_profile.setMessage("Updating Profile..");
                //   pb_profile.setCancelable(false);
                //pb_profile.setCanceledOnTouchOutside(false);
                pb_profile.show();
                RequestQueue rq = Volley.newRequestQueue(Profile.this);
                StringRequest srq = new StringRequest(StringRequest.Method.POST, update_data, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                Log.e("msg", "onClickimage1: " + imagefile);
                                try {
                                    if (imagefile != null) {
                                        profileUpload(v);
                                    } else {
                                        pb_profile.dismiss();
                                        Snackbar.make(v, "Profile updated succesfully", Snackbar.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    // Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.e("msg", "ProfileeeResponse: " + e.toString());
                                }


                            }
                            //Toast.makeText(Enquiry.this, ""+enquiry_id, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pb_profile.dismiss();

                            // Toast.makeText(PartStore_Location.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Profile.this, "Check Internet Connection" + error.toString(), Toast.LENGTH_SHORT).show();
                        pb_profile.dismiss();


                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("CID", cid);
                        map.put("fname", ed_first.getText().toString());
                        map.put("lname", ed_last.getText().toString());
                        map.put("email", ed_email.getText().toString());
                        map.put("address", ed_address.getText().toString());
                        map.put("mobile", ed_mobile.getText().toString());
                        map.put("stateId", stateId);
                        map.put("cityId", cityId);
                        map.put("areaId", areaId);
                        return map;
                    }
                };
                rq.add(srq);
            }
        });

        btnav_view0.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        getStates();


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateId = statelistid.get(position);
                Log.e("msg", "onCreatecid2: " + stateId);

                if (!stateId.equals("0") && isedit==false) {

                    getCities(stateId);

                }

                /*if(cityName.equals("null")){ }
                else {
                    ArrayAdapter myAdap1 = (ArrayAdapter) spinner_city.getAdapter();
                    int spinnerPosition1 = myAdap1.getPosition(cityName);
                    spinner_city.setSelection(spinnerPosition1);
                }*/

                // Toast.makeText(Profile.this, "State Id: "+stateId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = citylistid.get(position);
                Log.e("msg", "onCreatecid2: " + cityId);


                if ((!stateId.equals("0")) && (!cityId.equals("0")) && isedit==false) {
                    getAreas(stateId, cityId);


                }


                //  Toast.makeText(Profile.this, "City Id: "+cityId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaId = arealistid.get(position);
                Log.e("msg", "onCreatecid2: " + areaId);
                //Toast.makeText(Profile.this, "Area Id: "+areaId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showUserData();


            }
        }, 1000);


        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Profile.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(Profile.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && ActivityCompat.shouldShowRequestPermissionRationale(Profile.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(Profile.this,
                                    Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(Profile.this, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA},
                                REQUEST_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    selectImage();
                }
            }
        });


    }


    private void showUserData() {
        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        StringRequest srq = new StringRequest(StringRequest.Method.POST, show_userdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        pb_profile.dismiss();
                        String image = jsonObject.getString("image");

                        profilepath = "http://demo.digitalsolutionsplanet.com/motorkart/uploads/avtar/" + image;
                        //Uri uri = Uri.parse(profilepath);
                        /*imagefile = new File(String.valueOf(Uri.parse(profilepath)));
                        Log.e("msg", "onResponseImagee: " + imagefile);

                        ed_login.putString("profile_uri", String.valueOf(imagefile));
                        ed_login.commit();*/

                        try {
                            if (image.equals("null")) {
                                upload_image.setImageResource(R.drawable.profileimage);
                            } else {
                                Glide.with(getApplicationContext()).load(profilepath).into(upload_image);

                            }

                            //Picasso.with(Profile.this).load("http://demo.digitalsolutionsplanet.com/motorkart/uploads/avtar/"+image).into(upload_image);

                        } catch (Exception e) {
                            //  Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("msg", "onResponse11111111111: " + e.toString());
                            upload_image.setImageResource(R.drawable.profileimage);
                        }
                        ed_first.setText(jsonObject.getString("first_name"));
                        ed_last.setText(jsonObject.getString("last_name"));
                        ed_mobile.setText(jsonObject.getString("mobile"));
                        ed_email.setText(jsonObject.getString("email"));
                        ed_address.setText(jsonObject.getString("address"));
                        stateName = jsonObject.getString("stateName");
                        cityName = jsonObject.getString("cityName");
                        areaName = jsonObject.getString("areaName");
                        Log.e("msg", "onResponseeeeeeeee: " + image);


                        //String areaid=jsonObject.getString("areaid");


                        if (stateName.equals("null")) {
                            tv_st.setText("");
                        } else {
                            tv_st.setText(stateName);
                        }
                        if (cityName.equals("null")) {
                            tv_ci.setText("");
                        } else {
                            tv_ci.setText(cityName);
                        }

                        if (areaName.equals("null")) {
                            tv_ar.setText("");
                        } else {
                            tv_ar.setText(areaName);
                        }


                        // spinner_state.setSelection(Integer.parseInt(statelistid.get(Integer.parseInt(stateid))));
                        // spinner_city.setSelection(Integer.parseInt(citylistid.get(Integer.parseInt(cityid))));


                        // spinner_area.setSelection(Integer.parseInt(arealistid.get(Integer.parseInt(areaid))));


                        //Toast.makeText(Profile.this, ""+Integer.parseInt(statelistid.get(Integer.parseInt(stateid))), Toast.LENGTH_SHORT).show();
                        ed_first.requestFocus(ed_first.getText().length());

                    }
                    //Toast.makeText(Enquiry.this, ""+enquiry_id, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_profile.dismiss();


                    //Toast.makeText(Profile.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(Profile.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pb_profile.dismiss();


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

    private void getStates() {
        statelist.clear();
        statelist.add("Select state");
        statelistid.clear();
        statelistid.add("0");
        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        StringRequest sr = new StringRequest(Request.Method.GET, get_statesurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("msg", "StoreResponse1: " + response.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                        statelist.add(jsonObject1.getString("state_name"));
                        statelistid.add(jsonObject1.getString("sid"));
                    }

                    statedataAdapter = new ArrayAdapter<String>(Profile.this,
                            android.R.layout.simple_spinner_item, statelist);
                    statedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    spinner_state.setAdapter(statedataAdapter);
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

    private void getCities(final String sid) {
        citylist.clear();
        citylist.add("Select city");
        citylistid.clear();
        citylistid.add("0");
        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        StringRequest sr = new StringRequest(Request.Method.POST, get_citiesurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("msg", "StoreResponse1: " + response.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                        citylist.add(jsonObject1.getString("city_name"));
                        citylistid.add(jsonObject1.getString("cid"));
                    }

                    citydataAdapter = new ArrayAdapter<String>(Profile.this,
                            android.R.layout.simple_spinner_item, citylist);
                    citydataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_city.setAdapter(citydataAdapter);

                    if(isedit==true){
                        try {
                            ArrayAdapter myAdap = (ArrayAdapter) spinner_city.getAdapter();
                            int spinnerPosition = myAdap.getPosition(cityName);
                            cityId = citylistid.get(spinnerPosition);
                            //Toast.makeText(Profile.this, ""+stateId, Toast.LENGTH_SHORT).show();
                            spinner_city.setSelection(spinnerPosition);
                            getAreas(stateId, cityId);

                        } catch (Exception e) {
                            // Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("msg", "SERVICECENTERNAME155: " + e.toString());
                        }
                    }
                    else {
                        Log.e("msg", "SERVICECENTERNAME155: j" );

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //    Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Profile.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();


                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("sid", sid);
                return map;
            }
        };
        rq.add(sr);
    }

    private void getAreas(final String sid, final String cid) {
        arealist.clear();
        arealist.add("Select area");
        arealistid.clear();
        arealistid.add("0");
        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        StringRequest sr = new StringRequest(Request.Method.POST, get_areasurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("msg", "StoreResponse1: " + response.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                        arealist.add(jsonObject1.getString("area_name"));
                        arealistid.add(jsonObject1.getString("aid"));
                    }


                    areadataAdapter = new ArrayAdapter<String>(Profile.this,
                            android.R.layout.simple_spinner_item, arealist);
                    areadataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_area.setAdapter(areadataAdapter);

                    if(isedit==true){
                        try {
                            ArrayAdapter myAdap = (ArrayAdapter) spinner_area.getAdapter();
                            int spinnerPosition = myAdap.getPosition(areaName);
                            areaId = arealistid.get(spinnerPosition);
                            //Toast.makeText(Profile.this, ""+stateId, Toast.LENGTH_SHORT).show();
                            spinner_area.setSelection(spinnerPosition);
                        } catch (Exception e) {
                            // Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("msg", "SERVICECENTERNAME155: " + e.toString());
                        }
                    }
                    isedit=false;

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(Profile.this, ""+error.toString(), Toast.LENGTH_SHORT).show();


                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("sid", sid);
                map.put("cid", cid);
                return map;
            }
        };
        rq.add(sr);
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
                    Intent intent2 = new Intent(getApplicationContext(), Profile.class);
                    intent2.putExtra("nav_item", String.valueOf(item.getItemId()));
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                //  performCrop(tempUri);



             /*   CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);*/

// start cropping activity for pre-acquired image saved on the device
                CropImage.activity(tempUri)
                        .start(this);


                Log.d("msg", "Camerapath: " + imagefile);
            } catch (Exception e) {
            }


        } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                //performCrop(selectedImage);

          /*      CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);*/

// start cropping activity for pre-acquired image saved on the device
                CropImage.activity(selectedImage).start(this);
               /* Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath= cursor.getString(columnIndex);
                    //  String image_name=imgPath.substring(imgPath.lastIndexOf("/")+1);
                    imagefile=new File(imgPath);
                    upload_image.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                    Log.d("msg", "Camerapath1: "+imagefile);


                }*/
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();
//                imgPath=getRealPathFromURI(resultUri);
                // String image_name=imgPath.substring(imgPath.lastIndexOf("/")+1);
                imagefile = new File(resultUri.getPath());
                Log.e("msg", "onActivityResultoooo: " + imagefile);
                Log.e("msg", "onActivityResultoooo1: " + result);

                upload_image.setImageURI(result.getUri());


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }


        /*else if (requestCode == PIC_CROP && resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();
//get the cropped bitmap
            Bitmap thePic = extras.getParcelable("data");
            Uri tempUri2 = getImageUri(getApplicationContext(), thePic);

            imgPath=getRealPathFromURI(tempUri2);
            // String image_name=imgPath.substring(imgPath.lastIndexOf("/")+1);
            imagefile = new File(imgPath);
            upload_image.setImageBitmap(thePic);
            Log.d("msg", "Camerapath: "+imagefile);
        }*/

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    private void profileUpload(final View v) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody CID = RequestBody.create(MediaType.parse("text/plain"), cid);

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);
        file = MultipartBody.Part.createFormData("file", imagefile.getName(), body);
        Log.e("msg", "property_imageupload1: " + imagefile.getName());
        Log.e("msg", "property_imageupload2: " + imagefile);
        //  Toast.makeText(this, "1: "+imagefile.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "2: "+imagefile, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "3: "+image, Toast.LENGTH_SHORT).show();


        Call<JsonElement> call = apiInterface.insert_profile(file, CID);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {

                try {

                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    //Toast.makeText(Profile.this, ""+response.body().toString(), Toast.LENGTH_SHORT).show();
                    Log.e("msg", "property_imageupload5: " + response.body().toString());

                    String successval = jsonObject.optString("status");
                    if (successval.equals("success")) {
//                            Toast.makeText(AddPostProperty2.this,"Update Successfully",Toast.LENGTH_LONG).show();
                        pb_profile.dismiss();
                        ed_login.putString("profile_uri", String.valueOf(imagefile));
                        ed_login.commit();
                        Snackbar.make(v, "Profile updated succesfully", Snackbar.LENGTH_LONG).show();


                    } else if (successval.equals("image size error")) {

                        final Dialog dialog = new Dialog(Profile.this);
                        dialog.setContentView(R.layout.dialog_imagesize);
                        dialog.show();
                        Button btn_ok = dialog.findViewById(R.id.btn_ok);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(Profile.this, "Something went wrong.." + successval, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("error", "any error " + e);
                    e.printStackTrace();
                    pb_profile.dismiss();


                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("error", "property_imageupload4" + t.toString());
                pb_profile.dismiss();
                Toast.makeText(Profile.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

   /* private void uploadImage(final Bitmap bitmap){
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("ressssssoo",new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            jsonObject.toString().replace("\\\\","");

                            if (jsonObject.getString("status").equals("true")) {

                                arraylist = new ArrayList<HashMap<String, String>>();
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                String url = "";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    url = dataobj.optString("pathToFile");
                                }
                                Picasso.with(getApplicationContext()).load(url).into(imageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            *//*
     * If you want to add more parameters with the image
     * you can do it here
     * here we have only one parameter with the image
     * which is tags
     * *//*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("tags", "ccccc");  add string parameters
                return params;
            }

            *//*
     *pass files using below method
     * *//*
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("filename", new DataPart(imagename + ".png",
                        getFileDataFromDrawable(bitmap)));
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(Profile.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }*/
}
