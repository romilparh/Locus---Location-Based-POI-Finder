package xyz.gautamhans.locus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import xyz.gautamhans.locus.db.DatabaseModel;
import xyz.gautamhans.locus.ui.Reminders;

/**
 * Created by Gautam on 24-Apr-17.
 */

public class Notifier {

    private static Notifier sInstance;
    private NotificationManager mManager;
    private Context mContext;
    private Notifier() {
    }

    private Notifier(Context context) {
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mContext = context;
    }

    public static void notifyForReminder(Context context, DatabaseModel reminder) {
        Intent notificationIntent = new Intent(context, Reminders.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(context.getApplicationContext())
                        .setContentTitle(reminder.getTitle())
                        .setContentText(reminder.getDescription())
                        .setAutoCancel(false)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setTicker(reminder.getDescription())
                        .setVibrate(new long[]{1000, 1000})
                        .setLights(Color.BLUE, 3000, 3000)
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.ic_stat_maps_location_history)
                        .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) reminder.getId(), notification);
    }

    public static void cancelForReminder(Context context, long id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel((int) id);
    }

    public Notifier getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Notifier(context);
        }
        return sInstance;
    }

}
