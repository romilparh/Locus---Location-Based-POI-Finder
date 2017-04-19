package xyz.gautamhans.locus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Created by Gautam on 19-Apr-17.
 */

public class PlaceDetailsFromSearch extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String placeId, placeName, placeAddress, placeContact;
    String placeWeblink;
    float placeRating;
    //Views References
    RatingBar ratingBar;
    TextView tv_place_name, tv_address, tv_call_info, tv_website_info;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details_search);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            placeId = extras.getString("placeID");
        }

        //views
        tv_place_name = (TextView) findViewById(R.id.tv_place_name_pd);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_call_info = (TextView) findViewById(R.id.tv_call_info);
        tv_website_info = (TextView) findViewById(R.id.tv_website_info);
        ratingBar = (RatingBar) findViewById(R.id.rb_rating);

        //Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        loadData();
    }

    private void loadData() {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            Log.i(String.valueOf(PlaceDetailsFromSearch.this),
                                    "Place found: " + myPlace.getName() + "\n Address: " + myPlace.getAddress());
                            placeName = (String) myPlace.getName();
                            tv_place_name.setText(placeName);
                            placeAddress = (String) myPlace.getAddress();
                            tv_address.setText(placeAddress);
                            placeContact = (String) myPlace.getPhoneNumber();
                            tv_call_info.setText(placeContact);
                            placeRating = myPlace.getRating();
                            placeWeblink = String.valueOf(myPlace.getWebsiteUri());
                            tv_website_info.setText(placeWeblink);
                            placeRating = myPlace.getRating();
                            ratingBar.setRating(placeRating);

                        } else {
                            Log.i(String.valueOf(PlaceDetailsFromSearch.this), String.valueOf(places.getStatus()));
                        }
                        places.release();
                    }
                });
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
                Toast.makeText(this, "Save Toast", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "Incorrect Item Toast", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
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
}
