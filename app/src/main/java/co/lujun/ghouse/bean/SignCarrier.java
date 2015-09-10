package co.lujun.ghouse.bean;

/**
 * Created by lujun on 2015/9/2.
 */
public class SignCarrier {

    //app request config information
    private static final String APP_ID = "3";
    private static final String APP_TOKEN = "b1a93bc94fce9d08bc9ca1e2a6fe883c";

    public static String getAppId() {
        return APP_ID;
    }

    public static String getAppToken() {
        return APP_TOKEN;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private String timestamp;

    private String nonce;

    private String signature;
}
