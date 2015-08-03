package co.lujun.ghouse.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/8/3.
 */
public class User extends DataSupport {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public long getLatestlogin() {
        return latestlogin;
    }

    public void setLatestlogin(long latestlogin) {
        this.latestlogin = latestlogin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private long id;

    private String uname;

    private String upwd;

    private long latestlogin;

    private String phone;

    private String avatar;
}
