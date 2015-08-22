package co.lujun.ghouse.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lujun on 2015/8/3.
 */
public class Bill extends DataSupport {

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public long getLatesttime() {
        return latesttime;
    }

    public void setLatesttime(long latesttime) {
        this.latesttime = latesttime;
    }

    public boolean isconfirm() {
        return isconfirm;
    }

    public void setIsconfirm(boolean isconfirm) {
        this.isconfirm = isconfirm;
    }

    public long getAddperson() {
        return addperson;
    }

    public void setAddperson(long addperson) {
        this.addperson = addperson;
    }

    public long getConfirmperson() {
        return confirmperson;
    }

    public void setConfirmperson(long confirmperson) {
        this.confirmperson = confirmperson;
    }

    public String[] getInvoice() {
        return invoice;
    }

    public void setInvoice(String[] invoice) {
        this.invoice = invoice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoneyFlag() {
        return moneyFlag;
    }

    public void setMoneyFlag(int moneyFlag) {
        this.moneyFlag = moneyFlag;
    }

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    private long id;

    private String content;

    private String summary;

    private long time;//添加时间

    private String code;

    private float total;

    private String extra;

    private String securityCode;//MD5(houseid + id)

    private long latesttime;//最后修改时间

    private boolean isconfirm;

    private long addperson;

    private long confirmperson;

    private String[] invoice;//max size is 2, if the size exceed 2, will get the first two

    private int type;//012345，吃穿住行玩其他，EWLTPO，eat-wear-live-travel-play-other，红黄蓝绿棕灰

    private int moneyFlag;//0-RMB, 1-Dollar

    private long houseid;
}
