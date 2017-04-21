package xyz.gautamhans.locus.util;

import android.location.Location;

import java.util.Comparator;

import xyz.gautamhans.locus.entity.StoredReminder;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class DistanceComparator implements Comparator<StoredReminder> {

    private static DistanceComparator sInstance;

    public static DistanceComparator getInstance() {
        if (sInstance == null) {
            sInstance = new DistanceComparator();
        }
        return sInstance;
    }

    private Location mLastLocation;


    @Override
    public int compare(StoredReminder lhs, StoredReminder rhs) {
        Location lloc = lhs.makeLocation();
        Location rloc = rhs.makeLocation();
        return (int) (lloc.distanceTo(mLastLocation) -
                rloc.distanceTo(mLastLocation));
    }


    public void setLastKnownLocation(Location lastKnownLocation) {
        this.mLastLocation = lastKnownLocation;
    }
}

