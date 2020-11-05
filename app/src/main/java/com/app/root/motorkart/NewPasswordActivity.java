package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.material.snackbar.Snackbar;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {
    PinView otp_fgt;
EditText fgtpwd_password;
Button btnn_fgtpwdsubmit;
ImageView fgt_back;
ProgressBar pb_submitfgtpassword;
    String newpassword_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/setNewResetPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        btnn_fgtpwdsubmit=findViewById(R.id.btnn_fgtpwdsubmit);
        fgtpwd_password=findViewById(R.id.fgtpwd_password);
        otp_fgt=findViewById(R.id.otp_fgt);
        fgt_back=findViewById(R.id.iv_fgtpwd_back);
        pb_submitfgtpassword=findViewById(R.id.pb_submitfgtpassword);

        fgt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnn_fgtpwdsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(otp_fgt.getText().toString().isEmpty()||fgtpwd_password.getText().toString().isEmpty()){
                    Snackbar.make(v,"Please fill all fields",Snackbar.LENGTH_LONG).show();

                }

                else {
                    pb_submitfgtpassword.setVisibility(View.VISIBLE);
                    btnn_fgtpwdsubmit.setEnabled(false);
                    RequestQueue rq= Volley.newRequestQueue(NewPasswordActivity.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, newpassword_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");

                                if(status.equals("success")){
                                    pb_submitfgtpassword.setVisibility(View.GONE);
                                    Intent intent=new Intent(NewPasswordActivity.this,LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                                else if(status.equals("wrong otp")){
                                    Snackbar snackbar=Snackbar.make(v,"Incorrect OTP",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    pb_submitfgtpassword.setVisibility(View.GONE);
                                    btnn_fgtpwdsubmit.setEnabled(true);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_submitfgtpassword.setVisibility(View.GONE);
                                Snackbar snackbar=Snackbar.make(v,"Something went wrong..Please try again later..",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                btnn_fgtpwdsubmit.setEnabled(true);

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar snackbar=Snackbar.make(v,"Check Internet Connection",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            btnn_fgtpwdsubmit.setEnabled(true);



                            //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("otp",otp_fgt.getText().toString());
                            map.put("newPassword",fgtpwd_password.getText().toString());
                            return map;
                        }
                    };
                    rq.add(sr);
                }

            }
        });
    }
}
