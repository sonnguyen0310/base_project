package sng.com.base.service.dataprovider;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import sng.com.base.R;

/**
 * Created by son.nguyen on 3/19/2016.
 */
public class ApiService implements ApiCall {
    private ApiCall mApiCall;
    private static ApiService mInstance;

    private ApiService(Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gsonBuilder.create()))
                .setEndpoint(context.getString(R.string.global_base_url))
                .build();
        mApiCall = restAdapter.create(ApiCall.class);
    }

    public static ApiService getInstance(Context mContext) {
        if (null == mInstance) {
            mInstance = new ApiService(mContext);
        }
        return mInstance;
    }

    public String getAppID() {
        return "MlR6vYpYvLRxfibxE5cg0e73jXojL6jWFqXU6F8L";
    }

    public String getApiKEY() {
        return "7BTXVX1qUXKUCnsngL8LxhpEHKQ8KKd798kKpD9W";
    }

}
