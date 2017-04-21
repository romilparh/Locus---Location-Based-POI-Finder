package xyz.gautamhans.locus.ui.loader;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Collections;

import xyz.gautamhans.locus.App;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.entity.StoredReminder;
import xyz.gautamhans.locus.ui.fragment.AppConfigs;
import xyz.gautamhans.locus.util.DistanceComparator;

import static java.security.AccessController.getContext;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class ReminderListLoader extends SimpleAsyncTaskLoader<ArrayList<StoredReminder>> {

    private EventEdited mReceiver;

    public ReminderListLoader(Context context) {
        super(context);
        mReceiver = new EventEdited();
        IntentFilter filter = new IntentFilter(App.ACTION_REMINDER_EDITED);
        getContext().registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        getContext().unregisterReceiver(mReceiver);
    }

    @Override
    public ArrayList<StoredReminder> loadInBackground() {

        ArrayList<StoredReminder> mList = DBHelper.getInstance(getContext()).getAllReminders();
        if (AppConfigs.getListOrderedByDistance(getContext())) {
            LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return TODO;
            }
            DistanceComparator.getInstance().setLastKnownLocation(manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            Collections.sort(mList, DistanceComparator.getInstance());
        } else {
            Collections.sort(mList);
        }

        return mList;
    }


    private class EventEdited extends BroadcastReceiver {


        public EventEdited() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(App.ACTION_REMINDER_EDITED);
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(this, filter);

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
    }


}
