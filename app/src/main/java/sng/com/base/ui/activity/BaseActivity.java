package sng.com.base.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sng.com.base.BaseApplication;
import sng.com.base.R;
import sng.com.base.ui.dialog.ProgressDialogFragment;
import sng.com.base.ui.fragment.BaseFragment;
import sng.com.base.util.Constant;
import sng.com.base.util.PermissionUtils;

public class BaseActivity extends AppCompatActivity implements BaseApplication.OnToolBarCallback {

    private static final String TAG = "BaseActivity";
    protected final Handler mHandler = new Handler();
    protected BaseApplication.OnToolBarCallback mOnToolbarListener;
    protected AlertDialog.Builder mBuilder;
    protected ProgressDialogFragment mProgressDialogFragment;
    private Toolbar mToolbar;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialogFragment = ProgressDialogFragment.newInstance(null, 1);
        requestPhonePerMission();
        PermissionUtils.requestPermissionActivity(this, Manifest.permission.CAMERA);
    }

    protected void initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mOnToolbarListener = this;

        if (mToolbar == null) {
            return;
        }
        if (!TextUtils.isEmpty(title)) mToolbar.setTitle(title);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ico_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnToolbarListener.onMenuToolbarClicked();
            }
        });
    }

    @Override
    public void onMenuToolbarClicked() {
        // should be override by sub class if it have menu
    }

    //    protected void setOnToolbarListener(BaseApplication.OnToolBarCallback onToolbarListener) {
//        mOnToolbarListener = onToolbarListener;
//    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestLocationPermission() {
        Log.i("sonnguyen", "LeaderLocation permission has NOT been granted. Requesting permission.");

        int hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasLocationCoarsePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> permissions = new ArrayList<>();
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (hasLocationCoarsePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), Constant.REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPhonePerMission() {
        int hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        List<String> permissions = new ArrayList<>();
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), Constant.REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }
    }

    protected void replaceFragmentWithStack(Fragment fragment, String tag) {
        Log.d("sonnguyen", "replaceFragmentWithStack: " + tag);
        try {
            Fragment fr = getSupportFragmentManager().findFragmentByTag(tag);
            if (fr != null) {
                getSupportFragmentManager().beginTransaction().remove(fr).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(tag).replace(R.id.fragment_container, fragment, tag).commit();
        getSupportFragmentManager().executePendingTransactions();

    }

    protected void replaceFragmment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, tag).commitAllowingStateLoss();
    }

    public void showConfirmDialog(String mess, String button, boolean cancelable, boolean isNegativeButton, DialogInterface.OnClickListener listener) {

        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialogNoTitle));
        }
        if (listener == null) {
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mBuilder != null) {
                        mAlertDialog.dismiss();
                    }
                }
            };
        }

        mBuilder.setMessage(mess)
                .setCancelable(cancelable)
                .setPositiveButton(button, listener);
        if (isNegativeButton) {
            mBuilder.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAlertDialog.dismiss();
                }
            });
        }
        mAlertDialog = mBuilder.create();
        mAlertDialog.setCancelable(cancelable);
        mAlertDialog.show();
    }

    public void showConfirmDialog(String mess, String confirmButton, String cancelButton, boolean cancelable, final BaseFragment.ConfirmDialogListener listener) {
        String okButton;
        String btnCancel;
        if (TextUtils.isEmpty(confirmButton)) {
            okButton = getString(R.string.common_ok);
        } else {
            okButton = confirmButton;
        }
        if (TextUtils.isEmpty(cancelButton)) {
            btnCancel = getString(R.string.common_cancel);
        } else {
            btnCancel = cancelButton;
        }
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(getBaseContext());
        }

        mBuilder.setMessage(mess)
                .setCancelable(cancelable)
                .setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancel();
                        mAlertDialog.dismiss();
                    }
                })
                .setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onConfirm();
                    }
                });
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    public void showProgressDialog(final boolean isShow) {

        if (mProgressDialogFragment == null) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    if (!mProgressDialogFragment.isAdded()) {
                        mProgressDialogFragment.show(getSupportFragmentManager(), ProgressDialogFragment.class.getSimpleName());
                    }
                } else {
                    if (mProgressDialogFragment.getActivity() != null) {
                        mProgressDialogFragment.dismissAllowingStateLoss();
                    }
                }
            }
        });
    }

}
