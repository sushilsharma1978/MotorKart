package com.app.root.motorkart;

import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("updateAvtar")
    Call<JsonElement> insert_profile(@Part MultipartBody.Part file,
                                           @Part("CID") RequestBody user_id);

    @Multipart
    @POST("insertStoreImage")
    Call<JsonElement> insert_storeimage(@Part MultipartBody.Part file,
                                     @Part("storeId") RequestBody storeId);
}
