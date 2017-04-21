package xyz.gautamhans.locus.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import xyz.gautamhans.locus.App;
import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.entity.StoredReminder;
import xyz.gautamhans.locus.ui.BaseDialog;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class DeleteReminderDialog extends BaseDialog {
    private static final String EXTRA_REMINDER_ID = App.PACKAGE + ".reminder_id";

    public DeleteReminderDialog() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        long reminderID = getArguments().getLong(EXTRA_REMINDER_ID);
        StoredReminder reminder = DBHelper.getInstance(getActivity()).getReminder(reminderID);

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.delete_reminder_title)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onPositiveButtonClicked();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        return dialog;
    }

    public static DeleteReminderDialog newInstance(long id) {
        Bundle bundle = new Bundle(1);
        DeleteReminderDialog dialog = new DeleteReminderDialog();
        bundle.putLong(EXTRA_REMINDER_ID, id);
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnButtonClickListener {

        void onPositiveButtonClicked();
    }

    private OnButtonClickListener mListener;

    public void setPositiveListener(OnButtonClickListener l) {
        this.mListener = l;

    }
}
