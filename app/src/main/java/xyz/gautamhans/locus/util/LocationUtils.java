package xyz.gautamhans.locus.util;

/**
 * Created by Gautam on 21-Apr-17.
 */

final public class LocationUtils {
    public static String createLocationString(double latitude, double longtitude) {
        return String.format("(%f, %f)", latitude, longtitude);
    }
}