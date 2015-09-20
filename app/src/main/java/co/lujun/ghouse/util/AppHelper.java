package co.lujun.ghouse.util;

import android.content.Context;

import co.lujun.ghouse.bean.Config;

/**
 * Created by lujun on 2015/9/20.
 */
public class AppHelper {

    /**
     * check user permission
     * @return true, user can operate confirm & delete, else have no permission
     */
    public static boolean onCheckPermission(Context context){
        if (PreferencesUtils.getInt(context, Config.KEY_OF_USER_TYPE, 0) == 1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * get local user name
     * @param context
     * @return
     */
    public static String onGetLocalUserName(Context context){
        return PreferencesUtils.getString(context, Config.KEY_OF_USER_NAME, "");
    }
}
