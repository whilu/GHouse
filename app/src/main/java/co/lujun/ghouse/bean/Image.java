package co.lujun.ghouse.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lujun on 2015/8/3.
 */
@DatabaseTable(tableName = "image")
public class Image {

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    @DatabaseField(canBeNull = true, foreign = true, columnName = "bill", foreignAutoRefresh = true)
    private Bill bill;
    @DatabaseField(columnName = "small")
    private String small;
    @DatabaseField(columnName = "large")
    private String large;
    @DatabaseField(columnName = "bid")
    private long bid;
}
