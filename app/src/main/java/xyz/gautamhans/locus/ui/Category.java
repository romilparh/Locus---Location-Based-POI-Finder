package xyz.gautamhans.locus.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.retrofit.ApiClient;
import xyz.gautamhans.locus.retrofit.ApiClientSavePlace;
import xyz.gautamhans.locus.retrofit.ApiInterface;
import xyz.gautamhans.locus.retrofit.ApiInterfaceSavePlace;
import xyz.gautamhans.locus.retrofit.pojos.Example;
import xyz.gautamhans.locus.retrofit.pojos.Result;
import xyz.gautamhans.locus.retrofit.pojos.SavePlace;
import xyz.gautamhans.locus.ui.adapter.RVAdapter_CategoryDetails;

/**
 * Created by Gautam on 09-Apr-17.
 */

public class Category extends AppCompatActivity implements
        RVAdapter_CategoryDetails.ListItemClickListener {

    String type;
    int itemIndex = 0, radius = 15000;
    Double latitude, longitude;
    String category;
    List<Result> places;
    RecyclerView recyclerView;
    RVAdapter_CategoryDetails adapter;
    Toast mToast;
    String rankby = "distance";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        ActionBar actionBar = getSupportActionBar();
        // TODO (COMPLETED) : Fix the ActionBar disappearing
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            itemIndex = extras.getInt("clickIndex", 0);
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            Log.i(String.valueOf(this), "Lat: " + latitude + "\nLong: " + longitude);
        }

        // TODO (1) : find a better solution to this
        switch (itemIndex) {
            case 0:
                setTitle("ATMs");
                category = "atm";
                break;
            case 1:
                setTitle("Banks");
                category = "bank";
                break;
            case 2:
                setTitle("Hotels");
                category = "bar";
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
                setTitle("Travel Agencies");
                category = "travel_agency";
                break;
            case 10:
                setTitle("Movie Theaters");
                category = "movie_theater";
                break;
            case 11:
                setTitle("Airport");
                category = "airport";
                break;
            case 12:
                setTitle("Restaurants");
                category = "restaurant";
                break;
            case 13:
                setTitle("Gas Stations");
                category = "gas_station";
                break;
            case 14:
                setTitle("Schools");
                category = "school";
                break;
            case 15:
                setTitle("Universities");
                category = "university";
                break;
            default:
                setTitle("Respected Category Here");
        }

        //initializing the views for layout
        initViews();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_cat_details);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJson();
    }

    private void loadJson() {
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<Example> exampleCall = apiInterface.getNearbyPlaces
                (latitude + "," + longitude, rankby, category);

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                places = response.body().getResults();
                Log.d(String.valueOf(this), "Number of places received: " + places.size());
                setAdapter();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Log.d(String.valueOf(this), t.toString());
            }
        });

        Retrofit retrofitSave = ApiClientSavePlace.getClient();
        ApiInterfaceSavePlace apiInterfaceSave = retrofitSave.create(ApiInterfaceSavePlace.class);
        Call<List<SavePlace>> savedPlaces = apiInterfaceSave.getSavePlaces();
        savedPlaces.enqueue(new Callback<List<SavePlace>>() {
            @Override
            public void onResponse(Call<List<SavePlace>> call, Response<List<SavePlace>> response) {
                Log.d("in", "RESPONSE2");
                try {
                    Log.d("RESPONSE \nBody Size: ", response.body().size() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SavePlace>> call, Throwable throwable) {
                Log.d("RESPONSE", "FAILURE");
            }
        });
    }

    private void setAdapter() {
        adapter = new RVAdapter_CategoryDetails(this, places);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            if (item.getItemId() == android.R.id.home) {
                Intent actMain = new Intent(Category.this, MainActivity.class);
                startActivity(actMain);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Category.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onListItemClick(int clickedItemIndex, String place_id, String photoReference) {
        Log.i(String.valueOf(this), "Item #" + clickedItemIndex + "\nPlace ID:" + place_id);
        Intent intent = new Intent(Category.this, PlaceDetails.class);
        Bundle extras = new Bundle();
        extras.putInt("clickIndex", clickedItemIndex);
        extras.putString("placeId", place_id);
        extras.putString("ref", photoReference);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
