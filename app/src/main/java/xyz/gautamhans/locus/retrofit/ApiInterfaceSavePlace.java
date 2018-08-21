package xyz.gautamhans.locus.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.gautamhans.locus.retrofit.pojos.Example;
import xyz.gautamhans.locus.retrofit.pojos.Place;
import xyz.gautamhans.locus.retrofit.pojos.SavePlace;

/**
 * Created by Gautam on 09-Apr-17.
 */

public interface ApiInterfaceSavePlace {

    @GET("user")
    Call<List<SavePlace>> getSavePlaces();

    @POST("user")
    Call<List<SavePlace>> saveUserInfo(@Query("name") String name,
                                       @Query("userToken") String userToken,
                                       @Query("email") String email);

    @POST("user/{id}/places")
    Call<List<Place>> savePlace(@Path("id") int id,
                                @Query("placeName") String placeName,
                                @Query("placeId") String placeId,
                                @Query("placeAddress") String placeAddress,
                                @Query("latitude") String latitude,
                                @Query("longitude") String longitude,
                                @Query("photoReference") String photoReference);

    @GET("user/{id}/places")
    Call<List<Place>> getSavedPlaces(@Path("id") int id);
}
