package xyz.gautamhans.locus.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import xyz.gautamhans.locus.Notifier;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.db.DatabaseModel;

/**
 * Created by Gautam on 24-Apr-17.
 */

public class UserTransitionIntentService extends IntentService {

    public static final String EXTRA_FEATURES = "xyz.gautamhans.locus.service.features";
    private static final String TAG = "UserTransitionService";

    public UserTransitionIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        if (event.hasError()) {
            Log.e(TAG, "ERROR in Geofence! " + event.getErrorCode());
        } else {
            handleGeofences(event.getTriggeringLocation(), event.getTriggeringGeofences(), event.getGeofenceTransition(), intent.getExtras().getInt(EXTRA_FEATURES, -1));
        }
    }

    private void handleGeofences(Location location, List<Geofence> geoFences, int transition, int features) {
        for (Geofence fence : geoFences) {
            DatabaseModel reminder = DBHelper.getInstance(this).getReminder(Long.valueOf(fence.getRequestId()));
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER){
                Notifier.notifyForReminder(this, reminder);
            }
            Log.d(TAG, "Handled reminder: " + reminder);
        }
    }
}
