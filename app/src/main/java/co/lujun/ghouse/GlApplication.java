package co.lujun.ghouse;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.litepal.LitePalApplication;

/**
 * Created by lujun on 2015/3/1.
 */
public class GlApplication extends LitePalApplication {

    private static Context sContext;
    private static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public static Context getContext(){
        return sContext;
    }

    public static RequestQueue getRequestQueue(){ return mRequestQueue; }
}
