package co.lujun.ghouse.bean;

/**
 * Created by lujun on 2015/8/12.
 */
public class Config {

    // app config
    public static final long APP_SPLASH_TIME = 1000;
    //
    public static final long SPLASH_LAYER_ANIM_TIME = 1000;
    // random string length
    public static final int NONCE_LENGTH = 6;

    // Activity Result Code
    public static final int ACTIVITY_REQ_SCAN = 101;
    public static final int ACTIVITY_REQ_CAMERA = 102;
    public static final int ACTIVITY_RES_SCAN = 103;

    // app path
    private static final String APP_PATH_HOST = "/ghouse/";
    public static final String APP_IMAGE_PATH = APP_PATH_HOST + "images/";

    // server status
    public static final int STATUS_CODE_OK = 200;

    // keys
    public static final String KEY_OF_LOGIN_FLAG = "is_login";
    public static final String KEY_OF_VALIDATE = "validate";
    public static final String KEY_SCAN_CODE_RESULT = "key_scan_code_result";
    public static final String KEY_OF_FRAGMENT = "key_fragment";
    public static final String KEY_OF_USER_TYPE = "key_user_type";
    public static final String KEY_OF_USER_NAME = "key_user_name";
    public static final String KEY_OF_BID = "key_bill_id";

    // fragment key
    public static final int BILL_UNKNOW = 0x1000;
    public static final int BILL_FRAGMENT = 0x1001;
    public static final int TODO_FRAGMENT = 0x1002;

    // broadcast intent filter action
    public static final String ACTION_LOGOUT = "co.lujun.ghouse.action.logout";
}
