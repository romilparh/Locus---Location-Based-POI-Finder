package xyz.gautamhans.locus.ui;

/**
 * Created by Gautam on 02-Apr-17.
 */

public class PlaceCardDetails {
    public int place_photoId;
    public double place_rating;
    public String place_name;
    public String place_address;

    PlaceCardDetails(int place_photoId, double place_rating,
                     String place_name, String place_address) {
        this.place_photoId = place_photoId;
        this.place_rating = place_rating;
        this.place_name = place_name;
        this.place_address = place_address;
    }
}