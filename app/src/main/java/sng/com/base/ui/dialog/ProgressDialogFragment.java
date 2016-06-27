package sng.com.base.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sng.com.base.R;
import sng.com.base.ui.other.IDialogFragmentListener;


public class ProgressDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "ARG_TITLE";

    public static <T extends Fragment & IDialogFragmentListener> ProgressDialogFragment newInstance(T listener, int dialogId) {
        return newInstance(null, listener, dialogId);
    }

    public static <T extends Fragment & IDialogFragmentListener> ProgressDialogFragment newInstance(String title, T listener, int dialogId) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setTargetFragment(listener, dialogId);

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Transparent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_dialog, container, false);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (getTargetFragment() instanceof IDialogFragmentListener) {
            ((IDialogFragmentListener) getTargetFragment()).onCancel(getTargetRequestCode());
        }
    }
}
