package xyz.gautamhans.locus.ui;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.db.DatabaseModel;
import xyz.gautamhans.locus.service.UserTransitionIntentService;

public class NewReminder extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RADIUS_MAX = 500;
    private static final int DEFAULT_RADIUS = 50;
    int meters;
    DBHelper dbHelper;
    List<DatabaseModel> dbList;
    int PLACE_PICKER_REQUEST = 1;
    private long mExistingID = -1L;
    private TextView address_selected, radius_text;
    private EditText task_title, task_description;
    private ImageView chooseLocation;
    private SeekBar mRadius;
    private String address, placeID;
    private double mLatitude;
    private double mLongtitude;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_ui);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dbList = new ArrayList<DatabaseModel>();
        task_title = (EditText) findViewById(R.id.task_title_tv);
        task_description = (EditText) findViewById(R.id.task_desc_tv);
        address_selected = (TextView) findViewById(R.id.address_selected_tv);
        radius_text = (TextView) findViewById(R.id.radius_text);
        mRadius = (SeekBar) findViewById(R.id.radius_slider);
        mRadius.setMax(RADIUS_MAX);
        updateRadiusText();

        mRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRadiusText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void pickPlace(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                address = String.valueOf(place.getAddress());
                mLatitude = place.getLatLng().latitude;
                mLongtitude = place.getLatLng().longitude;
                Log.i(String.valueOf(this), "Latitude: " + mLatitude + "\nLongitude: " + mLongtitude);
                placeID = place.getId();
                address_selected.setText(address);
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    void onSaveButtonClicked(View view) {
        String title = task_title.getText().toString();
        String description = task_title.getText().toString();
        String address = address_selected.getText().toString();
        boolean error = false;

        if (TextUtils.isEmpty(title)) {
            task_title.setError(getString(R.string.title_error));
            error = true;
        }

        if (TextUtils.isEmpty(description)) {
            task_description.setError(getString(R.string.description_error));
            error = true;
        }

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_LONG).show();
            error = true;
        }

        if (error) {
            return;
        }

        setGeoFence();
    }

    private void saveData() {
        String title = task_title.getText().toString();
        String description = task_description.getText().toString();
        String address = address_selected.getText().toString();
        int radius = meters;
        dbHelper = new DBHelper(NewReminder.this);
        dbHelper.insertIntoDB(title, description, mLongtitude, mLatitude, placeID, address, radius);
        Toast.makeText(this, "Reminder Saved.", Toast.LENGTH_LONG).show();
        task_title.setText("");
        task_description.setText("");
        address_selected.setText("");
        startActivity(new Intent(NewReminder.this, Reminders.class));
    }

    private void setGeoFence() {
        setUpApi();
    }

    private void setUpApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void updateRadiusText() {
        meters = mRadius.getProgress();
        this.radius_text.setText(getString(R.string.radius_in_m, meters));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewReminder.this, Reminders.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(NewReminder.this, Reminders.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(String.valueOf(this), "API Client Connected.");

        /* let's save the reminder to the database first so that
        we can get the id from the database for the reminder */

        String title = task_title.getText().toString();
        String description = task_description.getText().toString();
        int radius = mRadius.getProgress();
        Double latitude = mLatitude;
        Double longitude = mLongtitude;
        String address = address_selected.getText().toString();
        this.setResult(Activity.RESULT_OK);
        new SaveReminderTask(this, mExistingID, title, description, placeID, radius, latitude, longitude, address).execute();
        this.finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(String.valueOf(this), "connection suspended!");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(String.valueOf(this), "connection failed!" + connectionResult);
    }

    private class SaveReminderTask extends AsyncTask<Void, Void, Integer> {

        private String title, description, placeID, address;
        private int radius;
        private long _id;
        private Double latitude, longitude;
        private Context mContext;

        public SaveReminderTask(Context context, long id, String title, String description, String placeID, int radius, Double latitude, Double longitude, String address) {
            this.title = title;
            this._id = id;
            this.description = description;
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
            this.placeID = placeID;
            this.address = address;
            this.mContext = context.getApplicationContext();
        }

        private static final int ACTION_REMINDER_FAILED = -1;
        private static final int ACTION_REMINDER_CREATED = 0;
        private static final int ACTION_REMINDER_UPDATED = 1;

        @Override
        protected Integer doInBackground(Void... params) {
            int actionPerformed = 0;
            long id;
            id = DBHelper.getInstance(mContext).insertIntoDB(title, description, longitude, latitude, placeID, address, radius);
                actionPerformed = ACTION_REMINDER_CREATED;
            Log.d(String.valueOf(this), "ID Returned after Insertion: " +id);

                Geofence cinemaFence = new Geofence.Builder()
                        .setRequestId(String.valueOf(id))
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .setCircularRegion(latitude, longitude, radius)
                        .build();

                GeofencingRequest request = new GeofencingRequest.Builder()
                        .addGeofence(cinemaFence)
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .build();

                // Create an intent pointing to the IntentService
                Intent intent = new Intent(mContext, UserTransitionIntentService.class);
                PendingIntent pi = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return 1;
                }

                LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, request, pi);
            return actionPerformed;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if (integer == ACTION_REMINDER_CREATED)
                Toast.makeText(mContext, "Reminder saved.", Toast.LENGTH_LONG).show();

            mGoogleApiClient.disconnect();
            mContext = null;


        }
    }
}
