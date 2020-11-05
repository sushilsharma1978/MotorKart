package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
Button register_customer;
ProgressBar pb_signup;
String mobilee;
EditText ed_name,ed_email,ed_mobile,ed_password;
String register_url="http://demo.digitalsolutionsplanet.com/motorkart/api/register_android/registerdata";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        register_customer=findViewById(R.id.register_customer);
        ed_name=findViewById(R.id.ed_name);
        ed_email=findViewById(R.id.ed_email);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_password=findViewById(R.id.ed_password);
        pb_signup=findViewById(R.id.pb_signup);
mobilee=getIntent().getStringExtra("signup_mobile");
        ed_mobile.setText(mobilee);

        register_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_name.getText().toString().isEmpty() &&
                ed_email.getText().toString().isEmpty() && ed_mobile.getText().toString().isEmpty() &&
                ed_password.getText().toString().isEmpty()){
                    ed_name.setError("Enter name");
                    ed_email.setError("Enter email");
                    ed_mobile.setError("Enter mobile");
                    ed_password.setError("Enter password");

                }
                else if(ed_name.getText().toString().isEmpty()){
                    ed_name.setError("Enter name");

                }
                else if(ed_email.getText().toString().isEmpty()
                       ){
                    ed_email.setError("Enter email");

                }
                else if(ed_mobile.getText().toString().isEmpty()){
                    ed_mobile.setError("Enter mobile");

                }
                else if(ed_password.getText().toString().isEmpty()){
                    ed_password.setError("Enter password");

                }
                else {
                    pb_signup.setVisibility(View.VISIBLE);
                    register_customer.setEnabled(false);

                    RequestQueue rq= Volley.newRequestQueue(SignupActivity.this);
                    StringRequest sr=new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("success")){
                                Toast.makeText(SignupActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                finish();
                                pb_signup.setVisibility(View.GONE);

                            }
                            else {
                                Toast.makeText(SignupActivity.this, "Something went wrong..Please try again later..", Toast.LENGTH_SHORT).show();
                                pb_signup.setVisibility(View.GONE);
                                register_customer.setEnabled(true);

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pb_signup.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();
                            register_customer.setEnabled(true);
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<>();
                            map.put("fname",ed_name.getText().toString());
                            map.put("email",ed_email.getText().toString());
                            map.put("mobile",ed_mobile.getText().toString());
                            map.put("password",ed_password.getText().toString());
                            return map;
                        }
                    };
                    rq.add(sr);
                }


            }
        });






    }
}
