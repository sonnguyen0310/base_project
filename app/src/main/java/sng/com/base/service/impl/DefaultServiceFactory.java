package sng.com.base.service.impl;

import android.content.Context;

import sng.com.base.service.ServiceFactory;

/**
 * Created by son.nguyen on 3/18/2016.
 */
public class DefaultServiceFactory implements ServiceFactory {
    private static DefaultServiceFactory sInstance;

    public DefaultServiceFactory(Context context) {
    }

    public static DefaultServiceFactory getsInstance(Context context) {
        if (null == sInstance) {
            sInstance = new DefaultServiceFactory(context);
        }
        return sInstance;
    }


}
