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

public class ChangePassword extends AppCompatActivity {
    EditText change_mobile;
    Button btnn_chngepswd;
    ImageView iv_chpwd_back;
    ProgressBar pb_chngepassword;
    String changepwdotp_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/forgotPasswordOtp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        change_mobile=findViewById(R.id.change_mobile);
        btnn_chngepswd=findViewById(R.id.btnn_chngepswd);
        iv_chpwd_back=findViewById(R.id.iv_chpwd_back);
        pb_chngepassword=findViewById(R.id.pb_chngepassword);

        btnn_chngepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(change_mobile.getText().toString().isEmpty()){
                    change_mobile.setError("Enter mobile");
                    change_mobile.requestFocus();
                }
                else {
                    pb_chngepassword.setVisibility(View.VISIBLE);
                    btnn_chngepswd.setEnabled(false);
                    RequestQueue rq= Volley.newRequestQueue(ChangePassword.this);
                    StringRequest srq=new StringRequest(Request.Method.POST, changepwdotp_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.equals("success")){
                                    Snackbar snackbar=Snackbar.make(v,"OTP sent successfully..",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    pb_chngepassword.setVisibility(View.GONE);

                                    Intent intent=new Intent(ChangePassword.this,ConfirmPassword.class);
                                    intent.putExtra("change_mobileno",change_mobile.getText().toString().trim());
                                    startActivity(intent);

                                }
                                else if(status.equals("no email")){
                                    Snackbar.make(v,"Enter correct mobile",Snackbar.LENGTH_SHORT).show();
                                    pb_chngepassword.setVisibility(View.GONE);
                                    btnn_chngepswd.setEnabled(true);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_chngepassword.setVisibility(View.GONE);
                                btnn_chngepswd.setEnabled(true);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("msg", "onErrorResponse: "+error.toString() );
                            pb_chngepassword.setVisibility(View.GONE);
                            btnn_chngepswd.setEnabled(true);

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("mobile",change_mobile.getText().toString().trim());
                            return map;
                        }
                    };
                    rq.add(srq);
                    srq.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }
            }
        });

        iv_chpwd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
