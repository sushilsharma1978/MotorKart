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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.StoreAdapter;
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

public class BookBikeService extends AppCompatActivity {
Spinner spinner_category,spinner_store;
Button btn_bookservice;
String service_for;
EditText ed_remarks;
ImageView iv_book_bike;
//ProgressBar pb_book;
    ProgressDialog pb_book;
TextView tv_time,tv_date;
BottomNavigationView btnav_view6;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
String centerId,storeId,cid,vid;
List<String> categorieslist=new ArrayList<>();
List<String> categorieslistid=new ArrayList<>();
List<String> storeidlist=new ArrayList<>();
List<String> storelist=new ArrayList<>();
    private Calendar cal;
    private int day;
    private int month;
    String token;
    private int year;
    String storeurl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getCateg";
    String allstores_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getAllStores";
    String newservicebook_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/bookAservice";
    Calendar myCalendar;

    String partstorebyId_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreById";
    String serviceBook_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/stoteUserService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bike_service);

        spinner_category=findViewById(R.id.spinner_category);
        spinner_store=findViewById(R.id.spinner_store);
        btn_bookservice=findViewById(R.id.btn_bookservice);
        ed_remarks=findViewById(R.id.ed_remarks);
      //  pb_book=findViewById(R.id.pb_book);
        iv_book_bike=findViewById(R.id.iv_book_bike);
        btnav_view6=findViewById(R.id.btnav_view6);
        tv_date=findViewById(R.id.tv_date);
        tv_time=findViewById(R.id.tv_time);
        iv_book_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myCalendar = Calendar.getInstance();
        pb_book=new ProgressDialog(BookBikeService.this);

        vid=getIntent().getStringExtra("veh_idd");
        sp_login=getSharedPreferences("LOGINDETAILS",MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");

        token=sp_login.getString("SAVETOKEN","");
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookBikeService.this,
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


        loadStores();

        tv_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookBikeService.this, new TimePickerDialog.OnTimeSetListener() {
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

                    RequestQueue rq= Volley.newRequestQueue(BookBikeService.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, newservicebook_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.equals("success")){
                                    pb_book.dismiss();
                                    sendFCMPush();
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
                            map.put("vid",vid);

                            return map;
                        }
                    };
                    rq.add(sr);
                }



            }
        });

        btnav_view6.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



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
        RequestQueue rq= Volley.newRequestQueue(BookBikeService.this);
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


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookBikeService.this,
                            android.R.layout.simple_spinner_item, storelist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_store.setAdapter(dataAdapter);
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

    private void updateLabel() {
        String myFormat = "yyyy-mm-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void getCategoriesList() {
        categorieslist.clear();
        categorieslist.add("Select category");
        categorieslistid.clear();
        categorieslistid.add("0");
        RequestQueue rq= Volley.newRequestQueue(BookBikeService.this);
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


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookBikeService.this,
                            android.R.layout.simple_spinner_item, categorieslist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_category.setAdapter(dataAdapter);
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

    private void sendFCMPush() {

         final String SERVER_KEY = "AAAAs5yB5Jg:APA91bEFr3QkOiAm2FF7fZj7q2CUJn4a-2mdj0S3jvsWmsROoBXyg21G2usGmn997vjnivHui_INuF2zTI-siEQSzpA0-MZnZ4RGUqmu3c5yJsYGTz8O0nO5KibiPo0nJlggEcwLDK9m";

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;
        final JSONArray jsonArray=new JSONArray();

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", "Service has been booked successfully");
            objData.put("title", "Motorkart");
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("title", "Motorkart");
            dataobjData.put("text", "Service has been booked successfully");

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("msg", "onResponse111111: "+response.toString() );
                      //  Toast.makeText(BookBikeService.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("msg", "onResponse1111112: "+error.toString() );

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsObjRequest);
    }
  /*  public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            btnDate.setText(ConverterDate.ConvertDate(year, month + 1, day));
        }
    }*/

}
