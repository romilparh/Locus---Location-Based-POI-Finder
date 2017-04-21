package xyz.gautamhans.locus.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import xyz.gautamhans.locus.ui.BaseDialog;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class ProgressDialog extends BaseDialog {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new android.app.ProgressDialog(getActivity());
    }
}
