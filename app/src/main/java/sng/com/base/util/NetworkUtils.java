package sng.com.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by son.nguyen on 3/17/2016.
 */
public class NetworkUtils {

    private NetworkUtils() {
    }

    /**
     * Checking for all possible internet providers
     */
    public static boolean isNetworkAvailable(final Context context) {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
