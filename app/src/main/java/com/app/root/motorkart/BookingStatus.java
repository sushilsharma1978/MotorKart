package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.adapter.BookingStatusAdapter;
import com.app.root.motorkart.adapter.VehicleAdapter;
import com.app.root.motorkart.interfaces.DeleteBooking;
import com.app.root.motorkart.modelclass.BookingStatusModel;
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

public class BookingStatus extends AppCompatActivity implements DeleteBooking {
ImageView iv_booking;
BottomNavigationView btnav_view8;
ProgressBar pb_bookingstatus;
RecyclerView rv_bookingstatus;
int mMenuId;
Button btnn_login_status;
LinearLayout ll_status;
    AlertDialog.Builder builder;
    BookingStatusAdapter bookingStatusAdapter;
List<BookingStatusModel> modelList=new ArrayList<>();
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;
    String cid,token;
    Button iv_addbooking;
    String bookingstatus="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getUserCurrentServices";
    String cancelbk_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/cancelBooking";
String checklogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);
        iv_booking=findViewById(R.id.iv_booking);
        btnav_view8=findViewById(R.id.btnav_view8);
        pb_bookingstatus=findViewById(R.id.pb_bookingstatus);
        rv_bookingstatus=findViewById(R.id.rv_bookingstatus);
        iv_addbooking=findViewById(R.id.iv_addbooking);
        btnn_login_status=findViewById(R.id.btnn_login_status);
        ll_status=findViewById(R.id.ll_status);

        sp_login=getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login=sp_login.edit();
        cid=sp_login.getString("cid","");
        checklogin=sp_login.getString("save_login","");
        token=sp_login.getString("SAVETOKEN","");
        Log.e("msg", "onCreateCIDDD: "+cid);

        if(checklogin.equals("false")){
            btnn_login_status.setVisibility(View.VISIBLE);
            ll_status.setVisibility(View.GONE);
        }
        else {
            ll_status.setVisibility(View.VISIBLE);
            btnn_login_status.setVisibility(View.GONE);
        }

        iv_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnn_login_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BookingStatus.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        /*try{
            mMenuId = Integer.parseInt(getIntent().getStringExtra("nav_item"));
            MenuItem menuItem = btnav_view8.getMenu().getItem(1);
            boolean isChecked = menuItem.getItemId() == mMenuId;
            menuItem.setChecked(isChecked);
        }
        catch (Exception e){}*/

        getServices();

        btnav_view8.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        iv_addbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingStatus.this,NoBookingStatus.class));
            }
        });


    }

    private void getServices() {
        modelList.clear();
        pb_bookingstatus.setVisibility(View.VISIBLE);
            RequestQueue rq= Volley.newRequestQueue(BookingStatus.this);
            StringRequest sr=new StringRequest(Request.Method.POST, bookingstatus, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");

                        if(status.equals("success")){
                            JSONArray jsonArray=jsonObject.getJSONArray("resultFirst");
                            Log.e("msg", "BookingResponse: "+jsonArray.length() );

                            if(jsonArray.length()==0){
                                //Toast.makeText(BookingStatus.this, "No bookings", Toast.LENGTH_SHORT).show();
                                iv_addbooking.setVisibility(View.VISIBLE);
                                rv_bookingstatus.setVisibility(View.GONE);
                                pb_bookingstatus.setVisibility(View.GONE);
                            }
                            else {
                                for(int x=0;x<jsonArray.length();x++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(x);


                                    BookingStatusModel bookingStatusModel=new BookingStatusModel(jsonObject1.getString("id"),
                                            jsonObject1.getString("created_on"),jsonObject1.getString("message"),
                                            jsonObject1.getString("status"),jsonObject1.getString("fname"),
                                            jsonObject1.getString("lname"),jsonObject1.getString("registration_num"),
                                            jsonObject1.getString("name"));
                                    modelList.add(bookingStatusModel);

                                }
                                rv_bookingstatus.setLayoutManager(new LinearLayoutManager(BookingStatus.this,RecyclerView.VERTICAL,false));
                                bookingStatusAdapter=new BookingStatusAdapter(BookingStatus.this,modelList,BookingStatus.this);
                                rv_bookingstatus.setAdapter(bookingStatusAdapter);
                                pb_bookingstatus.setVisibility(View.GONE);


                            }


                        }
                        else {
                           // Toast.makeText(BookingStatus.this, "No bookings", Toast.LENGTH_SHORT).show();
                            pb_bookingstatus.setVisibility(View.GONE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pb_bookingstatus.setVisibility(View.GONE);
                        //Toast.makeText(BookingStatus.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pb_bookingstatus.setVisibility(View.GONE);
                    Toast.makeText(BookingStatus.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    map.put("CID",cid);
                    Log.e("msg", "getParamsCIODDD: "+cid );
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
                    Intent intent2=new Intent(getApplicationContext(),Profile.class);
                    intent2.putExtra("nav_item",String.valueOf(item.getItemId()));
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void deletbooking(final String did) {

        builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to cancel booking?") .setTitle("Cancel Booking");

        //Setting message manually and performing action on button click

        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pb_bookingstatus.setVisibility(View.VISIBLE);

                        RequestQueue rq= Volley.newRequestQueue(BookingStatus.this);
                        StringRequest sr=new StringRequest(Request.Method.POST, cancelbk_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    String status=jsonObject.getString("status");

                                    if(status.equals("success")){
                                        sendFCMPush();
                                        getServices();
                                    }
                                    else if(status.equals("error")){
                                        pb_bookingstatus.setVisibility(View.GONE);
                                        rv_bookingstatus.setVisibility(View.GONE);
                                        Toast.makeText(BookingStatus.this, "Something went wrong..Please try again later..", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pb_bookingstatus.setVisibility(View.GONE);
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pb_bookingstatus.setVisibility(View.GONE);
                                Toast.makeText(BookingStatus.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map=new HashMap<>();
                                map.put("id",did);
                                return map;
                            }
                        };
                        rq.add(sr);
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
        alert.setTitle("Cancel Booking");
        alert.show();

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

            objData.put("body", "Service has been cancelled successfully");
            objData.put("title", "Motorkart");
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("title", "Motorkart");
            dataobjData.put("text", "Service has been cancelled successfully");

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
}
