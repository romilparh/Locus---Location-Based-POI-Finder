package xyz.gautamhans.locus.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import xyz.gautamhans.locus.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    private SeekBar mRadius;
    private static final int RADIUS_MAX = 50000;
    private static final int DEFAULT_RADIUS = 10000;
    private TextView radiusText;
    private int meters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Settings");

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        radiusText = (TextView) findViewById(R.id.radius_settings_imp_settings);
        mRadius = (SeekBar) findViewById(R.id.radius_seekbar_settings);
        mRadius.setMax(RADIUS_MAX);
        mRadius.setProgress(DEFAULT_RADIUS);
        updateRadiusText();

        mRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRadiusText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }

    private void updateRadiusText(){
        meters = mRadius.getProgress();
        this.radiusText.setText(getString(R.string.radius_in_m_settings, meters));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("currentRadius", meters);
        editor.apply();
        Log.d("current Radius", ": " +sharedPref.getInt("currentRadius", 0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                Intent actMain = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(actMain);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
}
