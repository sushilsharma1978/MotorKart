package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.BrandAdapter;
import com.app.root.motorkart.adapter.ModelAdapter;
import com.app.root.motorkart.adapter.StoreAdapter;
import com.app.root.motorkart.modelclass.BrandModel;
import com.app.root.motorkart.modelclass.Modelmodel;
import com.app.root.motorkart.modelclass.StoreModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookServicefromStore extends AppCompatActivity {
    Spinner spinner_category,spinner_store,spinner_vehicle,spinner_model;
    Button btn_bookservice;
    String service_for,vehicle;
    EditText ed_remarks;
    ImageView iv_book_bike;
    //ProgressBar pb_book;
    ProgressDialog pb_book;
    TextView tv_time,tv_date;
    BottomNavigationView btnav_view6;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String centerId,storeId,cid,brandid,servicecenter_name;
    List<String> categorieslist=new ArrayList<>();
    List<String> categorieslistid=new ArrayList<>();
    List<String> storeidlist=new ArrayList<>();
    List<String> storelist=new ArrayList<>();
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    List<String> vehiclelist=new ArrayList<>();
    List<String> vehiclelistid=new ArrayList<>();
    List<String> modellistid=new ArrayList<>();
    List<String> modellist=new ArrayList<>();
    String storeurl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getCateg";
    String allstores_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllStores";
    String newservicebook_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/bookAservice";
    String myvehicles="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getVechiles";
    String mybrands="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getBrandsByCid";
    String model_url="http://demo.digitalsolutionsplanet.com/motorkart/api/register_android/VehicleModel";
    ArrayAdapter<String> vehicledataAdapter,storedataAdapter,modelAdapter;
    Calendar myCalendar;
    String modelid,modelname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_servicefrom_store);


        spinner_category=findViewById(R.id.spinner_category6);
        spinner_store=findViewById(R.id.spinner_store6);
        spinner_vehicle=findViewById(R.id.spinner_vehiclee6);
        btn_bookservice=findViewById(R.id.btn_bookservice6);
        spinner_model=findViewById(R.id.spinner_model);
        ed_remarks=findViewById(R.id.ed_remarks6);
        //  pb_book=findViewById(R.id.pb_book);
        iv_book_bike=findViewById(R.id.iv_book_bike6);
        btnav_view6=findViewById(R.id.btnav_view66);
        tv_date=findViewById(R.id.tv_date6);
        tv_time=findViewById(R.id.tv_time6);
        iv_book_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myCalendar = Calendar.getInstance();
        pb_book=new ProgressDialog(BookServicefromStore.this);



        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");

        Log.e("msg", "onCreatecid: "+cid);


        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);


      /*  Calendar c = Calendar.getInstance();
        int min = c.get(Calendar.MINUTE);
        int hour=c.get(Calendar.HOUR);
        tv_time.setText(String.valueOf(hour)+":"+String.valueOf(min)+":00");


        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date_n);*/


        // getCategoriesList();

        categorieslist.clear();
        categorieslist.add("Four Wheeler");
        categorieslist.add("Two Wheeler");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookServicefromStore.this,
                android.R.layout.simple_spinner_item, categorieslist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(dataAdapter);

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service_for = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getVehicles();

        servicecenter_name=getIntent().getStringExtra("servicecntr_name");
        Log.e("msg", "SERVICECENTERNAME: "+servicecenter_name );

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter myAdap = (ArrayAdapter) spinner_vehicle.getAdapter();
                int spinnerPosition = myAdap.getPosition(servicecenter_name);
                spinner_vehicle.setSelection(spinnerPosition);
            }
        },500);*/



        spinner_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandid = vehiclelistid.get(position);
                getModels(brandid);
                Log.e("msg", "onCreatecid2111: "+brandid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelid = modellistid.get(position);
                Log.e("msg", "onCreateciddddd: "+modelid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        loadStores();

        tv_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookServicefromStore.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.SECOND, 0);
                        tv_time.setText(selectedHour + ":" + selectedMinute+ ":" +"00");

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


       /* final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };*/


        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog();

              /*  DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");*/

                   /* final Calendar c = Calendar.getInstance();
                new DatePickerDialog(BookBikeService.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/



            }
        });

        spinner_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeId=storeidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_bookservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(ed_remarks.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please fill all fields",Snackbar.LENGTH_LONG).show();
                }
                else {
                    pb_book.setMessage("Loading..");
                    pb_book.setCancelable(false);
                    pb_book.setCanceledOnTouchOutside(false);
                    pb_book.show();

                    RequestQueue rq= Volley.newRequestQueue(BookServicefromStore.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, newservicebook_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.equals("success")){
                                    pb_book.dismiss();

                                    Snackbar.make(v,"Service booked successfully",Snackbar.LENGTH_LONG).show();
                                    ed_remarks.setText("");
                                    tv_date.setText("");
                                    tv_time.setText("");
                                    spinner_category.setSelection(0);
                                    spinner_store.setSelection(0);
                                }
                                else {
                                    pb_book.dismiss();


                                    Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_book.dismiss();


                                // Toast.makeText(BookBikeService.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pb_book.dismiss();


                            //  Toast.makeText(BookBikeService.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                            Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_LONG).show();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("storeId",storeId);
                            map.put("details",ed_remarks.getText().toString());
                            map.put("for",spinner_category.getSelectedItem().toString());
                            map.put("date",tv_date.getText().toString());
                            map.put("time",tv_time.getText().toString());
                            map.put("CID",cid);
                            map.put("vid",brandid);
                            map.put("modelid",modelid);

                            return map;
                        }
                    };
                    rq.add(sr);
                }



            }
        });

        btnav_view6.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void getModels(final String brandid) {
        modellist.clear();
        modellistid.clear();
        RequestQueue rq= Volley.newRequestQueue(BookServicefromStore.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, model_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                        modellist.add(jsonObject1.getString("brand_name"));
                        modellistid.add(jsonObject1.getString("brand_id"));
                        Log.e("msg", "onCreatecid21111: "+modellist.get(x));
                    }


                    modelAdapter = new ArrayAdapter<String>(BookServicefromStore.this,
                            android.R.layout.simple_spinner_item, modellist);
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_model.setAdapter(modelAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                   // pb_brands.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(BookServicefromStore.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

               // pb_brands.setVisibility(View.GONE);


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("brand_id",brandid);
                return map;
            }
        };
        rq.add(srq);
    }

    public void DateDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                int month=monthOfYear+1;
                tv_date.setText(year+"-"+month+"-"+dayOfMonth);

            }};

        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();

    }

    private void loadStores() {
        storelist.clear();
        storeidlist.clear();
        RequestQueue rq= Volley.newRequestQueue(BookServicefromStore.this);
        StringRequest sr=new StringRequest(Request.Method.GET, allstores_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("msg", "StoreResponse: "+response.toString() );
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);
                        storelist.add( jsonObject1.getString("storeName"));
                        storeidlist.add( jsonObject1.getString("sid"));
                    }


                     storedataAdapter = new ArrayAdapter<String>(BookServicefromStore.this,
                            android.R.layout.simple_spinner_item, storelist);
                    storedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_store.setAdapter(storedataAdapter);

                    try{
                        ArrayAdapter adapter = (ArrayAdapter) spinner_store.getAdapter();
                        int spinnerPosition = adapter.getPosition(servicecenter_name);
                        Log.e("msg", "SERVICECENTERNAME12: "+spinnerPosition);
                        spinner_store.setSelection(spinnerPosition);
                    }
                    catch (Exception e){
                        Log.e("msg", "SERVICECENTERNAME1: "+e.toString() );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(BookBikeService.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(BookBikeService.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
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
                    Intent intent2=new Intent(getApplicationContext(),Profile.class);
                    intent2.putExtra("nav_item",String.valueOf(item.getItemId()));
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    private void getVehicles() {
        vehiclelist.clear();
        vehiclelistid.clear();
        RequestQueue rq= Volley.newRequestQueue(BookServicefromStore.this);
        StringRequest sr=new StringRequest(Request.Method.POST, mybrands, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if(status.equals("success")){
                        JSONArray jsonArray=jsonObject.getJSONArray("brands");
                        for(int x=0;x<jsonArray.length();x++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(x);

                            vehiclelist.add(jsonObject1.getString("brandName"));
                            vehiclelistid.add( jsonObject1.getString("brand_id"));

                        }
                        vehicledataAdapter = new ArrayAdapter<String>(BookServicefromStore.this,
                                android.R.layout.simple_spinner_item, vehiclelist);
                        vehicledataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_vehicle.setAdapter(vehicledataAdapter);



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
                map.put("cid",cid);
                return map;
            }
        };
        rq.add(sr);
    }

}
