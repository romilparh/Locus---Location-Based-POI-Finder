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
 * Created by Gautam on 13-May-17.
 */

public class UEC extends AppCompatActivity {

    List<SavePlace> userInfo;

    String email, name, userIdToken;
    boolean flag;
    SharedPreferences sharedPref;
    OnUserExistsCallback callback;

    public UEC(SharedPreferences sharedPref){
        this.sharedPref = sharedPref;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnUserExistsCallback {
        void onUserExists();
        void onUserDoesNotExist();
    }

    public boolean checkIfUserExists(OnUserExistsCallback onUserExistsCallback) {
        email = sharedPref.getString("userEmail", "");

        Retrofit retrofitCheckUser = ApiClientSavePlace.getClient();
        ApiInterfaceSavePlace apiInterfaceSavePlace = retrofitCheckUser.create(ApiInterfaceSavePlace.class);
        final Call<List<SavePlace>> checkUser = apiInterfaceSavePlace.getSavePlaces();
        final OnUserExistsCallback callback = onUserExistsCallback;

        checkUser.enqueue(new Callback<List<SavePlace>>() {
            @Override
            public void onResponse(Call<List<SavePlace>> call, Response<List<SavePlace>> response) {
                userInfo = response.body();
                try {
                    if(userInfo.size()!=0){
                        for (int i = 0; i < userInfo.size(); i++) {
                            String emailReturned = userInfo.get(i).getEmail();
                            Log.d("response", "email returned: " + emailReturned);
                            Log.d("sharedpref", "email: " + email);
                            if (emailReturned.equals(email)) {
                                Log.d("response", "email match?: " + emailReturned.equals(email));
//                                flag = true;
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("userID", userInfo.get(i).getId());
                                Log.d("ID returned", String.valueOf(userInfo.get(i).getId()));
                                editor.apply();
                                callback.onUserExists();
                                break;
                            } else {
                                callback.onUserDoesNotExist();
                            }
                        }
                    }
                } catch (Exception e) {
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

    List<SavePlace> userInfoResponse;
    public void saveUserInfo(){
        name = sharedPref.getString("userName", "");
        email = sharedPref.getString("userEmail", "");
        userIdToken = sharedPref.getString("userIdToken", "");
        Retrofit retrofitSaveUserInfo = ApiClientSavePlace.getClient();
        ApiInterfaceSavePlace apiInterfaceSavePlace = retrofitSaveUserInfo.create(ApiInterfaceSavePlace.class);
        Call<List<SavePlace>> savedInfo = apiInterfaceSavePlace.saveUserInfo(name, userIdToken, email);

        savedInfo.enqueue(new Callback<List<SavePlace>>() {
            @Override
            public void onResponse(Call<List<SavePlace>> call, Response<List<SavePlace>> response) {
                try {
                    Log.d("error", ": "+ response.errorBody().toString());
                    userInfoResponse = response.body();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("userID", userInfoResponse.get(0).getId());
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SavePlace>> call, Throwable throwable) {
                Log.d("RESPONSE", "COULDN'T SAVE USER INFO");
            }
        });
    }
}
