package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class LoginMobile extends AppCompatActivity {
EditText login2_mobile;
Button btnn_proceed,btnn_home;
ProgressBar pb_login2;
Dialog dialog;
String insertmobile_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/insertmobile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        login2_mobile=findViewById(R.id.login2_mobile);
        btnn_proceed=findViewById(R.id.btnn_proceed);
        pb_login2=findViewById(R.id.pb_login2);
        btnn_home=findViewById(R.id.btnn_home);

        dialog=new Dialog(LoginMobile.this);
        dialog.setContentView(R.layout.dialog_mobile_error);

        btnn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(login2_mobile.getText().toString().isEmpty()){
                    login2_mobile.setError("Enter mobile");
                }
                else {
                    pb_login2.setVisibility(View.VISIBLE);
                  btnn_proceed.setEnabled(false);
                    RequestQueue rq= Volley.newRequestQueue(LoginMobile.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, insertmobile_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");

                                if(status.equals("success")){
                                    pb_login2.setVisibility(View.GONE);
                                    Snackbar snackbar=Snackbar.make(v,"OTP sent successfully..",Snackbar.LENGTH_SHORT);
                                    snackbar.show();

                                    Intent intent=new Intent(LoginMobile.this,OTPActivity.class);
                                    intent.putExtra("mobilenumber",login2_mobile.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                else if(status.equals("fail")){
                                    dialog.show();
                                    Button btn_ok=dialog.findViewById(R.id.btn_ok);

                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
//                                    Snackbar snackbar=Snackbar.make(v,"Mobile is already registered..",Snackbar.LENGTH_SHORT);
//                                    snackbar.show();
                                    pb_login2.setVisibility(View.GONE);
                                    btnn_proceed.setEnabled(true);


                                }
                                else {
                                    Snackbar snackbar=Snackbar.make(v,"Mobile already registered",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    pb_login2.setVisibility(View.GONE);
                                    btnn_proceed.setEnabled(true);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pb_login2.setVisibility(View.GONE);
                                Snackbar snackbar=Snackbar.make(v,"Mobile already registered",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                btnn_proceed.setEnabled(true);

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar snackbar=Snackbar.make(v,"Check Internet Connection"+error.toString(),Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            pb_login2.setVisibility(View.GONE);
                            btnn_proceed.setEnabled(true);



                            //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("mobile",login2_mobile.getText().toString());
                            return map;
                        }
                    };
                    rq.add(sr);
                }
            }
        });

        btnn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMobile.this,HomeScreeeen.class));
                finish();
            }
        });
    }
}
