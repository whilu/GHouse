package co.lujun.ghouse.bean;

import co.lujun.ghouse.BuildConfig;

/**
 * Created by lujun on 2015/9/2.
 */
public class SignCarrier {

    public static String getAppId() {
        return BuildConfig.CLIENT_ID;
    }

    public static String getAppToken() {
        return BuildConfig.CLIENT_TOKEN;
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
