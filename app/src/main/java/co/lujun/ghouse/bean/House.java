package co.lujun.ghouse.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/8/3.
 */
public class House extends DataSupport {

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getHousereg() {
        return housereg;
    }

    public void setHousereg(long housereg) {
        this.housereg = housereg;
    }

    private long id;

    private long houseid;

    private String houseaddress;

    private String houseinfo;

    private float money;

    private long housereg;
}
