package xyz.gautamhans.locus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

/**
 * Created by Gautam on 10-Apr-17.
 */

public class PlaceDetails extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String placeId, photoReference, placeName, placeAddress, placeContact;
    int itemIndex, placePriceLevel;
    String placeWeblink;
    String na_reference = "na";
    float placeRating;
    String finalImageUrl;
    //Views References
    RatingBar ratingBar;
    ImageView iv_place_photo;
    TextView tv_place_name, tv_address, tv_call_info, tv_website_info;
    private Toast mToast;
    private String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private String API_KEY = "&key=AIzaSyARJMoytbx68cOuuX1aIzhRrdN5uql90uY";
    //Google API Client to get places details
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placedetails_layout);

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

        loadData();
        loadViews();
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

                        } else {
                            Log.i(String.valueOf(PlaceDetails.this), String.valueOf(places.getStatus()));
                        }
                        places.release();
                    }
                });
    }

    private void loadViews() {
        iv_place_photo = (ImageView) findViewById(R.id.iv_place_photo);
        tv_place_name = (TextView) findViewById(R.id.tv_place_name_pd);

        if (photoReference == "na") {
            iv_place_photo.setImageResource(R.drawable.atm_stock);
        } else {
            Picasso.with(this).load(finalImageUrl)
                    .resize(420, 200).centerCrop().into(iv_place_photo);
        }

//        Log.i(String.valueOf(this), "Place name from loadView: " + placeName);
//
//        tv_place_name.setText(placeName);
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
