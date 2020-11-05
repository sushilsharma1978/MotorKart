package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.mukesh.OtpView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {
Button btn_submit;
PinView otp_view;
ProgressBar pb_login3;
String mobileno;
ImageView iv_edit_mobile;
TextView tv_resendotp,tv_mobileno;
String checkotp_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/checkOtp";
String resendotp_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/resendOTPMsg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        btn_submit=findViewById(R.id.btn_submit);
        otp_view=findViewById(R.id.otp_view);
        pb_login3=findViewById(R.id.pb_login3);
        tv_resendotp=findViewById(R.id.tv_resendotp);
        iv_edit_mobile=findViewById(R.id.iv_edit_mobile);
        tv_mobileno=findViewById(R.id.tv_mobileno);

        mobileno=getIntent().getStringExtra("mobilenumber");
        tv_mobileno.setText(mobileno);

        tv_mobileno.setEnabled(false);


        iv_edit_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTPActivity.this,LoginMobile.class));
                finish();
            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
            if(otp_view.getText().toString().isEmpty()){
            Snackbar.make(v,"Enter OTP",Snackbar.LENGTH_LONG).show();

            }
        else {
    pb_login3.setVisibility(View.VISIBLE);
    btn_submit.setEnabled(false);
    RequestQueue rq= Volley.newRequestQueue(OTPActivity.this);
    StringRequest sr=new StringRequest(Request.Method.POST, checkotp_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject=new JSONObject(response);
                String status=jsonObject.getString("status");

                if(status.equals("success")){
                    pb_login3.setVisibility(View.GONE);
                  //  Toast.makeText(OTPActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(OTPActivity.this,SignupActivity.class);
                    intent.putExtra("signup_mobile",mobileno);
                    startActivity(intent);
                    finish();
                }
                else {
                    Snackbar snackbar=Snackbar.make(v,"Incorrect OTP",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    pb_login3.setVisibility(View.GONE);
                    btn_submit.setEnabled(true);


                }

            } catch (JSONException e) {
                e.printStackTrace();
                pb_login3.setVisibility(View.GONE);
                Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                snackbar.show();
                btn_submit.setEnabled(true);

            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Snackbar snackbar=Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT);
            snackbar.show();
            btn_submit.setEnabled(true);



            //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> map=new HashMap<>();
            map.put("mobile",mobileno);
            map.put("otp",otp_view.getText().toString());
            return map;
        }
    };
    rq.add(sr);
}

            }
        });

        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP(v);
            }
        });
    }

    private void resendOTP(final View v) {
        pb_login3.setVisibility(View.VISIBLE);
        tv_resendotp.setEnabled(false);
        RequestQueue rq= Volley.newRequestQueue(OTPActivity.this);
        StringRequest sr=new StringRequest(Request.Method.POST, resendotp_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if(status.equals("success")){
                        Snackbar snackbar=Snackbar.make(v,"OTP sent successfully..",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        pb_login3.setVisibility(View.GONE);
                        tv_resendotp.setEnabled(true);

                    }
                    else {
                        Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        pb_login3.setVisibility(View.GONE);
                        tv_resendotp.setEnabled(true);



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_login3.setVisibility(View.GONE);
                    Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    tv_resendotp.setEnabled(true);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar=Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT);
                snackbar.show();
                tv_resendotp.setEnabled(true);



                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("mobile",mobileno);
                return map;
            }
        };
        rq.add(sr);
    }
}
