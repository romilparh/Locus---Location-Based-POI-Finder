package xyz.gautamhans.locus;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
,com.google.android.gms.location.LocationListener, RVCat_Adapter.ListItemClickListener{

    //List to inflate RecyclerView with ViewHolder
    private List<CategoryDetails> categoryDetailsList;
    private List<PlaceCardDetails> placeCardDetails;

    private static final int PERMISSION_REQUEST_CODE = 100;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    //Get Longitude & Latitude using Places API
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 100;
    private long FASTEST_INTERVAL = 2000;
    String longt, lat;

    //Toast for onClick on RVCat
    private Toast mToast;

    //RecyclerView Variables
    public RecyclerView rv_cat, rv_places;

    //User information Navigation Drawer
    TextView userName, userEmail;
    String name, email, photoUrl;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,    drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rv_cat = (RecyclerView) findViewById(R.id.rv_cat);
        rv_places = (RecyclerView) findViewById(R.id.rv_places);

        LinearLayoutManager llm_cat = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager llm_places = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        rv_cat.setLayoutManager(llm_cat);
        rv_places.setLayoutManager(llm_places);

        initializeDataCat();
        initializeDataPlaces();
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
        photoUrl = sharedPref.getString("photoUrl", "");

        userName = (TextView) navHeader.findViewById(R.id.nav_user_name_tv);
        userEmail = (TextView) navHeader.findViewById(R.id.nav_user_email_tv);
        userPhoto = (ImageView) navHeader.findViewById(R.id.user_photoView);

        try{
            Picasso.with(this).load(photoUrl).into(userPhoto);
            userName.setText(name);
            userEmail.setText(email);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void initializeDataCat(){
        categoryDetailsList = new ArrayList<>();
        categoryDetailsList.add(new CategoryDetails(R.drawable.atm));
        categoryDetailsList.add(new CategoryDetails(R.drawable.bank));
        categoryDetailsList.add(new CategoryDetails(R.drawable.bar));
        categoryDetailsList.add(new CategoryDetails(R.drawable.hospital));
        categoryDetailsList.add(new CategoryDetails(R.drawable.parks));
        categoryDetailsList.add(new CategoryDetails(R.drawable.police));
        categoryDetailsList.add(new CategoryDetails(R.drawable.shopping));
        categoryDetailsList.add(new CategoryDetails(R.drawable.store));
        categoryDetailsList.add(new CategoryDetails(R.drawable.train));
        categoryDetailsList.add(new CategoryDetails(R.drawable.taxi));
        categoryDetailsList.add(new CategoryDetails(R.drawable.theater));
    }

    public void initializeDataPlaces(){
        placeCardDetails = new ArrayList<>();
        placeCardDetails.add(new PlaceCardDetails(R.drawable.brewmaster, 4.0, "The Brewmaster", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.curo_mall, 3.9, "Curo Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.pvr_mbdmall, 4.5, "PVR MBD Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.hotel_cabbana, 4.6, "Hotel Cabbana", "Phagwara-Jalandhar Highway"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.brewmaster, 4.0, "The Brewmaster", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.curo_mall, 3.9, "Curo Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.pvr_mbdmall, 4.5, "PVR MBD Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.hotel_cabbana, 4.6, "Hotel Cabbana", "Phagwara-Jalandhar Highway"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.brewmaster, 4.0, "The Brewmaster", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.curo_mall, 3.9, "Curo Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.pvr_mbdmall, 4.5, "PVR MBD Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.hotel_cabbana, 4.6, "Hotel Cabbana", "Phagwara-Jalandhar Highway"));
    }

    public void initializeAdapter(){
        RVCat_Adapter adapter = new RVCat_Adapter(categoryDetailsList, this);
        rv_cat.setAdapter(adapter);
        RVAdapter_PlaceCard adapter_placeCard = new RVAdapter_PlaceCard(placeCardDetails);
        rv_places.setAdapter(adapter_placeCard);
        }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_saved_places) {

        } else if (id == R.id.nav_reminders) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

//            Toast.makeText(this, "Latitude: " +lat+"\nLongitude: " +longt, Toast.LENGTH_LONG).show();

            Log.i(String.valueOf(this.getClass()), "Lat: " + lat + " Longt: " + longt);
        }
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        //Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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
        Intent intent = new Intent(MainActivity.this, Category.class);
        Bundle extras = new Bundle();
        extras.putInt("clickIndex", clickedItemIndex);
        extras.putDouble("latitude", Double.parseDouble(lat));
        extras.putDouble("longitude", Double.parseDouble(longt));
        intent.putExtras(extras);
        startActivity(intent);
        }
    }

