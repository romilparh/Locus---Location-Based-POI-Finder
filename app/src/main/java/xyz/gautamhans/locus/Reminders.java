package xyz.gautamhans.locus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Gautam on 18-Apr-17.
 */

public class Reminders extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }



    public void addReminder(View view){
        Intent intent = new Intent(Reminders.this, Reminder_UI_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reminders.this, MainActivity.class);
        startActivity(intent);
    }
}
