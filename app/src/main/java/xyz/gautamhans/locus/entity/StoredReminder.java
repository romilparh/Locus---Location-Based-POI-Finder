package xyz.gautamhans.locus.entity;

import android.support.annotation.NonNull;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class StoredReminder implements Comparable<StoredReminder> {

    public static final int FEATURE_FLAG_MUTE = 0x00000001;
    public static final float DEFAULT_RADIUS = 50;

    private long id;
    private String mNote, placeId;
    private Double latitude, longitude;

    private long mDateCreated;

    public StoredReminder(long id, String mNote, String placeId, Double latitude, Double longitude, long mDateCreated){

    }

    @Override
    public int compareTo(@NonNull StoredReminder o) {
        return (int) (o.mDateCreated - mDateCreated);
    }
}
