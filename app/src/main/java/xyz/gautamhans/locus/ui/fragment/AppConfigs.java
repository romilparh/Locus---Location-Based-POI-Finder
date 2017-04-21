package xyz.gautamhans.locus.ui.fragment;

import android.content.Context;

import xyz.gautamhans.locus.R;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class AppConfigs {

    public static boolean setListOrderedByDistance(Context activity, boolean sortByDistance) {
        return activity.getSharedPreferences(activity.getString(R.string.file_userpreferences), Context.MODE_PRIVATE)
                .edit()
                .putBoolean(activity.getString(R.string.key_sort_order), sortByDistance).commit();
    }

    public static boolean getListOrderedByDistance(Context context) {
        return context.getSharedPreferences(context.getString(R.string.file_userpreferences), Context.MODE_PRIVATE)
                .getBoolean(context.getString(R.string.key_sort_order), false);
    }

}
