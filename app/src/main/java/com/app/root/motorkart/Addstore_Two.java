package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Addstore_Two extends AppCompatActivity {
String catid,time,storecharges,storename,storeno,phone,email;
EditText ed_storedisct,ed_storeratings,ed_storeaddress,ed_storelocation,ed_addcoord;
Spinner sp_storestate,sp_storecity,sp_storearea;
Button btn_addstore;
ImageView iv_addstore2;
ProgressBar pb_addstore;
String storeID;
File imagefile=null;
    private MultipartBody.Part file;
    List<String> statelist = new ArrayList<>();
    List<String> statelistid = new ArrayList<>();
    List<String> citylist = new ArrayList<>();
    List<String> citylistid = new ArrayList<>();
    List<String> arealist = new ArrayList<>();
    List<String> arealistid = new ArrayList<>();
    private ApiInterface apiInterface;
    ArrayAdapter<String> statedataAdapter, citydataAdapter, areadataAdapter;
    String get_statesurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getallstates";
    String get_citiesurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllCitiesviasid";
    String get_areasurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllAreasviaid";
    String stateId = "0", cityId = "0", areaId = "0";
    String addstoreurl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/insertStoreFromApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstore__two);

        iv_addstore2=findViewById(R.id.iv_addstore2);
        ed_storedisct=findViewById(R.id.ed_storedisct);
        ed_storeratings=findViewById(R.id.ed_storeratings);
        ed_storeaddress=findViewById(R.id.ed_storeaddress);
        ed_storelocation=findViewById(R.id.ed_storelocation);
        ed_addcoord=findViewById(R.id.ed_addcoord);
        sp_storestate=findViewById(R.id.sp_storestate);
        sp_storecity=findViewById(R.id.sp_storecity);
        sp_storearea=findViewById(R.id.sp_storearea);
        btn_addstore=findViewById(R.id.btn_addstore);
        pb_addstore=findViewById(R.id.pb_addstore);

        storename=getIntent().getStringExtra("storename");
        storeno=getIntent().getStringExtra("storeno");
        email=getIntent().getStringExtra("storeemail");
        phone=getIntent().getStringExtra("storephone");
        time=getIntent().getStringExtra("storetime");
        storecharges=getIntent().getStringExtra("storecharges");
        catid=getIntent().getStringExtra("storecatid");
        imagefile= (File) getIntent().getExtras().get("store_photo");

        getStates();

        iv_addstore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_addstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(ed_storedisct.getText().toString().isEmpty() || ed_storeratings.getText().toString().isEmpty() ||
                        ed_storeaddress.getText().toString().isEmpty() ||ed_storelocation.getText().toString().isEmpty() ||
                        ed_addcoord.getText().toString().isEmpty()){
                    Snackbar.make(view,"Enter mandatory fields",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    btn_addstore.setEnabled(false);
                    pb_addstore.setVisibility(View.VISIBLE);
                    RequestQueue rq= Volley.newRequestQueue(Addstore_Two.this);
                    StringRequest srq=new StringRequest(StringRequest.Method.POST, addstoreurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.equals("store number is already existed")){
                                    pb_addstore.setVisibility(View.GONE);
                                    btn_addstore.setEnabled(true);
                                    Snackbar.make(view,"Store is already registered",Snackbar.LENGTH_SHORT).show();

                                }
                                else if(status.equals("success")){
                                    storeID=jsonObject.getString("storeId");
                                    try {
                                        if (imagefile != null) {
                                            storeimageUpload(view);
                                        } else {
                                            Snackbar.make(view,"Store has been added successfully",Snackbar.LENGTH_SHORT).show();
                                            btn_addstore.setEnabled(true);
                                            pb_addstore.setVisibility(View.GONE);
                                        }

                                    } catch (Exception e) {
                                        // Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("msg", "ProfileeeResponse: " + e.toString());
                                    }



                                }
                                //Toast.makeText(Enquiry.this, ""+enquiry_id, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_addstore.setVisibility(View.GONE);
                                btn_addstore.setEnabled(true);
                              //  Toast.makeText(Addstore_Two.this, "1"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(view,"Check Internet Connection",Snackbar.LENGTH_SHORT).show();

                            // Toast.makeText(Enquiry.this, "", Toast.LENGTH_SHORT).show();
                            btn_addstore.setEnabled(true);
                            pb_addstore.setVisibility(View.GONE);


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("cid",catid);
                            map.put("name",storename);
                            map.put("email",email);
                            map.put("phone",phone);
                            map.put("timings",time);
                            map.put("store_num",storeno);
                            map.put("instt_charge",storecharges);
                            map.put("discount",ed_storedisct.getText().toString());
                            map.put("ratings",ed_storeratings.getText().toString());
                            map.put("address",ed_storeaddress.getText().toString());
                            map.put("location",ed_storelocation.getText().toString());
                            map.put("location_coordinates",ed_addcoord.getText().toString());
                            map.put("state_id",stateId);
                            map.put("city_id",cityId);
                            map.put("area_id",areaId);
                            return map;
                        }
                    };
                    rq.add(srq);
                }
            }
        });



        sp_storestate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateId = statelistid.get(position);
                Log.e("msg", "onCreatecid2: " + stateId);

                if (!stateId.equals("0")) {

                    getCities(stateId);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_storecity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = citylistid.get(position);
                Log.e("msg", "onCreatecid2: " + cityId);


                if ((!stateId.equals("0")) && (!cityId.equals("0"))) {
                    getAreas(stateId, cityId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp_storearea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    }

    private void storeimageUpload(final View v) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody CID = RequestBody.create(MediaType.parse("text/plain"), storeID);

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);
        file = MultipartBody.Part.createFormData("avtar", imagefile.getName(), body);
        Log.e("msg", "property_imageupload1: " + imagefile.getName());
        Log.e("msg", "property_imageupload2: " + imagefile);
        //  Toast.makeText(this, "1: "+imagefile.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "2: "+imagefile, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "3: "+image, Toast.LENGTH_SHORT).show();


        Call<JsonElement> call = apiInterface.insert_storeimage(file, CID);
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
                        pb_addstore.setVisibility(View.GONE);

                        Snackbar.make(v,"Store has been added successfully",Snackbar.LENGTH_SHORT).show();



                    }

                } catch (JSONException e) {
                    Log.e("error", "any error " + e);
                    e.printStackTrace();
                    pb_addstore.setVisibility(View.GONE);


                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("error", "property_imageupload4" + t.toString());
                pb_addstore.setVisibility(View.GONE);

                Toast.makeText(Addstore_Two.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void getStates() {
        statelist.clear();
        statelist.add("Select state");
        statelistid.clear();
        statelistid.add("0");
        RequestQueue rq = Volley.newRequestQueue(Addstore_Two.this);
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

                    statedataAdapter = new ArrayAdapter<String>(Addstore_Two.this,
                            android.R.layout.simple_spinner_item, statelist);
                    statedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    sp_storestate.setAdapter(statedataAdapter);
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
        RequestQueue rq = Volley.newRequestQueue(Addstore_Two.this);
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

                    citydataAdapter = new ArrayAdapter<String>(Addstore_Two.this,
                            android.R.layout.simple_spinner_item, citylist);
                    citydataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_storecity.setAdapter(citydataAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                    //    Toast.makeText(Profile.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(Profile.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();


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
        RequestQueue rq = Volley.newRequestQueue(Addstore_Two.this);
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

                    areadataAdapter = new ArrayAdapter<String>(Addstore_Two.this,
                            android.R.layout.simple_spinner_item, arealist);
                    areadataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_storearea.setAdapter(areadataAdapter);


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
                map.put("sid", sid);
                map.put("cid", cid);
                return map;
            }
        };
        rq.add(sr);
    }

}
