package xyz.gautamhans.locus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import xyz.gautamhans.locus.R;

public class Reminder_UI_Activity extends AppCompatActivity{

    private static final int RADIUS_MAX = 500;
    private static final int DEFAULT_RADIUS = 50;
    private TextView address_selected, radius_text;
    private EditText task_title, task_description;
    private ImageView chooseLocation;
    private SeekBar mRadius;

    private double mLatitude;
    private double mLongtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_ui);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        task_title = (EditText) findViewById(R.id.task_title_tv);
        task_description = (EditText) findViewById(R.id.task_desc_tv);
        address_selected = (TextView) findViewById(R.id.address_selected_tv);
        radius_text = (TextView) findViewById(R.id.radius_text);
        mRadius = (SeekBar) findViewById(R.id.radius_slider);
        mRadius.setMax(RADIUS_MAX);
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

    private void updateRadiusText(){
        int meters = mRadius.getProgress();
        this.radius_text.setText(getString(R.string.radius_in_m, meters));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reminder_UI_Activity.this, Reminders.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home){
            Intent intent = new Intent(Reminder_UI_Activity.this, Reminders.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
