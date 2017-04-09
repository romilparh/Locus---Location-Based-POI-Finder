package xyz.gautamhans.locus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Gautam on 09-Apr-17.
 */

public class Category extends AppCompatActivity {
    String type;
    int itemIndex = 0, radius = 10000;
    Double latitude, longitude;
    String category;
    List<Result> places;
    private RVAdapter_CategoryDetails adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent!=null) {
            Bundle extras = intent.getExtras();
            itemIndex = extras.getInt("clickIndex", 0);
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            Log.i(String.valueOf(this), "Lat: " + latitude + "\nLong: " + longitude);
        }

        // TODO (1) : find a better solution to this
        switch (itemIndex){
            case 0:
                setTitle("ATMs");
                category = "atm";
                break;
            case 1:
                setTitle("Banks");
                category = "bank";
                break;
            case 2:
                setTitle("Bars");
                category = "bars";
                break;
            case 3:
                setTitle("Hospital");
                category = "hospital";
                break;
            case 4:
                setTitle("Parks");
                category = "park";
                break;
            case 5:
                setTitle("Police");
                category = "police";
                break;
            case 6:
                setTitle("Shopping");
                category = "shopping";
                break;
            case 7:
                setTitle("Stores");
                category = "store";
                break;
            case 8:
                setTitle("Train Stations");
                category = "train_station";
                break;
            case 9:
                setTitle("Taxi Stations");
                category = "travel_agency";
                break;
            case 10:
                setTitle("Movie Theaters");
                category = "movie_theater";
                break;
            default:
                setTitle("Respected Category Here");
        }

        //initializing the views for layout
        initViews();
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_cat_details);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJson();
    }

    private void loadJson(){
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<Example> exampleCall = apiInterface.getNearbyPlaces
                (latitude+","+longitude, radius, category);

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                places = response.body().getResults();
                Log.d(String.valueOf(this), "Number of places received: " + places.size());
                adapter = new RVAdapter_CategoryDetails(places);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(String.valueOf(this), t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home) {
            Intent actMain = new Intent(Category.this, MainActivity.class);
        }
                return super.onOptionsItemSelected(item);
    }
}
