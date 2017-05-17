package xyz.gautamhans.locus.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.gautamhans.locus.retrofit.pojos.Example;
import xyz.gautamhans.locus.retrofit.pojos.SavePlace;
import xyz.gautamhans.locus.retrofit.pojosplaces.Attractions;

/**
 * Created by Gautam on 09-Apr-17.
 */

public interface ApiInterface {

    @GET("json?key=AIzaSyCCMCIXp6kXBSQdkucV51LP23cuvxu4tjI")
    Call<Example> getNearbyPlaces(@Query("location") String location,
                                  @Query("rankby") String rankby,
                                  @Query("type") String category);

    @GET("json?key=AIzaSyCCMCIXp6kXBSQdkucV51LP23cuvxu4tjI")
    Call<Attractions> getAttractions(@Query("query") String attractions,
                                     @Query("location") String location,
                                     @Query("rankby") String rankby);

}
