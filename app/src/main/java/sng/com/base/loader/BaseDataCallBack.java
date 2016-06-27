package sng.com.base.loader;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import sng.com.base.R;
import sng.com.base.model.Data;
import sng.com.base.ui.fragment.BaseFragment;
import sng.com.base.util.Constant;

/**
 * Created by son.nguyen on 4/30/2016.
 */
public abstract class BaseDataCallBack<T> implements LoaderManager.LoaderCallbacks<Data<T>> {
    private final BaseFragment mFragment;

    public BaseDataCallBack(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public Loader<Data<T>> onCreateLoader(int id, Bundle args) {
        return onCreateLoader(args);
    }

    @Override
    public void onLoadFinished(Loader<Data<T>> loader, Data<T> data) {
        if (data == null) {
            if (mFragment != null) {
                mFragment.showConfirmDialog(mFragment.getContext().getString(R.string.common_error_network), null, true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onRestartLoader();
                    }
                });
            }
        } else {
            if (data.getReturnCode() == Constant.API_RETURN_CODE_ERROR) {
                if (mFragment != null) {
                    mFragment.showConfirmDialog(data.getMessage(), null, true, null);
                }
            } else if (data.getReturnCode() == Constant.API_RETURN_CODE_ERROR_TOKEN || data.getReturnCode() == Constant.API_RETURN_CODE_EXPiRED_TOKEN) {
                if (mFragment != null) {
                    mFragment.showConfirmDialog(data.getMessage(), null, true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }

            } else if (data.getData() != null) {
                onDataReady(data.getData());
            }
        }
    }

    protected abstract Loader<Data<T>> onCreateLoader(Bundle args);

    protected abstract void onRestartLoader();

    protected abstract void onDataReady(T data);
}
