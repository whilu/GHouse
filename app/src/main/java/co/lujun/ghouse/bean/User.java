package co.lujun.ghouse.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lujun on 2015/8/3.
 */
@DatabaseTable(tableName = "user")
public class User {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    @DatabaseField(columnName = "uid", id = true)
    private long uid;
    @DatabaseField(columnName = "username")
    private String username;
    @DatabaseField(columnName = "upwd")
    private String upwd;
    @DatabaseField(columnName = "validate_time")
    private long validate_time;
    @DatabaseField(columnName = "phone")
    private String phone;
    @DatabaseField(columnName = "avatar")
    private String avatar;
    @DatabaseField(columnName = "houseid")
    private long houseid;
    @DatabaseField(columnName = "usertype")
    private int usertype;
}
