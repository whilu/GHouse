package co.lujun.ghouse;

import android.app.Application;
import android.content.Context;

import java.text.SimpleDateFormat;

import co.lujun.ghouse.api.Api;
import co.lujun.ghouse.api.ApiService;
import retrofit.RestAdapter;

/**
 * Created by lujun on 2015/3/1.
 */
public class GlApplication extends Application {

    private static Context sContext;

    private static RestAdapter sRestAdapter;
    private static ApiService sApiService;

    private static SimpleDateFormat sSimpleDateFormat;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static Context getContext(){
        return sContext;
    }

    public static RestAdapter getRestAdapter(){
        if (sRestAdapter == null){
            synchronized (GlApplication.class){
                if (sRestAdapter == null){
                    sRestAdapter = new RestAdapter.Builder()
                            .setEndpoint(Api.API_HOST + Api.API_VERSION).build();
                }
            }
        }
        return sRestAdapter;
    }

    public static ApiService getApiService(){
        if (sApiService == null){
            synchronized (GlApplication.class){
                if (sApiService == null){
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
}
