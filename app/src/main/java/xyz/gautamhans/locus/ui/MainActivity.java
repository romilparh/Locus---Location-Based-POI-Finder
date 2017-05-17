package xyz.gautamhans.locus.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.background.UEC;
import xyz.gautamhans.locus.retrofit.ApiClientPlaces;
import xyz.gautamhans.locus.retrofit.ApiInterface;
import xyz.gautamhans.locus.retrofit.pojosplaces.Attractions;
import xyz.gautamhans.locus.ui.adapter.RVAdapter_PlaceCard;
import xyz.gautamhans.locus.ui.adapter.RVCat_Adapter;
import xyz.gautamhans.locus.retrofit.pojosplaces.*;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener, RVCat_Adapter.ListItemClickListener, View.OnClickListener, RVAdapter_PlaceCard.ListItemClickListener {

    //location user resolution specifier
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    //RecyclerView Variables
    public RecyclerView rv_cat, rv_places;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ProgressBar progressBar;
    boolean userExists;
    String longt, lat;
    //User information Navigation Drawer
    TextView userName, userEmail;
    String name, email, photoUrl;
    ImageView userPhoto;
    //List to inflate RecyclerView with ViewHolder
    private List<CategoryDetailsModel> categoryDetailsList;
    private List<PlaceCardDetails> placeCardDetails;
    //Get Longitude & Latitude using Places API
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 100;
    private long FASTEST_INTERVAL = 2000;
    //Toast for onClick on RVCat
    private Toast mToast;
    private String userIdToken;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView placeLoadErrorText;
    private Button refreshButton;
    SharedPreferences sharedPref;

    List<xyz.gautamhans.locus.retrofit.pojosplaces.Result> resultsList;

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        UEC uec = new UEC(sharedPref);
        uec.checkIfUserExists(new UEC.OnUserExistsCallback() {
            @Override
            public void onUserExists() {
                Log.d("USERSTATUS", String.valueOf(sharedPref.getInt("userID", 0)));
            }

            @Override
            public void onUserDoesNotExist() {
                Log.d("USERSTATUS", "FALSE:DOESNT EXIST");
                Log.d("USERSTATUS", String.valueOf(sharedPref.getInt("userID", 0)));
            }
        });

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        uec.saveUserInfo();
        Log.d("saveplace", "shared pref id: " + sharedPref.getInt("userID", 0));


        //  Declared a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                if (isFirstStart) {
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });

        t.run();

//        UserExistenceChecker userExistenceChecker = new UserExistenceChecker(sharedPref);
//        boolean userExists = userExistenceChecker.checkIfUserExist();
//
//        if(userExists){
//            Log.d("USERSTATUS", String.valueOf(sharedPref.getInt("userID", 0)));
//        } else{
//            int idReturned = userExistenceChecker.saveUserInfo();
//            Log.d("USERSTATUS", String.valueOf(sharedPref.getInt("userID", 0)));
//        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rv_cat = (RecyclerView) findViewById(R.id.rv_cat);
        rv_places = (RecyclerView) findViewById(R.id.rv_places);
        progressBar = (ProgressBar) findViewById(R.id.progressBarPlaces);
        placeLoadErrorText = (TextView) findViewById(R.id.placeLoadErrorText);
        refreshButton = (Button) findViewById(R.id.refresh_places_main);
        refreshButton.setOnClickListener(this);

        LinearLayoutManager llm_cat = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager llm_places = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        rv_cat.setLayoutManager(llm_cat);
        rv_places.setLayoutManager(llm_places);

        initializeDataCat();
