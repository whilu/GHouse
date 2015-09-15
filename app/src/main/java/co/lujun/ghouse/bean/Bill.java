package co.lujun.ghouse.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lujun on 2015/8/3.
 */
@DatabaseTable(tableName = "bill")
public class Bill {

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getQcode() {
        return qcode;
    }

    public void setQcode(String qcode) {
        this.qcode = qcode;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public long getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(long confirm_time) {
        this.confirm_time = confirm_time;
    }

    public int getConfirm_status() {
        return confirm_status;
    }

    public void setConfirm_status(int confirm_status) {
        this.confirm_status = confirm_status;
    }

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getMoney_flag() {
        return money_flag;
    }

    public void setMoney_flag(int money_flag) {
        this.money_flag = money_flag;
    }

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    public String getAdd_user() {
        return add_user;
    }

    public void setAdd_user(String add_user) {
        this.add_user = add_user;
    }

    public String getConf_user() {
        return conf_user;
    }

    public void setConf_user(String conf_user) {
        this.conf_user = conf_user;
    }

    @DatabaseField(columnName = "bid", id = true)
    private long bid;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "create_time")
    private long create_time;//添加时间
    @DatabaseField(columnName = "qcode")
    private String qcode;
    @DatabaseField(columnName = "total")
    private float total;
    @DatabaseField(columnName = "remark")
    private String remark;
    @DatabaseField(columnName = "securityCode")
    private String securityCode;//MD5(houseid + id)
    @DatabaseField(columnName = "last_time")
    private long last_time;//最后修改时间
    @DatabaseField(columnName = "confirm_time")
    private long confirm_time;
    @DatabaseField(columnName = "confirm_status")
    private int confirm_status;// 0 un confirm, 1 confirm
    @DatabaseField(columnName = "photos")
    private String[] photos;//max size is 2, if the size exceed 2, will get the first two
    @DatabaseField(columnName = "type_id")
    private int type_id;//012345，吃穿住行玩其他，EWLTPO，eat-wear-live-travel-play-other，红黄蓝绿棕灰
    @DatabaseField(columnName = "money_flag")
    private int money_flag;//0-RMB, 1-Dollar, 2-Other
    @DatabaseField(columnName = "houseid")
    private long houseid;
    @DatabaseField(columnName = "add_user")
    private String add_user;
    @DatabaseField(columnName = "conf_user")
    private String conf_user;

}
