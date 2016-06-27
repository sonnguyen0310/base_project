package sng.com.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import retrofit.client.Response;
import sng.com.base.BaseApplication;

/**
 * Created by son.nguyen on 4/5/2016.
 */
public class Utils {
    private static final String PREFERENCE = "Main_preference";

    public static JsonObject toJson(Response result) {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (new JsonParser()).parse(sb.toString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("CommitPrefEdits")
    public static void savePreference(Context context, String key, String value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @SuppressLint("CommitPrefEdits")
    public static void savePreference(Context context, String key, boolean value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    @SuppressLint("CommitPrefEdits")
    public static void delPreference(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String readPreference(Context context, String key) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static boolean readBoolPreference(Context context, String key) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static boolean isConnectionOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String priceDecimal(double price) {
        DecimalFormat df = new DecimalFormat("###.###");
        return df.format(price);
    }

    public static String parseDate(String date) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            Date tmpDate = sdf.parse(date);
            return output.format(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String parseDateToPost(String date) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            Date tmpDate = sdf.parse(date);
            return output.format(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String getPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return res;
        }
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static Bitmap getBitMapFromUri(Context context, Uri uri) {
        InputStream imageStream;
        try {
            imageStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap image = BitmapFactory.decodeStream(imageStream);
//            Bitmap rotation = ExifUtil.rotateBitmap(getPathFromURI(context, uri), image);
            Bitmap resizedBitmap = bitmapResize(image, 400, 400);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bitmapdata = byteArrayOutputStream.toByteArray();
            return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void hideKeyBoard(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public static double roundDoubleRating(Double number) {
        if (number == null) {
            return 0.0;
        } else {
            double newNumber;
            newNumber = Math.round(number * 100.0) / 100.0;
            return newNumber;
        }
    }

    public static Double roundDoubleMoney(Double number) {
        if (number == null) {
            return 0.0;
        } else {
            double newNumber;
            newNumber = Math.round(number * 100.0) / 100.0;
            return newNumber;
        }
    }

    private static Bitmap bitmapResize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static boolean isGpsEnable() {
        LocationManager manager = (LocationManager) BaseApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static int getDurationMin(String start, String end) {
        int returnDuration = 0;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date startTime = sdf.parse(start);
            Date endTime = sdf.parse(end);
            long duration = (endTime.getTime() - startTime.getTime());
            returnDuration = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
            if (returnDuration < 1) {
                returnDuration = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDuration;
    }

    public static Bitmap getCircleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }
}
