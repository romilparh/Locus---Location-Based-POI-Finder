package xyz.gautamhans.locus.ui;

/**
 * Created by Gautam on 14-May-17.
 */

public class SavedPlaceModel {
    String placeName, placeAddress, placeId, latitude, longitude, photoReference, id;

    public SavedPlaceModel(String placeName,
                           String placeAddress,
                           String placeId,
                           String latitude,
                           String longitude,
                           String photoReference,
                           String id) {

        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoReference = photoReference;
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
