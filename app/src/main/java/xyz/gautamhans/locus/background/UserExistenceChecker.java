package xyz.gautamhans.locus.background;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.gautamhans.locus.retrofit.ApiClientSavePlace;
import xyz.gautamhans.locus.retrofit.ApiInterfaceSavePlace;
import xyz.gautamhans.locus.retrofit.pojos.SavePlace;

/**
 * Created by Gautam on 12-May-17.
 */

public class UserExistenceChecker extends AppCompatActivity {

    List<SavePlace> userInfo;

    String name, email, userIdToken, photoUrl;
    boolean flag = false;

    SharedPreferences sharedPref;

    public UserExistenceChecker(SharedPreferences sharedPref){
        this.sharedPref = sharedPref;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    public boolean checkIfUserExist(){
        name = sharedPref.getString("userName", "");
        email = sharedPref.getString("userEmail", "");
        userIdToken = sharedPref.getString("userIdToken", "");

        Retrofit retrofitCheckUser = ApiClientSavePlace.getClient();
        ApiInterfaceSavePlace apiInterfaceSavePlace = retrofitCheckUser.create(ApiInterfaceSavePlace.class);
        final Call<List<SavePlace>> usersInfo = apiInterfaceSavePlace.getSavePlaces();


        usersInfo.enqueue(new Callback<List<SavePlace>>() {
            @Override
            public void onResponse(Call<List<SavePlace>> call, Response<List<SavePlace>> response) {
                Log.d("in", "enqueue");
                try{
                    Log.d("RESPONSE \nBody Size: ", response.body().size() + "");
                    userInfo = response.body();
                    Log.d("json", String.valueOf(usersInfo));
                    Log.d("sizeinfo", String.valueOf(userInfo.size()));
                    if(userInfo.size() != 0) {
                        for (int i = 1; i < userInfo.size(); i++) {
                            String userIdTokenReturned = userInfo.get(i).getToken();
                            String emailReturned = userInfo.get(i).getEmail();
                            Log.d("response", "token returned: " + userIdTokenReturned);
                            Log.d("response", "email returned: " + emailReturned);
                            if (userIdTokenReturned.equals(userIdToken)
                                    && emailReturned.equals(email)) {
                                flag = true;
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("userID", userInfo.get(i).getId());
                                editor.apply();
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SavePlace>> call, Throwable throwable) {
                Log.d("RESPONSE", "FAILED CHECKING USER ID/SOMETHING HAPPENED");
            }
        });
        return flag;
    }

    Retrofit retrofitSaveUserInfo;
    ApiInterfaceSavePlace apiInterfaceSavePlace;
    Call<List<SavePlace>> savedInfo;
    int userIDReturned;

    public int saveUserInfo(){
        name = sharedPref.getString("userName", "");
        email = sharedPref.getString("userEmail", "");
        userIdToken = sharedPref.getString("userIdToken", "");
        retrofitSaveUserInfo = ApiClientSavePlace.getClient();
        apiInterfaceSavePlace = retrofitSaveUserInfo.create(ApiInterfaceSavePlace.class);
        savedInfo = apiInterfaceSavePlace.saveUserInfo(name, userIdToken, email);

        userIDReturned = checkForSaveUserInfo();
        Log.d("userID", String.valueOf(userIDReturned));
        return userIDReturned;
    }

    List<SavePlace> userInfoResponse;
    public int checkForSaveUserInfo(){
        savedInfo.enqueue(new Callback<List<SavePlace>>() {
            @Override
            public void onResponse(Call<List<SavePlace>> call, Response<List<SavePlace>> response) {
                try {
                    userInfoResponse = response.body();
                    if(userInfoResponse.size() == 0){
                      checkForSaveUserInfo();
                    } else{
                        userIDReturned = userInfoResponse.get(0).getId();
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("userID", userInfoResponse.get(0).getId());
                        editor.apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SavePlace>> call, Throwable throwable) {
                Log.d("RESPONSE", "COULDN'T SAVE USER INFO");
            }
        });
        return userIDReturned;
    }
}
