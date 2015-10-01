package co.lujun.ghouse.bean;

/**
 * Created by lujun on 2015/10/1.
 */
public class UpPay {

    private long id;

    private long uid;

    private long hid;

    private String remark;

    private long create_time;

    private int money_flag;

    private float amount;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getHid() {
        return hid;
    }

    public void setHid(long hid) {
        this.hid = hid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getMoney_flag() {
        return money_flag;
    }

    public void setMoney_flag(int money_flag) {
        this.money_flag = money_flag;
    }
}
