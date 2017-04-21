package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import xyz.gautamhans.locus.R;

/**
 * Created by Gautam on 19-Apr-17.
 */

public class PlaceSearch extends AppCompatActivity {

    CardView cv;
    View fragment;

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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fragment.performClick();
//            }
//        }, 1000);


        //PlaceAutoComplete Search Implementation
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlaceSearch.this, MainActivity.class));
    }
}
