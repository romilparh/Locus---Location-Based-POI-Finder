package xyz.gautamhans.locus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gautam on 09-Apr-17.
 */

public interface ApiInterface {

    @GET("json?key=AIzaSyBE8jPCH28fGzNwldLfR2h5WTgMC_IvuJI")
    Call<Example> getNearbyPlaces(@Query("location") String location, @Query("radius") int radius,
                                  @Query("type") String category);

}
