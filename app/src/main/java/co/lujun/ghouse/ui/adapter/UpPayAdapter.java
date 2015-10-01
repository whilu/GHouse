package co.lujun.ghouse.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.UpPay;
import co.lujun.ghouse.bean.User;
import co.lujun.ghouse.util.DatabaseHelper;

/**
 * Created by lujun on 2015/8/4.
 */
public class UpPayAdapter extends RecyclerView.Adapter<UpPayAdapter.UpPayItemViewHolder> {

    private List<UpPay> mList;
    private Dao mUserDao;
    private String[] moneyFlags;

    public UpPayAdapter(List<UpPay> list){
        mList = list;
        try {
            mUserDao =
                    DatabaseHelper.getDatabaseHelper(GlApplication.getContext()).getDao(User.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
        moneyFlags = GlApplication.getContext().getResources().getStringArray(R.array.money_flag);
    }

    @Override public int getItemCount() {
        return mList.size();
    }

    @Override public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override public UpPayAdapter.UpPayItemViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_uppay_item, viewGroup, false);
        return new UpPayItemViewHolder(view);
    }

    @Override public void onBindViewHolder(UpPayItemViewHolder holder, int position) {
        holder.tvDate.setText(
                GlApplication.getSimpleDateDotFormat().format(mList.get(position).getCreate_time()));
        holder.tvAmount.setText(GlApplication.getContext().getString(R.string.tv_plus)
                + mList.get(position).getAmount());
        String uname = "";
        try {
            List<User> userList = mUserDao.queryForEq("uid", mList.get(position).getUid());
            if (userList != null && userList.size() > 0){
                uname = userList.get(0).getUsername();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        String moneyFlag = "";
        if (mList.get(position).getMoney_flag() >= 0
                && mList.get(position).getMoney_flag() < moneyFlags.length){
            moneyFlag = moneyFlags[mList.get(position).getMoney_flag()];
        }else {
            moneyFlag = moneyFlags[0];
        }
        String operator = GlApplication.getContext().getString(R.string.tv_opeartor) + ": " + uname;
        String coin = GlApplication.getContext().getString(R.string.tv_coin) + ": " + moneyFlag;
        String remark = GlApplication.getContext().getString(R.string.til_hint_bill_extra) + ": "
                + mList.get(position).getRemark();

        holder.tvExtra.setText(operator + ", " + coin + ", " + remark);
    }

    public static class UpPayItemViewHolder extends RecyclerView.ViewHolder{

        TextView tvDate, tvAmount, tvExtra;

        public UpPayItemViewHolder(View view){
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tv_vui_date);
            tvAmount = (TextView) view.findViewById(R.id.tv_vui_amoune);
            tvExtra = (TextView) view.findViewById(R.id.tv_vui_extra);
        }
    }
}