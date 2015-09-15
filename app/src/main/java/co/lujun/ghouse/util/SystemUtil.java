package co.lujun.ghouse.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by lujun on 2015/9/15.
 */
public class SystemUtil {

    /**
     * 自动显示或隐藏软键盘静态方法
     * @param context
     */
    public static void showOrHideInputMethodManager(Context context){
        InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏软键盘方法
     * @param activity 当前软键盘所在的activity
     */
    public static void hideInputMethodManager(Activity activity){
        InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
