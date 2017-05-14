package xyz.gautamhans.locus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.db.model.DatabaseModel;
import xyz.gautamhans.locus.ui.adapter.RVAdapter_Reminders;

/**
 * Created by Gautam on 18-Apr-17.
 */

public class Reminders extends AppCompatActivity implements RVAdapter_Reminders.ReminderListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    DBHelper dbHelper;
    List<DatabaseModel> dbList;
    RecyclerView mRecyclerView;
    GoogleApiClient mGoogleApiClient;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private long mDeletedReminderID = -1;
    public int position;

    // Nav drawer vars
    private DrawerLayout drawer;
    private View navHeader;
    private NavigationView navigationView;

    String name, email, photoUrl;
    private String userIdToken;
    TextView userName, userEmail;
    ImageView userPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,    drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            getSupportActionBar().setTitle("Reminders");
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbHelper = new DBHelper(this);
        dbList = new ArrayList<DatabaseModel>();
        dbList = dbHelper.getDataFromDB();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_reminders);
        mRecyclerView.setHasFixedSize(true);

        // using a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specifying an adapter
        mAdapter = new RVAdapter_Reminders(this, dbList, this);
        mRecyclerView.setAdapter(mAdapter);

        initializeUserInfo();

    }

    public void addReminder(View view) {
        Intent intent = new Intent(Reminders.this, NewReminder.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reminders.this, MainActivity.class);
        startActivity(intent);
    }

    private void onReminderDelete(int position) {
        int id = dbList.get(position).getId();
        onDeleteReminder(id, position);
    }

    private void onDeleteReminder(long id, int position) {
        mDeletedReminderID = id;
        int mPosition = position;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

        }
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            deleteReminder(id, position);
        }
    }

    private void deleteReminder(long id, int position) {
        int mPosition = position;
        List<String> ids = new ArrayList<>();
        ids.add(String.valueOf(id));
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, ids);
        mGoogleApiClient.disconnect();
        Log.d(String.valueOf(this), "deleting reminder " + id);
        finallyDeleteReminder((int) id, position);
    }

    private void finallyDeleteReminder(int id, int position) {
        dbHelper.deleteARow(id);
        dbList.remove(position);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Reminder deleted.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOptionsClick(View v, final int reminderIndex) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.reminder_rv_menu);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_delete_option:
                        position = reminderIndex;
                        onReminderDelete(reminderIndex);
                        break;
                }
                return true;
            }
        });
        popup.show();
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

    @Override
    public void onCardClick(View v, int position) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(String.valueOf(this), "connected");
        deleteReminder(mDeletedReminderID, position);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(String.valueOf(this), "connection suspended!");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(String.valueOf(this), "connection failed!");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_saved_places) {
            Intent i=new Intent(this, SavedPlaces.class);
            startActivity(i);
        } else if (id == R.id.nav_reminders) {
            Context context = getApplicationContext();
            CharSequence text = "Already on Reminders Page";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(Reminders.this, SettingsActivity.class));
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
