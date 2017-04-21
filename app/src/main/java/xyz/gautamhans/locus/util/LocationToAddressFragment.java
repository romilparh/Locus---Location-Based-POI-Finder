package xyz.gautamhans.locus.util;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import xyz.gautamhans.locus.ui.BaseFragment;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class LocationToAddressFragment extends BaseFragment {

    public interface OnLocationConverteredListener {

        /**
         * Called when
         *
         * @param address
         * @param requested
         */
        public void onLocationConverted(String address, LatLng requested);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private class LocationToAddressTask extends AsyncTask<LatLng, Void, String> {
        @Override
        protected String doInBackground(LatLng... params) {
            return null;
        }
    }

}