package com.task.login.Retrofit;

import com.google.gson.JsonObject;
import com.task.login.Model.MyPojo;
import com.task.login.Model.Product;
import com.task.login.Model.ProductList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("djadmin/get_product_details")
    Call<List<MyPojo>> getProductDetails();

    @POST("customer_registration/")
    Call updateOrder(@Body JsonObject bodyParameters);

}
