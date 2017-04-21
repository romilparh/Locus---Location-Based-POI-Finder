package xyz.gautamhans.locus.ui;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class BaseFragment extends Fragment {

    private BaseActivity mActivity;

    @Override
    public void onAttach(Activity activity) {

        try {
            mActivity = (BaseActivity) activity;
        } catch (ClassCastException ex) {
            throw new RuntimeException(this.getClass().getSimpleName() + " can only be attached to a BaseActivity");
        }
        super.onAttach(activity);
    }

    protected BaseActivity getBaseActivity() {
        return mActivity;
    }

}
