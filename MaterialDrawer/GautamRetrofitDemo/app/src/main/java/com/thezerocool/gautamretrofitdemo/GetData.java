package com.thezerocool.gautamretrofitdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by KH9275 on 21-03-2017.
 */
public class GetData extends Fragment {
    Retrofit retrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_get_data,container,false);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://gautams-161606.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<User>> call = apiInterface.getData();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                List<User> list = response.body();
                Log.d("NAME",list.get(0).getName());
                Log.d("PASSWORD", list.get(0).getPassword());
                List<Place> places = list.get(0).getPlaces();
                Log.d("FIRST PLACE", places.get(0).getName());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });

        return v;
    }
}
