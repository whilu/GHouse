package co.lujun.ghouse;

import android.app.Application;
import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import co.lujun.ghouse.api.ApiService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by lujun on 2015/3/1.
 */
public class GlApplication extends Application {

    private static Context sContext;

    private static RestAdapter sRestAdapter;
    private static ApiService sApiService;

    private static SimpleDateFormat sSimpleDateFormat;
    private static SimpleDateFormat sSimpleDateDotFormat;

    @Override public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sSimpleDateDotFormat = new SimpleDateFormat("yyyy.MM.dd");
    }

    public static Context getContext(){
        return sContext;
    }

    public static RestAdapter getRestAdapter(){
        if (sRestAdapter == null) {
            synchronized (GlApplication.class) {
                if (sRestAdapter == null) {
                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    RestAdapter.Builder builder = new RestAdapter.Builder();
                    builder.setClient(new OkClient(client));
                    sRestAdapter = builder
                            .setEndpoint(BuildConfig.API_ENDPOINT + BuildConfig.API_VERSION)
                            .build();
                }
            }
        }
        return sRestAdapter;
    }

    public static ApiService getApiService(){
        if (sApiService == null) {
            synchronized (GlApplication.class) {
                if (sApiService == null) {
                    sApiService = getRestAdapter().create(ApiService.class);
                }
            }
        }
        return sApiService;
    }

    /**
     * get SimpleDateFormat
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(){
        return sSimpleDateFormat;
    }

    /**
     * get SimpleDateFormat with dot split
     * @return
     */
    public static SimpleDateFormat getSimpleDateDotFormat(){
        return sSimpleDateDotFormat;
    }
}
