package sng.com.base.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import sng.com.base.R;
import sng.com.base.ui.other.IDialogFragmentListener;

public class AlertDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT";
    private static final String ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT";
    private static final String ARG_ALERT_ICON_RES_ID = "ARG_ALERT_ICON_RES_ID";
    private static final String ARG_IS_SHOW_SIGNATURE = "ARG_IS_SHOW_SIGNATURE";

    protected static boolean isShow = false;
    protected String mTitle;
    protected String mMessage;
    protected String mPositiveButtonText;
    protected String mNegativeButtonText;
    protected int mAlertIconResId;
    protected boolean mIsShowSignature;

    protected IDialogFragmentListener mListener;


    public static AlertDialogFragment newInstance(String title, String message, String positiveButtonText, int alertIconResId) {
        return newInstance(title, message, positiveButtonText, alertIconResId, null, 0);
    }

    public static AlertDialogFragment newInstance(String title, String message, String positiveButtonText, int alertIconResId, boolean isShowSignature) {
        return newInstance(title, message, positiveButtonText, null, alertIconResId, null, 0, isShowSignature);
    }

    public static <T extends Fragment & IDialogFragmentListener> AlertDialogFragment newInstance(String title,
                                                                                                 String message,
                                                                                                 String positiveButtonText,
                                                                                                 int alertIconResId,
                                                                                                 T listener, int dialogId) {
        return newInstance(title, message, positiveButtonText, null, alertIconResId, listener, dialogId);
    }

    public static <T extends Fragment & IDialogFragmentListener> AlertDialogFragment newInstance(String title,
                                                                                                 String message,
                                                                                                 String positiveButtonText,
                                                                                                 String negativeButtonText,
                                                                                                 int alertIconResId,
                                                                                                 T listener, int dialogId) {

        return newInstance(title, message, positiveButtonText, negativeButtonText, alertIconResId, listener, dialogId, false);
    }

    public static <T extends Fragment & IDialogFragmentListener> AlertDialogFragment newInstance(String title,
                                                                                                 String message,
                                                                                                 String positiveButtonText,
                                                                                                 String negativeButtonText,
                                                                                                 int alertIconResId,
                                                                                                 T listener,
                                                                                                 int dialogId,
                                                                                                 boolean isShowSignature) {
        AlertDialogFragment fragment = new AlertDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText);
        args.putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText);
        args.putInt(ARG_ALERT_ICON_RES_ID, alertIconResId);
        args.putBoolean(ARG_IS_SHOW_SIGNATURE, isShowSignature);

        fragment.setArguments(args);

        fragment.setTargetFragment(listener, dialogId);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Transparent);

        mTitle = getArguments().getString(ARG_TITLE);
        mMessage = getArguments().getString(ARG_MESSAGE);
        mPositiveButtonText = getArguments().getString(ARG_POSITIVE_BUTTON_TEXT);
        mNegativeButtonText = getArguments().getString(ARG_NEGATIVE_BUTTON_TEXT);
        mAlertIconResId = getArguments().getInt(ARG_ALERT_ICON_RES_ID);
        mIsShowSignature = getArguments().getBoolean(ARG_IS_SHOW_SIGNATURE);

        if (getTargetFragment() != null && getTargetFragment() instanceof IDialogFragmentListener) {
            mListener = (IDialogFragmentListener) getTargetFragment();
        } else {
            // check activity
            if (getActivity() instanceof IDialogFragmentListener) {
                mListener = (IDialogFragmentListener) getActivity();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mTitleTextView.setText(mTitle);
//        mContentTextView.setText(mMessage);
//        mPositiveButton.setText(mPositiveButtonText);
//        mAlertIcon.setImageResource(mAlertIconResId);
//
//        mPositiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//
//                if (mListener != null) {
//                    mListener.onPositiveButtonClicked(getTargetRequestCode());
//                }
//            }
//        });
//
//        if (mNegativeButtonText != null) {
//            mNegativeButton.setVisibility(View.VISIBLE);
//            mNegativeButton.setText(mNegativeButtonText);
//
//            mNegativeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//
//                    if (mListener != null) {
//                        mListener.onCancel(getTargetRequestCode());
//                    }
//                }
//            });
//
//        } else {
//            mNegativeButton.setVisibility(View.GONE);
//        }
//
//        mSignatureTextView.setVisibility(mIsShowSignature ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(mTitle);
//        builder.setMessage(mMessage);
//
//        if (mPositiveButtonText != null) {
//            builder.setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dismiss();
//
//                    if (mListener != null) {
//                        mListener.onPositiveButtonClicked(getTargetRequestCode());
//                    }
//                }
//            });
//        }
//
//        if (mNegativeButtonText != null) {
//            builder.setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dismiss();
//
//                    if (mListener != null) {
//                        mListener.onCancel(getTargetRequestCode());
//                    }
//                }
//            });
//        }
//
//
//        return builder.create();
//    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (mListener != null) {
            mListener.onCancel(getTargetRequestCode());
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isShow) return;
        super.show(manager, tag);
        isShow = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isShow = false;
        super.onDismiss(dialog);
    }
}
