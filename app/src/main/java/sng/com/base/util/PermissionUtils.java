package sng.com.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnguyenh on 5/13/2016.
 */
public class PermissionUtils {
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionActivity(Context context, String permission){
        int hasLocationPermission = ActivityCompat.checkSelfPermission(context, permission);
        List<String> permissions = new ArrayList<>();
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(permission);
        }
        if (!permissions.isEmpty()) {
            if (context instanceof Activity){
                ((Activity) context).requestPermissions(permissions.toArray(new String[permissions.size()]), Constant.REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }
    }
}
