package xyz.gautamhans.locus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    String placeId, photoReference;
    int itemIndex, placePriceLevel;
    private Toast mToast;
    String placeName;
    String placeAddress;
    String placeContact;
    Uri placeWeblink;
    float placeRating;
    String finalImageUrl;

    private String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private String API_KEY = "&key=AIzaSyDDs4rr2v_ZQJ9_6qkFq5NG4B_aRCEe0o0";

    //Google API Client to get places details
    private GoogleApiClient mGoogleApiClient;

    //Views References
    ImageView iv_place_photo;
    TextView tv_place_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placedetails_layout);

        Intent intent = getIntent();
        if(intent!=null){
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

        if(mToast!=null) mToast.cancel();
        mToast = Toast.makeText(this, "Index: "+itemIndex +"\nPlace ID: " + placeId + "\nReference: "+photoReference,
                Toast.LENGTH_LONG);
        Log.i(String.valueOf(this), "Reference: " + finalImageUrl);
        mToast.show();

        loadData();
        loadViews();
    }

    private void loadData(){
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if(places.getStatus().isSuccess()){
                            final Place myPlace = places.get(0);
                            Log.i(String.valueOf(PlaceDetails.this),
                                    "Place found: " + myPlace.getName() + "\n Address: " + myPlace.getAddress());
                            placeName = (String) myPlace.getName();
                            placeAddress = (String) myPlace.getAddress();
                            placeContact = (String) myPlace.getPhoneNumber();
                            placePriceLevel = myPlace.getPriceLevel();
                            placeRating = myPlace.getRating();
                            placeWeblink = myPlace.getWebsiteUri();

                        } else {
                            Log.i(String.valueOf(PlaceDetails.this), String.valueOf(places.getStatus()));
                        }
                        places.release();
                    }
                });
    }

    private void loadViews(){
        iv_place_photo = (ImageView) findViewById(R.id.iv_place_photo);
        tv_place_name = (TextView) findViewById(R.id.tv_place_name_pd);
        Picasso.with(this).load(finalImageUrl)
                .resize(420,200).centerCrop().into(iv_place_photo);
        tv_place_name.setText(placeName);
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
