package xyz.gautamhans.locus.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.retrofit.ApiClientSavePlace;
import xyz.gautamhans.locus.retrofit.ApiInterfaceSavePlace;

/**
 * Created by Gautam on 19-Apr-17.
 */

public class PlaceDetailsFromSearch extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    String placeId, placeName, placeAddress, placeContact, placeLatitude, placeLongitude;
    String  photoReference = "na";
    String placeWeblink;
    float placeRating;
    //Views References
    RatingBar ratingBar;
    TextView tv_place_name, tv_address, tv_call_info, tv_website_info;
    ImageView iv_place_photo;
    List<xyz.gautamhans.locus.retrofit.pojos.Place> savePlaceList;
    Toast mToast;
    private CardView cv_view_on_map;
    private RelativeLayout rl_view_on_map;
    private Button bt_view_on_map;

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
        iv_place_photo = (ImageView) findViewById(R.id.iv_place_photo);


        cv_view_on_map = (CardView) findViewById(R.id.cv_view_on_map);
        rl_view_on_map = (RelativeLayout) findViewById(R.id.rl_view_on_map);
        bt_view_on_map = (Button) findViewById(R.id.bt_view_on_map);
        cv_view_on_map.setOnClickListener(this);
        rl_view_on_map.setOnClickListener(this);
        bt_view_on_map.setOnClickListener(this);

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
                            iv_place_photo.setImageResource(R.drawable.defaultplace);
                            placeLatitude = String.valueOf(myPlace.getLatLng().latitude);
                            placeLongitude = String.valueOf(myPlace.getLatLng().longitude);
                            setIntents();
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_place:
                savePlace();
                break;

            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePlace() {
        Log.d("address", placeAddress);
        Log.d("place name", placeName);
        Log.d("place id", placeId);
        Log.d("latitude", placeLatitude);
        Log.d("longitude", placeLongitude);

        String latitude = placeLatitude;
        String longitude = placeLongitude;

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
                    mToast = Toast.makeText(PlaceDetailsFromSearch.this, "Place saved.", Toast.LENGTH_LONG);
                    mToast.show();
                    Log.d("response: ", "placedetails: " + response.errorBody().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<xyz.gautamhans.locus.retrofit.pojos.Place>> call, Throwable throwable) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PlaceDetailsFromSearch.this, "Place saved.", Toast.LENGTH_LONG);
                mToast.show();
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

    private void setIntents(){
        tv_website_info.setOnClickListener(this);
        tv_call_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_website_info:
                if(placeWeblink!=null){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(placeWeblink));
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.tv_call_info:
                if(placeContact!=null){
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+placeContact));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                break;

            case R.id.cv_view_on_map:
                openMaps();
                break;

            case R.id.rl_view_on_map:
                openMaps();
                break;

            case R.id.bt_view_on_map:
                openMaps();
                break;

            default:
                Log.d("placedetails: ", "incorrect intent" );
        }
    }

    private void openMaps(){
        if(placeLatitude!=null && placeLongitude!=null){
            Float placeLatitudeFloat = Float.parseFloat(placeLatitude);
            Float placeLongitudeFloat = Float.parseFloat(placeLongitude);
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", placeLatitudeFloat, placeLongitudeFloat, placeName);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException ex)
            {
                try
                {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                }
                catch(ActivityNotFoundException innerEx)
                {
                    Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
