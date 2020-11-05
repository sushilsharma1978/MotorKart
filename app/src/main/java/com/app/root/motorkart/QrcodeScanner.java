package com.app.root.motorkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QrcodeScanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String s1;
    String scanned_storenum;
String checkstore_url="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/checkStoreAfterScanning";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        try {
            s1 = getIntent().getStringExtra("nav_item");

        } catch (Exception e) {

        }


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QrcodeScanner.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scanned_storenum=result.getText();

checkStore(scanned_storenum);

                        Log.e("msg", "ScannedNUM"+ result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void checkStore(final String scanned_storenum) {
        RequestQueue rq= Volley.newRequestQueue(QrcodeScanner.this);
        StringRequest srq=new StringRequest(StringRequest.Method.POST, checkstore_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String storeNumber=jsonObject.getString("storeNumber");
                    if(storeNumber.equals("0")){
                        Toast.makeText(QrcodeScanner.this, "QR code not avaialble", Toast.LENGTH_SHORT).show();
                   finish();
                    }
                    else {
                        Intent intent=new Intent(QrcodeScanner.this,PartStore_Location.class);
                        intent.putExtra("nav_item",s1);
                        intent.putExtra("store_num",scanned_storenum);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(WashingPoints.this, "Check Internet Connection"+error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("storeNumber",scanned_storenum);
                return map;
            }
        };
        rq.add(srq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}
