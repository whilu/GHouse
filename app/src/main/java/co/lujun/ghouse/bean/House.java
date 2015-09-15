package co.lujun.ghouse.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by lujun on 2015/8/3.
 */
@DatabaseTable(tableName = "house")
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

    public Collection<User> getMember() {
        return member;
    }

    public void setMember(Collection<User> member) {
        this.member = member;
    }

    @DatabaseField(columnName = "hid", id = true)
    private long hid;
    @DatabaseField(columnName = "reg_user")
    private String reg_user;
    @DatabaseField(columnName = "houseaddress")
    private String houseaddress;
    @DatabaseField(columnName = "houseinfo")
    private String houseinfo;
    @DatabaseField(columnName = "money")
    private float money;
    @DatabaseField(columnName = "reguid")
    private long reguid;
    @ForeignCollectionField
    private Collection<User> member;
}
