package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmPassword extends AppCompatActivity {
    PinView otp_change;
    EditText new_password,confirm_password;
    Button btnn_changepwdsubmit;
    ImageView iv_chpwdd_back;
    ProgressBar pb_submitchangepassword;
    String mobile;
    String newpwd_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/setForgotPwd";
    String verifyotp_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/verifyForgotpwdOtp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        btnn_changepwdsubmit=findViewById(R.id.btnn_changepwdsubmit);
        iv_chpwdd_back=findViewById(R.id.iv_chpwdd_back);
        pb_submitchangepassword=findViewById(R.id.pb_submitchangepassword);
        confirm_password=findViewById(R.id.confirm_password);
        new_password=findViewById(R.id.new_password);
        otp_change=findViewById(R.id.otp_change);
        mobile=getIntent().getStringExtra("change_mobileno");

        iv_chpwdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnn_changepwdsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(otp_change.getText().toString().isEmpty()||new_password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please fill all fields",Snackbar.LENGTH_LONG).show();

                }

                else if(!new_password.getText().toString().equals(confirm_password.getText().toString().trim())){
                    Snackbar.make(v,"Password can't match",Snackbar.LENGTH_LONG).show();
                }

                else {
                    pb_submitchangepassword.setVisibility(View.VISIBLE);
                    btnn_changepwdsubmit.setEnabled(false);
                    RequestQueue rq= Volley.newRequestQueue(ConfirmPassword.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, verifyotp_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");

                                if(status.equals("success")){

                                    newPasswordapi(v);


                                }
                                else if(status.equals("wrong otp")){
                                    Snackbar snackbar=Snackbar.make(v,"Incorrect OTP",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    pb_submitchangepassword.setVisibility(View.GONE);
                                    btnn_changepwdsubmit.setEnabled(true);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_submitchangepassword.setVisibility(View.GONE);
                                Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                btnn_changepwdsubmit.setEnabled(true);

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar snackbar=Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            btnn_changepwdsubmit.setEnabled(true);



                            //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("mobile",mobile);
                            map.put("otp",otp_change.getText().toString());
                            return map;
                        }
                    };
                    rq.add(sr);
                }

            }
        });
    }

    private void newPasswordapi(final View v) {
        RequestQueue rq= Volley.newRequestQueue(ConfirmPassword.this);
        StringRequest sr=new StringRequest(Request.Method.POST, newpwd_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if(status.equals("success")){
                        pb_submitchangepassword.setVisibility(View.GONE);

                        Intent intent=new Intent(ConfirmPassword.this,HomeScreeeen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else {
                        Snackbar snackbar=Snackbar.make(v,"Something went wrong",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        pb_submitchangepassword.setVisibility(View.GONE);
                        btnn_changepwdsubmit.setEnabled(true);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_submitchangepassword.setVisibility(View.GONE);
                    Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    btnn_changepwdsubmit.setEnabled(true);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar=Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT);
                snackbar.show();
                btnn_changepwdsubmit.setEnabled(true);



                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("mobile",mobile);
                map.put("password",confirm_password.getText().toString());
                return map;
            }
        };
        rq.add(sr);
    }
}