//        initializeDataPlaces();
        initializeAdapter();
        initializeUserInfo();
    }

    private void initializeUserInfo() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        name = sharedPref.getString("userName", "");
        email = sharedPref.getString("userEmail", "");
        userIdToken = sharedPref.getString("userIdToken", "");
        photoUrl = sharedPref.getString("photoUrl", "");

        Log.d(String.valueOf(this), "ID Token: " + userIdToken);

        userName = (TextView) navHeader.findViewById(R.id.nav_user_name_tv);
        userEmail = (TextView) navHeader.findViewById(R.id.nav_user_email_tv);
        userPhoto = (ImageView) navHeader.findViewById(R.id.user_photoView);

        try {
            Picasso.with(this).load(photoUrl).into(userPhoto);
            userName.setText(name);
            userEmail.setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeDataCat() {
        categoryDetailsList = new ArrayList<>();
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.atm));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.bank));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.hotel));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.hospital));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.parks));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.police));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.shopping));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.store));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.train));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.cabs));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.theater));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.flight));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.restaurants));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.gas));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.school));
        categoryDetailsList.add(new CategoryDetailsModel(R.drawable.uni));
    }

    public void initializeDataPlaces(String lat, String longt) {
        String rankby = "distance";
        String attractions = "Attractions";
        Retrofit retrofit = ApiClientPlaces.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<Attractions> attractionsCall = apiInterface.getAttractions(Uri.encode(attractions),
                lat + "," + longt,
                rankby);

        progressBar.setVisibility(View.VISIBLE);

        attractionsCall.enqueue(new Callback<Attractions>() {
            @Override
            public void onResponse(Call<Attractions> call, Response<Attractions> response) {

                progressBar.setVisibility(View.GONE);

                Log.d("request url", ":" + call.request().url().toString());
                Log.d("response", ": " +new Gson().toJson(response));
                resultsList = response.body().getResults();
                Log.d(String.valueOf(this), "Number of places received: " + resultsList.size());
                setAdapter(resultsList);
            }

            @Override
            public void onFailure(Call<Attractions> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                placeLoadErrorText.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
                Log.d(String.valueOf(this), throwable.toString());
            }
        });
    }

    private void setAdapter(List<Result> resultsList) {
        RVAdapter_PlaceCard adapter = new RVAdapter_PlaceCard(resultsList, this, this);
        rv_places.setAdapter(adapter);
    }

    public void initializeAdapter() {
        RVCat_Adapter adapter = new RVCat_Adapter(categoryDetailsList, this);
        rv_cat.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //connects GoogleAPIClient
        mGoogleApiClient.connect();
    }

    public void settingsRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        if (doesUserHavePermission()) {
                            requestLocation();
                        } else {
                            askForPermissions();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsRequest(); //keep asking for location
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search) {
            Intent intent = new Intent(MainActivity.this, PlaceSearch.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_saved_places) {
            Intent i = new Intent(MainActivity.this, SavedPlaces.class);
            startActivity(i);
        } else if (id == R.id.nav_reminders) {
            Intent i = new Intent(MainActivity.this, Reminders.class);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_feedback) {
            sendEMAIL();
        } else if (id == R.id.nav_help) {
            Intent i=new Intent(this,FeedbackActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendEMAIL() {
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(new Uri.Builder().scheme("mailto").build())
                .putExtra(Intent.EXTRA_EMAIL, new String[]{ "Romil <romilparhwal007@gmail.com>" })
                .putExtra(Intent.EXTRA_SUBJECT, "Locus Feedback")
                .putExtra(Intent.EXTRA_TEXT, "Hello Locus Team, I would like to give feedback about your application.")
                ;

        ComponentName emailApp = intent.resolveActivity(getPackageManager());
        ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
        if (emailApp != null && !emailApp.equals(unsupportedAction))
            try {
                // Needed to customise the chooser dialog title since it might default to "Share with"
                // Note that the chooser will still be skipped if only one app is matched
                Intent chooser = Intent.createChooser(intent, "Send email with");
                startActivity(chooser);
                return;
            }
            catch (ActivityNotFoundException ignored) {
            }

        Toast
                .makeText(this, "Couldn't find an email app and account", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Prompts user to enable location
        settingsRequest();

        if (doesUserHavePermission()) {
            if (lat != null && longt != null)
                requestLocation();
        }
    }

    protected void requestLocation() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }

        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            Log.d("Debug", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            lat = String.valueOf(mCurrentLocation.getLatitude());
            longt = String.valueOf(mCurrentLocation.getLongitude());

//            Toast.makeText(this, "Latitude: " + lat + "\nLongitude: " + longt, Toast.LENGTH_LONG).show();

            Log.i(String.valueOf(this.getClass()), "Lat: " + lat + " Longt: " + longt);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentLatString", lat);
            editor.putString("currentLongString", longt);
            editor.putLong("currentLat", Double.doubleToLongBits(mCurrentLocation.getLatitude()));
            editor.putLong("currentLong", Double.doubleToLongBits(mCurrentLocation.getLongitude()));
            editor.apply();

            initializeDataPlaces(lat, longt);
        }
        startLocationUpdates();
    }

    private boolean doesUserHavePermission() {
        int result = this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    protected void startLocationUpdates() {
        //Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void askForPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mCurrentLocation != null) {
                        Log.d("Debug", "current location: " + mCurrentLocation.toString());
                        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                        lat = String.valueOf(mCurrentLocation.getLatitude());
                        longt = String.valueOf(mCurrentLocation.getLongitude());

                        if (mToast != null) {
                            mToast.cancel();
                        }

                        mToast = Toast.makeText(this, "Latitude: " + lat + "\nLongitude: " + longt, Toast.LENGTH_LONG);
                        mToast.show();

                        initializeDataPlaces(lat, longt);

                        Log.i(String.valueOf(this.getClass()), "Lat: " + lat + " Longt: " + longt);
                    }

                    mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(UPDATE_INTERVAL)
                            .setFastestInterval(FASTEST_INTERVAL);

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                } else {
                    askForPermissions();
                }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onListItemClick(int clickedItemIndex, String type) {
        try {
            Intent intent = new Intent(MainActivity.this, Category.class);
            Bundle extras = new Bundle();
            extras.putInt("clickIndex", clickedItemIndex);
            extras.putDouble("latitude", Double.parseDouble(lat));
            extras.putDouble("longitude", Double.parseDouble(longt));
            intent.putExtras(extras);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Network error.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh_places){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            initializeDataPlaces(sharedPref.getString("currentLatString", ""), sharedPref.getString("currentLongString", ""));
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex, String place_id, String photoReference) {
        Log.i(String.valueOf(this), "Item #" + clickedItemIndex + "\nPlace ID:" + place_id);
        Intent intent = new Intent(MainActivity.this, PlaceDetails.class);
        Bundle extras = new Bundle();
        extras.putInt("clickIndex", clickedItemIndex);
        extras.putString("placeId", place_id);
        extras.putString("ref", photoReference);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
