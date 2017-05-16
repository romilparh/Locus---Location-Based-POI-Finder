package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import xyz.gautamhans.locus.R;

public class WhatIsLocus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_locus);
        getSupportActionBar().setTitle("What is Locus?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed() {
            Intent i=new Intent(this,FeedbackActivity.class);
            startActivity(i);
    }
}
