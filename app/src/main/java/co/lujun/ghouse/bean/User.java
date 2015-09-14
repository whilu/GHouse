package co.lujun.ghouse.bean;

/**
 * Created by lujun on 2015/8/3.
 */
public class User {

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

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getValidate_time() {
        return validate_time;
    }

    public void setValidate_time(long validate_time) {
        this.validate_time = validate_time;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    private long uid;

    private String uname;

    private String upwd;

    private long validate_time;

    private String phone;

    private String avatar;

    private long houseid;

    private int usertype;
}
