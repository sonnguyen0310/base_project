package sng.com.base.loader;

import android.content.Context;

import sng.com.base.common.loader.AbstractAsyncTaskLoader;
import sng.com.base.util.LogUtils;

/**
 * Created by son.nguyen on 4/5/2016.
 */
public abstract class BaseLoader<T> extends AbstractAsyncTaskLoader<T> {

    private Exception mException;

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public final T loadInBackground() {
        //Ensuring the previous error is cleared
        mException = null;

        try {
            return doLoadInBackground();
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
            mException = e;
        }

        return null;
    }

    protected abstract T doLoadInBackground() throws Exception;

}
