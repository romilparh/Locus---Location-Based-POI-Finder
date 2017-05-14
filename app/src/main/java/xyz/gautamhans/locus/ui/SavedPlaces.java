package xyz.gautamhans.locus.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.background.UEC;
import xyz.gautamhans.locus.retrofit.ApiClientSavePlace;
import xyz.gautamhans.locus.retrofit.ApiInterfaceSavePlace;
import xyz.gautamhans.locus.retrofit.pojos.Place;
import xyz.gautamhans.locus.ui.adapter.RVAdapter_SavedPlaces;

public class SavedPlaces extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String name, email, photoUrl;
    TextView userName, userEmail;
    ImageView userPhoto;
    SharedPreferences sharedPref;
    RecyclerView recyclerView;
    //Nav drawer vars
    private DrawerLayout drawer;
    private View navHeader;
    private NavigationView navigationView;
    private String userIdToken;

    List<Place> savedPlacesList;
    RVAdapter_SavedPlaces adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places_ui);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            getSupportActionBar().setTitle("Saved Places");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeUserInfo();
        initViews();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_saved_places);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
        );
        recyclerView.setLayoutManager(layoutManager);
        loadJson();
    }

    private void loadJson() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int id = sharedPref.getInt("userID", 0);

        if (id != 0) {
            Retrofit retrofitSavedPlaces = ApiClientSavePlace.getClient();
            ApiInterfaceSavePlace apiInterfaceSavePlace =
                    retrofitSavedPlaces.create(ApiInterfaceSavePlace.class);

            Call<List<Place>> savedPlaces = apiInterfaceSavePlace.getSavedPlaces(id);
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            savedPlaces.enqueue(new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    savedPlacesList = response.body();
                    setAdapter();
                }

                @Override
                public void onFailure(Call<List<Place>> call, Throwable throwable) {

                }
            });
        }

    }


    private void setAdapter(){
        adapter = new RVAdapter_SavedPlaces(this, savedPlacesList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savedplace_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.refresh_places:
                initViews();
                break;
            default:
                Toast.makeText(this, "Wassup", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_saved_places) {
            Context context = getApplicationContext();
            CharSequence text = "Already on Saved Places Page";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (id == R.id.nav_reminders) {
            Intent intent = new Intent(this, Reminders.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
