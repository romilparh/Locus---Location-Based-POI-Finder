package xyz.gautamhans.locus.background;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import xyz.gautamhans.locus.ui.MainActivity;
import xyz.gautamhans.locus.ui.SignInActivity;

/**
 * Created by Gautam on 16-Apr-17.
 */

public class BaseLoginChecker extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (sharedPref.getString("userName", "").length() == 0) {
            Intent intent = new Intent(BaseLoginChecker.this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(BaseLoginChecker.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }
}
