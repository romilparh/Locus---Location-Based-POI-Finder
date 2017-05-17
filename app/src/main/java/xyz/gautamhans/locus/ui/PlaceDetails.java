package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.retrofit.ApiClientSavePlace;
import xyz.gautamhans.locus.retrofit.ApiInterfaceSavePlace;

/**
 * Created by Gautam on 10-Apr-17.
 */

public class PlaceDetails extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    String placeId, photoReference, placeName, placeAddress, placeContact, placeLatitude, placeLongitude;
    int itemIndex, placePriceLevel;
    String placeWeblink;
    String na_reference = "na";
    float placeRating;
    String finalImageUrl;
    //Views References
    RatingBar ratingBar;
    ImageView iv_place_photo;
    TextView tv_place_name, tv_address, tv_call_info, tv_website_info;
    List<xyz.gautamhans.locus.retrofit.pojos.Place> savePlaceList;
    private Toast mToast;
    private String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private String API_KEY = "&key=AIzaSyARJMoytbx68cOuuX1aIzhRrdN5uql90uY";
    //Google API Client to get places details
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placedetails_layout);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            itemIndex = extras.getInt("clickIndex", 0);
            placeId = extras.getString("placeId");
            photoReference = extras.getString("ref");
        }

        //Generating Image URL
        finalImageUrl = photoBaseUrl + photoReference + API_KEY;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, "Index: " + itemIndex + "\nPlace ID: " + placeId + "\nReference: " + photoReference,
                Toast.LENGTH_LONG);
        Log.i(String.valueOf(this), "Reference: " + finalImageUrl);
        mToast.show();

        tv_call_info = (TextView) findViewById(R.id.tv_call_info);
        tv_website_info = (TextView) findViewById(R.id.tv_website_info);

        loadData();
        loadViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void loadData() {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            Log.i(String.valueOf(PlaceDetails.this),
                                    "Place found: " + myPlace.getName() + "\n Address: " + myPlace.getAddress());
                            placeName = (String) myPlace.getName();
                            tv_place_name = (TextView) findViewById(R.id.tv_place_name_pd);
                            tv_place_name.setText(placeName);
                            placeAddress = (String) myPlace.getAddress();
                            tv_address = (TextView) findViewById(R.id.tv_address);
                            tv_address.setText(placeAddress);
                            placeContact = (String) myPlace.getPhoneNumber();
                            tv_call_info = (TextView) findViewById(R.id.tv_call_info);
                            tv_call_info.setText(placeContact);
                            placePriceLevel = myPlace.getPriceLevel();
                            placeRating = myPlace.getRating();
                            placeWeblink = String.valueOf(myPlace.getWebsiteUri());
                            tv_website_info = (TextView) findViewById(R.id.tv_website_info);
                            tv_website_info.setText(placeWeblink);
                            placeRating = myPlace.getRating();
                            ratingBar = (RatingBar) findViewById(R.id.rb_rating);
                            ratingBar.setRating(placeRating);
                            placeLatitude = String.valueOf(myPlace.getLatLng().latitude);
                            placeLongitude = String.valueOf(myPlace.getLatLng().longitude);
                            setIntents();
                        } else {
                            Log.i(String.valueOf(PlaceDetails.this), String.valueOf(places.getStatus()));
                        }
                        places.release();
                    }
                });
    }

    private void setIntents(){
       tv_website_info.setOnClickListener(this);
        tv_call_info.setOnClickListener(this);
    }

    private void loadViews() {
        iv_place_photo = (ImageView) findViewById(R.id.iv_place_photo);
        tv_place_name = (TextView) findViewById(R.id.tv_place_name_pd);
        if (photoReference == null) {
            iv_place_photo.setImageResource(R.drawable.defaultplace);
        } else {
            Picasso.with(this).load(finalImageUrl)
                    .resize(420, 200).centerCrop().into(iv_place_photo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pd_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_place:
                savePlace();
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                Toast.makeText(this, "Incorrect Item Toast", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void savePlace() {
        Log.d("address", placeAddress);
        Log.d("place name", placeName);
        Log.d("place id", placeId);
        Log.d("latitude", placeLatitude);
        Log.d("longitude", placeLongitude);
        try {
            Log.d("photoReference", "" + finalImageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String latitude = placeLatitude;
        String longitude = placeLongitude;
        if (photoReference == null) {
            photoReference = "na";
        } else {
            photoReference = finalImageUrl;
        }

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int id = sharedPref.getInt("userID", 0);

        Log.d("userID", String.valueOf(id));

        Retrofit retrofitSavePlace = ApiClientSavePlace.getClient();
        ApiInterfaceSavePlace apiInterfaceSavePlace = retrofitSavePlace.create(ApiInterfaceSavePlace.class);
        Call<List<xyz.gautamhans.locus.retrofit.pojos.Place>> savePlace =
                apiInterfaceSavePlace.savePlace(id, placeName, placeId, placeAddress, latitude, longitude, photoReference);


        savePlace.enqueue(new Callback<List<xyz.gautamhans.locus.retrofit.pojos.Place>>() {
            @Override
            public void onResponse(Call<List<xyz.gautamhans.locus.retrofit.pojos.Place>> call, Response<List<xyz.gautamhans.locus.retrofit.pojos.Place>> response) {
                savePlaceList = response.body();

                try {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(PlaceDetails.this, "Place saved.", Toast.LENGTH_LONG);
                    mToast.show();
                    Log.d("response: ", "placedetails: " + response.errorBody().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<xyz.gautamhans.locus.retrofit.pojos.Place>> call, Throwable throwable) {
                Log.d("place save error", "ERROR SAVING PLACE");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_website_info:
                if(placeWeblink!=null){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(placeWeblink));
                    startActivity(i);
                }
                break;
            case R.id.tv_call_info:
                if(placeContact!=null){
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+placeContact));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                break;
            default:
                Log.d("placedetails: ", "incorrect intent" );
        }
    }
}
