package xyz.gautamhans.locus.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import xyz.gautamhans.locus.App;
import xyz.gautamhans.locus.DeLog;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.ui.BaseActivity;
import xyz.gautamhans.locus.ui.fragment.CompassFragment;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class CompassActivity extends BaseActivity {

    public static final String EXTRA_REMINDER_ID = App.PACKAGE + ".REMINDER_ID";
    private static final String TAG = "CompassActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        Bundle extras = getIntent().getExtras();
        if (!extras.containsKey(EXTRA_REMINDER_ID)) {
            DeLog.w(TAG, "Cannot show activity without reminder ID");
            finish();
            return;
        }

        if (savedInstanceState == null) {
            CompassFragment frag = CompassFragment.newInstance(extras.getLong(EXTRA_REMINDER_ID));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.compass_holder, frag).commit();

        }

    }

    public static void startActivity(Context context, long reminderID) {
        Intent i = new Intent(context, CompassActivity.class);
        i.putExtra(EXTRA_REMINDER_ID, reminderID);
        context.startActivity(i);

    }

}
