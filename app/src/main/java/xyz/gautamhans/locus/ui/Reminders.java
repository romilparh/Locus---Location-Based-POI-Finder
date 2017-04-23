package xyz.gautamhans.locus.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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

public class Reminders extends AppCompatActivity implements RVAdapter_Reminders.ReminderListener {

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
        mAdapter = new RVAdapter_Reminders(this, dbList, this);
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


    private void onReminderDelete(int position) {
        int id = dbList.get(position).getId();
        dbHelper.deleteARow(id);
        dbList.remove(position);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onOptionsClick(View v, final int reminderIndex) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.reminder_rv_menu);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.menu_delete_option:
                        onReminderDelete(reminderIndex);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onCardClick(View v, int position) {

    }
}
