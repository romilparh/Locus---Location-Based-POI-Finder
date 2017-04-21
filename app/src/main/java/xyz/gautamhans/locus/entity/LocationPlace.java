package xyz.gautamhans.locus.entity;

import android.content.Context;
import android.location.Location;

import java.io.Serializable;

import xyz.gautamhans.locus.Revisitor;

/**
 * Created by Gautam on 21-Apr-17.
 */

//it contains the location name and the longitude and latitude (location)

public class LocationPlace implements Serializable {
    private String mNameOfPlace;
    private double mLatitude;
    private double mLongtitude;
    private String mAddress;

    public LocationPlace(String name, Location location, String address) {
        this(name, location.getLatitude(), location.getLongitude(), address);
    }

    public LocationPlace(String name, double latitude, double longtitude, String address) {
        this.mNameOfPlace = name;
        this.mLatitude = latitude;
        this.mLongtitude = longtitude;
        this.mAddress = address;
    }

    @Override
    public String toString() {
        return mNameOfPlace;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongtitude() {
        return mLongtitude;
    }

    public String getAddress() {
        return mAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof LocationPlace)) {
            return false;
        }
        LocationPlace other = (LocationPlace) o;
        return mLatitude == other.getLatitude()
                && mLongtitude == other.getLongtitude();
    }

    @Override
    public int hashCode() {
        return (int) (mLongtitude * 100 + mLatitude);
    }

    public String getPrettyLocation(Context mContext) {
        if (mNameOfPlace == null) {
            mNameOfPlace = Revisitor.getInstance(mContext).getAddressFrom(mLatitude, mLongtitude);
        }
        if (mNameOfPlace == null) {
            return mAddress;
        }
        return mNameOfPlace;
    }

    private static final double ACCURACY = 1e-8;

    public boolean compareDistance(double latitude, double longtitude) {
        return Math.abs(mLatitude - latitude) < ACCURACY
                && Math.abs(mLongtitude - longtitude) < ACCURACY;
    }

}
