package com.task.login.Retrofit;

import com.task.login.Model.MyPojo;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @GET("djadmin/get_product_details")
    Call<List<MyPojo>> getProductDetails();

    @POST("customer_registration/")
    Call<Void> userRegistration(@Body JSONObject orderData);

    @POST("customer_login/")
    Call<Void> userLogin(@Body JSONObject bodyParameters);

    @GET("customer_logout")
    Call<Void> logout();

}
