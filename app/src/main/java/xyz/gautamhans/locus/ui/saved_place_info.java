package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.gautamhans.locus.R;

public class saved_place_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_place_info);
        getSupportActionBar().setTitle("How to view saved places?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,FeedbackActivity.class);
        startActivity(i);
        finish();
    }
}
