package com.thezerocool.gautamretrofitdemo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
/**
 * Created by KH9275 on 21-03-2017.
 */

public interface ApiInterface {
    @GET("user")
    Call<ArrayList<User>> getData();

    @POST("user")
    Call<User> setData(@Body User user);
}
