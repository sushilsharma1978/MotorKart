package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
EditText fgt_email;
Button btn_forgotp;
ImageView iv_fgt_back;
ProgressBar pb_fgtpassword;
    String fgtpwdotp_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/resetPasswordMessageForOtp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fgt_email=findViewById(R.id.fgt_email);
        btn_forgotp=findViewById(R.id.btnn_forgotp);
        pb_fgtpassword=findViewById(R.id.pb_fgtpassword);
        iv_fgt_back=findViewById(R.id.iv_fgt_back);

        btn_forgotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(fgt_email.getText().toString().isEmpty()){
                    fgt_email.setError("Enter mobile");
                    fgt_email.requestFocus();
                }
                else {
                    pb_fgtpassword.setVisibility(View.VISIBLE);
                    btn_forgotp.setEnabled(false);
                    RequestQueue rq= Volley.newRequestQueue(ForgotPassword.this);
                    StringRequest srq=new StringRequest(Request.Method.POST, fgtpwdotp_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.equals("success")){
                                    Snackbar snackbar=Snackbar.make(v,"OTP sent successfully..",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    pb_fgtpassword.setVisibility(View.GONE);

                                    Intent intent=new Intent(ForgotPassword.this,NewPasswordActivity.class);
                                    startActivity(intent);

                                }
                                else if(status.equals("no email")){
                                    Snackbar.make(v,"Enter correct mobile",Snackbar.LENGTH_SHORT).show();
                                    pb_fgtpassword.setVisibility(View.GONE);
                                    btn_forgotp.setEnabled(true);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_fgtpassword.setVisibility(View.GONE);
                                btn_forgotp.setEnabled(true);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("msg", "onErrorResponse: "+error.toString() );
                            pb_fgtpassword.setVisibility(View.GONE);
                            btn_forgotp.setEnabled(true);

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("mobile",fgt_email.getText().toString().trim());
                            return map;
                        }
                    };
                    rq.add(srq);
                    srq.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }
            }
        });

        iv_fgt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
