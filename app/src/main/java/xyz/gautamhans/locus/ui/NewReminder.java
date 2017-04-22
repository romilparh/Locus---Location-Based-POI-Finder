package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.db.DatabaseModel;

public class NewReminder extends AppCompatActivity {

    private static final int RADIUS_MAX = 500;
    private static final int DEFAULT_RADIUS = 50;
    int meters;
    DBHelper dbHelper;
    List<DatabaseModel> dbList;
    int PLACE_PICKER_REQUEST = 1;
    private TextView address_selected, radius_text;
    private EditText task_title, task_description;
    private ImageView chooseLocation;
    private SeekBar mRadius;
    private String address, placeID;
    private double mLatitude;
    private double mLongtitude;

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
        boolean error = false;

        if (TextUtils.isEmpty(title)) {
            task_title.setError(getString(R.string.title_error));
            error = true;
        }

        if (TextUtils.isEmpty(description)) {
            task_description.setError(getString(R.string.description_error));
            error = true;
        }

        if (TextUtils.isEmpty(String.valueOf(mLatitude))) {
            Toast.makeText(this, "You forgot to pick location", Toast.LENGTH_LONG).show();
            error = true;
        }

        if (TextUtils.isEmpty(String.valueOf(mLongtitude))) {
            Toast.makeText(this, "You forgot to pick location", Toast.LENGTH_LONG).show();
            error = true;
        }

        if (error) {
            return;
        }

        saveData();
//        setGeoFence();
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

        if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(NewReminder.this, Reminders.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
