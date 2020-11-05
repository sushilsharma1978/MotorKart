package com.app.root.motorkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddStore extends AppCompatActivity {
Button btn_nextstore;
ImageView iv_addstore,iv_store_photo;
Spinner sp_storecategory;
EditText ed_storename,ed_storeno,ed_add_email,ed_addphone;
EditText tv_addtime,tv_storecharges;
    String categoryid;
    List<String> categorieslist=new ArrayList<>();
    List<String> categorieslistid=new ArrayList<>();
    private File imagefile = null;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String storeurl="http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getCateg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        btn_nextstore=findViewById(R.id.btn_nextstore);
        iv_addstore=findViewById(R.id.iv_addstore);
        sp_storecategory=findViewById(R.id.sp_storecategory);
        ed_storename=findViewById(R.id.ed_storename);
        ed_storeno=findViewById(R.id.ed_storeno);
        ed_add_email=findViewById(R.id.ed_add_email);
        ed_addphone=findViewById(R.id.ed_addphone);
        tv_addtime=findViewById(R.id.tv_addtime);
        tv_storecharges=findViewById(R.id.tv_storecharges);
        iv_store_photo=findViewById(R.id.iv_store_photo);

        getCategoriesList();


        iv_addstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_nextstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_storename.getText().toString().isEmpty() || ed_storeno.getText().toString().isEmpty() ||
                ed_add_email.getText().toString().isEmpty() || ed_addphone.getText().toString().isEmpty()
                || tv_storecharges.getText().toString().isEmpty()){
                    Snackbar.make(view,"Enter mandatory fields",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(AddStore.this,Addstore_Two.class);
                    intent.putExtra("storename",ed_storename.getText().toString());
                    intent.putExtra("storeno",ed_storeno.getText().toString());
                    intent.putExtra("storeemail",ed_add_email.getText().toString());
                    intent.putExtra("storephone",ed_addphone.getText().toString());
                    intent.putExtra("storetime",tv_addtime.getText().toString());
                    intent.putExtra("storecharges",tv_storecharges.getText().toString());
                    intent.putExtra("storecatid",categoryid);
                    intent.putExtra("store_photo",imagefile);
                    startActivity(intent);


                }
            }
        });


        sp_storecategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryid = categorieslistid.get(position);
                Log.e("msg", "onCreatecid111: "+categoryid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_store_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddStore.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddStore.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(AddStore.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddStore.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && ActivityCompat.shouldShowRequestPermissionRationale(AddStore.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(AddStore.this,
                                    Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(AddStore.this, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA},
                                REQUEST_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    selectImage();
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddStore.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                //  performCrop(tempUri);



             /*   CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);*/

// start cropping activity for pre-acquired image saved on the device
                CropImage.activity(tempUri)
                        .start(this);


                Log.d("msg", "Camerapath: " + imagefile);
            } catch (Exception e) {
            }


        } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                //performCrop(selectedImage);

          /*      CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);*/

// start cropping activity for pre-acquired image saved on the device
                CropImage.activity(selectedImage).start(this);
               /* Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath= cursor.getString(columnIndex);
                    //  String image_name=imgPath.substring(imgPath.lastIndexOf("/")+1);
                    imagefile=new File(imgPath);
                    upload_image.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                    Log.d("msg", "Camerapath1: "+imagefile);


                }*/
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();
//                imgPath=getRealPathFromURI(resultUri);
                // String image_name=imgPath.substring(imgPath.lastIndexOf("/")+1);
                imagefile = new File(resultUri.getPath());
                Log.e("msg", "onActivityResultoooo: " + imagefile);
                Log.e("msg", "onActivityResultoooo1: " + result);

                iv_store_photo.setImageURI(result.getUri());


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private void getCategoriesList() {
        categorieslist.clear();
        categorieslistid.clear();
        RequestQueue rq= Volley.newRequestQueue(AddStore.this);
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


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddStore.this,
                            android.R.layout.simple_spinner_item, categorieslist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_storecategory.setAdapter(dataAdapter);


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
}
