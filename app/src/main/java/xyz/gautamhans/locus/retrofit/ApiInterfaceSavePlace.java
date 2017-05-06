package xyz.gautamhans.locus.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.gautamhans.locus.retrofit.pojos.Example;
import xyz.gautamhans.locus.retrofit.pojos.SavePlace;

/**
 * Created by Gautam on 09-Apr-17.
 */

public interface ApiInterfaceSavePlace {

    @GET("user")
    Call<List<SavePlace>> getSavePlaces();

}
