package co.lujun.ghouse.bean;

/**
 * Created by lujun on 2015/9/29.
 */
public class UploadToken {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token; // qiniu upload token
}
