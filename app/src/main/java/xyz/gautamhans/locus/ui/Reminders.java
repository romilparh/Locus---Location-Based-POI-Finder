package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.db.DatabaseModel;
import xyz.gautamhans.locus.ui.adapter.RVAdapter_Reminders;

/**
 * Created by Gautam on 18-Apr-17.
 */

public class Reminders extends AppCompatActivity {

    FloatingActionButton fab;
    DBHelper dbHelper;
    List<DatabaseModel> dbList;
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        try {
            getSupportActionBar().setTitle("Reminders");
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbHelper = new DBHelper(this);
        dbList = new ArrayList<DatabaseModel>();
        dbList = dbHelper.getDataFromDB();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_reminders);
        mRecyclerView.setHasFixedSize(true);

        // using a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specifying an adapter
        mAdapter = new RVAdapter_Reminders(this, dbList);
        mRecyclerView.setAdapter(mAdapter);

    }


    public void addReminder(View view) {
        Intent intent = new Intent(Reminders.this, NewReminder.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reminders.this, MainActivity.class);
        startActivity(intent);
    }
}