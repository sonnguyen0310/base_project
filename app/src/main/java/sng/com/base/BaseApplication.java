package sng.com.base;

/**
 * Created by son.nguyen on 3/17/2016.
 */
public class BaseApplication extends android.app.Application {
    public static BaseApplication sSingleton;

    @Override
    public void onCreate() {
        super.onCreate();
        sSingleton = this;
    }

    public static BaseApplication getContext() {
        return sSingleton;
    }

    public interface OnToolBarCallback {
        void onMenuToolbarClicked();
    }
}
