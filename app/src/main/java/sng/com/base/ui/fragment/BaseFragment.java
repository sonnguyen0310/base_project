package sng.com.base.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import sng.com.base.BaseApplication;
import sng.com.base.R;
import sng.com.base.ui.activity.BaseActivity;
import sng.com.base.ui.dialog.AlertDialogFragment;
import sng.com.base.ui.dialog.ProgressDialogFragment;

/**
 * Created by nguye on 3/17/2016.
 */
public class BaseFragment extends Fragment {
    protected static final int INDEX_CONTENT = 0;
    protected static final int INDEX_PROGRESS = 1;

    protected Handler mHandler = new Handler();

    protected ProgressDialogFragment mProgressDialogFragment;

    protected BaseApplication mApp;

    protected ViewSwitcher mViewSwitcher;
    protected AlertDialog.Builder mBuilder;
    protected AlertDialog mAlertDialog;
    protected String mTitle;
    protected BaseApplication.OnToolBarCallback mToolbarListener;
    protected Activity mActivity;
    protected Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialogFragment = ProgressDialogFragment.newInstance(null, 1);
        mApp = (BaseApplication) getActivity().getApplication();
    }

    /**
     * show or hide progress dialog
     *
     * @param isShow true : show, false : hide progress
     */
    public void showProgressDialog(final boolean isShow) {

        if (mProgressDialogFragment == null) {
            return;
        }
        try {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isShow) {
                        if (!mProgressDialogFragment.isAdded()) {
                            try {
                                mProgressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.class.getSimpleName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (mProgressDialogFragment.getActivity() != null) {
                            try {
                                mProgressDialogFragment.dismissAllowingStateLoss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialog(final String message) {
        showAlertDialog(getString(R.string.common_error), message);
    }

    public void showAlertDialog(final String title, final String message) {
        showAlertDialog(title, message, R.mipmap.ic_launcher);
    }

    public void showAlertDialog(final String title, final String message, final int alertId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance(title,
                        message, getString(R.string.common_ok), alertId);
                dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
            }
        });
    }

    /*
    * show dialog with message and yes, no button, pass listener to do something when click on OK button*/
    public void showConfirmDialog(String mess, String button, boolean cancelable, DialogInterface.OnClickListener listener) {
        String okButton;
        if (TextUtils.isEmpty(button)) {
            okButton = getString(R.string.common_ok);
        } else {
            okButton = button;
        }
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(getContext());
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
                .setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                    }
                })
                .setPositiveButton(okButton, listener);
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    public void showConfirmDialog(String mess, String confirmButton, String cancelButton, boolean showCancelButton, boolean cancelable, final ConfirmDialogListener listener) {
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
            mBuilder = new AlertDialog.Builder(getContext());
        }

        mBuilder.setMessage(mess)
                .setCancelable(cancelable)
                .setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        } else {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    }
                });

        if (showCancelButton) {
            mBuilder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mAlertDialog != null) {
                        mAlertDialog.dismiss();
                    }
                    if (listener != null) {
                        listener.onCancel();
                    }
                }
            });
        }
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    protected TabLayout.Tab createTabItem(TabLayout tabLayout, String title, boolean isActive, int color) {
//        TabLayout.Tab tab = tabLayout.newTab();
//        tab.setCustomView(R.layout.view_tab_item);
//        TextView textTitle = ((TextView) tab.getCustomView().findViewById(R.id.text_title));
//        textTitle.setText(title);
//        if(isActive) {
////            textTitle.setTypeface(TypefaceUtils.load(MPAApplication.getContext().getAssets(), "fonts/Montserrat-Bold.ttf"), Typeface.BOLD);
//        }
//
//        textTitle.setTextColor(ContextCompat.getColor(BaseApplication.getContext(), color));
//
//        return tab;
//    }

    /**
     * show progress inside view with view switcher
     *
     * @param isShow true : show, false : hide
     */
    public void showProgressbar(final boolean isShow) {
        if (mViewSwitcher != null) {
            mViewSwitcher.setDisplayedChild(isShow ? INDEX_PROGRESS : INDEX_CONTENT);
        }
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void replaceFragmmentWithStack(android.support.v4.app.Fragment fragment, String tag) {
//        try {
//            Fragment fr = getFragmentManager().findFragmentByTag(tag);
//            if (fr != null) {
//                getFragmentManager().beginTransaction().remove(fr).commit();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        getFragmentManager().beginTransaction().addToBackStack(tag).replace(R.id.fragment_container, fragment, tag).commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();

    }

    protected void replaceFragmment(android.support.v4.app.Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, tag).commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLeftMenu();
    }

    protected void initToolbar(View view) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            final BaseActivity activity = (BaseActivity) getActivity();
            mToolbar.setTitleTextAppearance(getContext(), R.style.Toolbar_TitleText);
            // set title for toolbar
            if (activity != null) {
                activity.setSupportActionBar(mToolbar);

                if (!TextUtils.isEmpty(mTitle)) {
                    activity.getSupportActionBar().setTitle(mTitle);
                } else {
                    activity.getSupportActionBar().setTitle(getScreenTitle());
                }
                activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
                if (hasShowNavigation()) {
                    activity.getSupportActionBar().setHomeAsUpIndicator(getHomeAsUpIconRes());
                    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
                    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mToolbarListener != null) {
                                switch (getHomeAsUpIconRes()) {
                                    case R.drawable.ico_menu:
                                        mToolbarListener.onMenuToolbarClicked();
                                        break;
                                    case R.drawable.ico_back:
                                        onBackPressed();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });
                }
            }

        }
    }

    protected void setToolbarColor(View view, int color) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(getResources().getColor(color));
        }

    }

    protected String getScreenTitle() {
//        overwrite in child fragment
        return "";
    }

    protected boolean hasShowNavigation() {
        // default always show navigation icon
        return true;
    }

    protected int getHomeAsUpIconRes() {
        return R.drawable.ico_menu; // default is icon menu
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        if (mActivity instanceof BaseApplication.OnToolBarCallback) {
            mToolbarListener = (BaseApplication.OnToolBarCallback) getActivity();
        }
    }

    protected void onBackPressed() {
        getActivity().onBackPressed();
    }

    private void updateLeftMenu() {
        try {
            Activity activity = getActivity();
//            if (activity instanceof HomeActivity) {
//                activity = getActivity();
//                if (getScreenTitle().equals(getString(R.string.left_menu_leader_stats))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_leader_stats);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_user_home))) {
//                    if (BaseFragment.this instanceof MapSearchFragment) {
//                        ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_user_search);
//                    } else {
//                        ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_user_home);
//                    }
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_switch_to_leader))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_switch_to_leader);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_switch_to_user))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_switch_to_user);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_profile))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_profile);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_setting))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_setting);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_user_message))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_user_message);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_help))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_help);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_info))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_info);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_leader_stats))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_leader_stats);
//                } else if (getScreenTitle().equals(getString(R.string.left_menu_leader_guide))) {
//                    ((HomeActivity) activity).updateSelectedLeftMenu(R.string.left_menu_leader_guide);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ConfirmDialogListener {
        void onConfirm();

        void onCancel();
    }
}
