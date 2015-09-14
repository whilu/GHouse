package co.lujun.ghouse.bean;

import java.util.List;

/**
 * Created by lujun on 2015/8/3.
 */
public class House {

    public String getHouseaddress() {
        return houseaddress;
    }

    public void setHouseaddress(String houseaddress) {
        this.houseaddress = houseaddress;
    }

    public String getHouseinfo() {
        return houseinfo;
    }

    public void setHouseinfo(String houseinfo) {
        this.houseinfo = houseinfo;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public long getHid() {
        return hid;
    }

    public void setHid(long hid) {
        this.hid = hid;
    }

    public String getReg_user() {
        return reg_user;
    }

    public void setReg_user(String reg_user) {
        this.reg_user = reg_user;
    }

    public long getReguid() {
        return reguid;
    }

    public void setReguid(long reguid) {
        this.reguid = reguid;
    }

    public List<User> getMember() {
        return member;
    }

    public void setMember(List<User> member) {
        this.member = member;
    }

    private long hid;

    private String reg_user;

    private String houseaddress;

    private String houseinfo;

    private float money;

    private long reguid;

    private List<User> member;
}
