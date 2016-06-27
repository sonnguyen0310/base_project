package sng.com.base.loader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import sng.com.base.model.Data;
import sng.com.base.ui.activity.BaseActivity;

/**
 * Created by son.nguyen on 5/1/2016.
 */
public abstract class BaseDataCallBackActivity<T> implements LoaderManager.LoaderCallbacks<Data<T>> {
    final BaseActivity mActivity;

    public BaseDataCallBackActivity(BaseActivity fragment) {
        mActivity = fragment;
    }

    @Override
    public Loader<Data<T>> onCreateLoader(int id, Bundle args) {
        return onCreateLoader(args);
    }

    @Override
    public void onLoadFinished(Loader<Data<T>> loader, Data<T> data) {
        if (data == null) {
            if (mActivity != null) {
                mActivity.showConfirmDialog(mActivity.getBaseContext().getString(R.string.common_error_network), null, true, true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onRestartLoader();
                    }
                });
            }
        } else {
            if (data.getReturnCode() == Consts.API_RETURN_CODE_ERROR) {
                if (mActivity != null) {
                    mActivity.showConfirmDialog(data.getMessage(), null, true, true, null);
                }
            } else if (data.getReturnCode() == Consts.API_RETURN_CODE_ERROR_TOKEN) {
                if (mActivity != null) {
                    mActivity.showConfirmDialog(data.getMessage(), null, true, true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mActivity, SplashActivity.class);
                            UserSession.getInstance().clearSession();
                            mActivity.startActivity(intent);
                            mActivity.finish();
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
