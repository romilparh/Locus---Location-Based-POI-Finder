package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.gautamhans.locus.R;

public class Save_Place_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save__place__info);
        getSupportActionBar().setTitle("How to save a place?");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        Intent i=new Intent(this,FeedbackActivity.class);
//        startActivity(i);
//        finish();
    }
}
