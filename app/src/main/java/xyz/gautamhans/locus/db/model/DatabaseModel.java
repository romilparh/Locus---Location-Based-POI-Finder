package xyz.gautamhans.locus.db.model;

/**
 * Created by Gautam on 22-Apr-17.
 */

public class DatabaseModel {
    public String title, description, address, placeID;
    Double longitude, latitude;
    int radius;
    long id;

    public int getId() {
        return (int) id;
    }

    public DatabaseModel(long id, String title, String description, String address, String placeID, Double longitude, Double latitude, int radius) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.placeID = placeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.id = id;
    }

    public DatabaseModel(String title, String description, String address, String placeID, Double longitude, Double latitude, int radius) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.placeID = placeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
