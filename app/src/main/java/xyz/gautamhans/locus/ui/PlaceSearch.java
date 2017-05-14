package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import xyz.gautamhans.locus.R;

/**
 * Created by Gautam on 19-Apr-17.
 */

public class PlaceSearch extends AppCompatActivity {

    CardView cv;
    View fragment;
    int radius;
    SharedPreferences sharedPref;
    LatLngBounds latLngBounds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);

        //Hiding SupportActionBar
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cv = (CardView) findViewById(R.id.cv_search);
        fragment = findViewById(R.id.place_autocomplete_fragment);

        double radius = 20000d;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Double latitude = Double.longBitsToDouble(sharedPref.getLong("currentLat", 0));
        Double longitude = Double.longBitsToDouble(sharedPref.getLong("currentLong", 0));

        if(latitude!=0.0 && longitude!=0.0) {
            LatLng center = new LatLng(latitude, longitude);
            latLngBounds = toBounds(center, radius);
        }

        //PlaceAutoComplete Search Implementation
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(latLngBounds);

        //run autocompletefragment automatically on activity creation
        final View root = autocompleteFragment.getView();
        root.post(new Runnable() {
            @Override
            public void run() {
                root.findViewById(R.id.place_autocomplete_search_input)
                        .performClick();
            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(String.valueOf(this), "Place: " + place.getName() + "\nID: " + place.getId());
                String placeId = place.getId();
                try {
                    Intent intent = new Intent(PlaceSearch.this, PlaceDetailsFromSearch.class);
                    Bundle extras = new Bundle();
                    extras.putString("placeID", placeId);
                    intent.putExtras(extras);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i(String.valueOf(this), "An error occurred: " + status);
            }
        });
    }

    private LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlaceSearch.this, MainActivity.class));
    }
}
