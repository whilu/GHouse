package co.lujun.ghouse.bean;

import java.util.List;

/**
 * Created by lujun on 2015/9/15.
 */
public class BaseList<T> {

    private int count;

    private int current_page;

    private List<T> lists;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<T> getLists() {
        return lists;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }
}
