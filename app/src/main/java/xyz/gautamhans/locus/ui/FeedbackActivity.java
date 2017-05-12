package xyz.gautamhans.locus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import xyz.gautamhans.locus.R;

public class FeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int fail;

    private DrawerLayout drawer;
    private View navHeader;
    private NavigationView navigationView;

    String name, email, photoUrl;
    private String userIdToken;
    TextView userName, userEmail;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,    drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeUserInfo();

    }

    public void sendFeedback(View button) {
        // Do click handling here
        fail=0;

        final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        String feedback = feedbackField.getText().toString();
        if(feedback.matches("")){
            fail=1;
            Context context = getApplicationContext();
            CharSequence text = "No Feedback Message";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        Context context = getApplicationContext();
        CharSequence text = "Feedback Sent!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
//        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        if(fail==0) {
            toast.show();
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_saved_places) {
            Intent i=new Intent(this, SavedPlaces.class);
            startActivity(i);
        } else if (id == R.id.nav_reminders) {
            Intent intent = new Intent(FeedbackActivity.this, Reminders.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_feedback) {
            Context context = getApplicationContext();
            CharSequence text = "Already on Feedback Page";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeUserInfo() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        name = sharedPref.getString("userName", "");
        email = sharedPref.getString("userEmail", "");
        userIdToken = sharedPref.getString("userIdToken", "");
        photoUrl = sharedPref.getString("photoUrl", "");

        Log.d(String.valueOf(this), "ID Token: " + userIdToken);

        userName = (TextView) navHeader.findViewById(R.id.nav_user_name_tv);
        userEmail = (TextView) navHeader.findViewById(R.id.nav_user_email_tv);
        userPhoto = (ImageView) navHeader.findViewById(R.id.user_photoView);

        try {
            Picasso.with(this).load(photoUrl).into(userPhoto);
            userName.setText(name);
            userEmail.setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
