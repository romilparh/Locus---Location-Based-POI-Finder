package com.thezerocool.gautamretrofitdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KH9275 on 21-03-2017.
 */
public class SetData extends Fragment {
    Retrofit retrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_data,container,false);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://gautams-161606.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        User user = new User();
        user.setName("ROMIL PARHWAL");
        Call<User> call = apiInterface.setData(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("WRITE", "SUCCESSFUL");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        return v;
    }
}
