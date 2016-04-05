package sng.com.base.service.dataprovider;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import sng.com.base.R;

/**
 * Created by son.nguyen on 3/19/2016.
 */
public class ApiService implements ApiCall {
    private ApiCall mApiCall;
    private static ApiService mInstance;
    private OkHttpClient mOkHttpClient;

    private ApiService(Context context) {
        mOkHttpClient = getUnsafeOkHttpClient();
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

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }


                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }


                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };


            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });


            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getAppID() {
        return "MlR6vYpYvLRxfibxE5cg0e73jXojL6jWFqXU6F8L";
    }

    public String getApiKEY() {
        return "7BTXVX1qUXKUCnsngL8LxhpEHKQ8KKd798kKpD9W";
    }

}
