package co.lujun.ghouse.bean;

/**
 * Created by lujun on 2015/8/12.
 */
public class Config {

    // app config
    public final static long APP_SPLASH_TIME = 1000;
    //
    public final static long SPLASH_LAYER_ANIM_TIME = 1000;
    // random string length
    public static final int NONCE_LENGTH = 6;

    // Activity Result Code
    public final static int ACTIVITY_REQ_SCAN = 101;
    public final static int ACTIVITY_REQ_CAMERA = 102;
    public final static int ACTIVITY_RES_SCAN = 103;

    // app path
    private final static String APP_PATH_HOST = "/ghouse/";
    public final static String APP_IMAGE_PATH = APP_PATH_HOST + "images/";

    // server status
    public final static int STATUS_CODE_OK = 200;

    // keys
    public final static String KEY_OF_LOGIN_FLAG = "is_login";
    public final static String KEY_OF_VALIDATE = "validate";
    public final static String KEY_SCAN_CODE_RESULT = "key_scan_code_result";
    public final static String KEY_OF_FRAGMENT = "key_fragment";

    // fragment key
    public final static int BILL_UNKNOW = 0x1000;
    public final static int BILL_FRAGMENT = 0x1001;
    public final static int TODO_FRAGMENT = 0x1002;
}
