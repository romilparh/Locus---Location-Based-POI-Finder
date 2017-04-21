package xyz.gautamhans.locus;

import android.content.Context;

/**
 * Created by Gautam on 21-Apr-17.
 */

final public class ReminderManager {

    private static ReminderManager sInstance;

    private Context mContext;

    private ReminderManager(Context context) {
        this.mContext = context.getApplicationContext();
    }


    public static ReminderManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ReminderManager(context);
        }
        return sInstance;
    }

    public void cancelReminder(long id) {

    }
}