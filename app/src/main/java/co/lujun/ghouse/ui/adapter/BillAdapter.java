package co.lujun.ghouse.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Bill;

/**
 * Created by lujun on 2015/8/4.
 */
public class BillAdapter extends RecyclerSwipeAdapter<BillAdapter.BillItemViewHolder> {

    private BillItemViewHolder.ItemClickListener mItemClickListener;
    protected SwipeItemRecyclerMangerImpl mItemManger;

    private List<Bill> mList;

    public BillAdapter(List<Bill> list){
        mList = list;
        mItemManger = new SwipeItemRecyclerMangerImpl(this);
    }

    public void setItemClickListener(BillItemViewHolder.ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public BillItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_bill_item, viewGroup, false);
        view.setTag(mList.get(i));
        return new BillItemViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final BillItemViewHolder viewHolder, final int i) {
        viewHolder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {

            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
//                Toast.makeText(GlApplication.getContext(), mList.get(i) + "--Open", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GlApplication.getContext(), "Confirm:" + viewHolder.tvType.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GlApplication.getContext(), "Modify:" + viewHolder.tvType.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(viewHolder.mSwipeLayout);
                mList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mList.size());
                mItemManger.closeAllItems();
                Toast.makeText(GlApplication.getContext(), "Deleted:" + viewHolder.tvType.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        String[] types = GlApplication.getContext().getResources().getStringArray(R.array.bill_type);
        String[] bgDrawableTypes = GlApplication.getContext().getResources().getStringArray(R.array.bill_drawable_type);
        String type, bgDrawableType;
        if (mList.get(i).getType() >= 0 && mList.get(i).getMoneyFlag() < types.length){
            type = types[mList.get(i).getType()];
            bgDrawableType = bgDrawableTypes[mList.get(i).getType()];
        }else {
            type = types[0];
            bgDrawableType = bgDrawableTypes[0];
        }
        try {
            Field field = R.drawable.class.getField(bgDrawableType);
            int j = field.getInt(new R.drawable());
            viewHolder.tvType.setBackgroundResource(j);
        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.tvType.setText(type);
        viewHolder.tvSummary.setText(mList.get(i).getSummary());
        String[] moneyFlags = GlApplication.getContext().getResources().getStringArray(R.array.money_flag);
        String moneyFlag;
        if (mList.get(i).getMoneyFlag() >= 0 && mList.get(i).getMoneyFlag() < moneyFlags.length){
            moneyFlag = moneyFlags[mList.get(i).getMoneyFlag()];
        }else {
            moneyFlag = moneyFlags[0];
        }
        viewHolder.tvTotal.setText(
                GlApplication.getContext().getResources().getString(R.string.tv_total)
                        + moneyFlag + mList.get(i).getTotal());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.tvTime.setText(format.format(mList.get(i).getTime()));
        viewHolder.tv7SecurityCode.setText(mList.get(i).getSecurityCode().substring(0, 7));
        int voliceLength = mList.get(i).getInvoice().length;
        if (voliceLength > 0){
            viewHolder.llInvoice.setVisibility(View.VISIBLE);
            switch (voliceLength){
                case 2:
                    viewHolder.ivInvoiceR.setVisibility(View.VISIBLE);
                    viewHolder.ivInvoiceL.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    viewHolder.ivInvoiceL.setVisibility(View.VISIBLE);
                    viewHolder.ivInvoiceR.setVisibility(View.GONE);
                    break;

                default:
                    viewHolder.ivInvoiceL.setVisibility(View.GONE);
                    viewHolder.ivInvoiceR.setVisibility(View.GONE);
                    break;
            }
        }else {
            viewHolder.llInvoice.setVisibility(View.GONE);
        }
        mItemManger.bindView(viewHolder.itemView, i);
    }

    public static class BillItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        SwipeLayout mSwipeLayout;
        ImageButton btnConfirm, btnModify, btnDelete;
        TextView tvType, tvSummary, tvTotal, tvTime, tv7SecurityCode;
        LinearLayout llInvoice;
        ImageView ivInvoiceL, ivInvoiceR;
        ItemClickListener mItemClickListener;

        public BillItemViewHolder(View view, ItemClickListener listener){
            super(view);
            mSwipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_bill_item);
            btnConfirm = (ImageButton) view.findViewById(R.id.btn_bi_confirm);
            btnModify = (ImageButton) view.findViewById(R.id.btn_bi_modify);
            btnDelete = (ImageButton) view.findViewById(R.id.btn_bi_delete);
            tvType = (TextView) view.findViewById(R.id.tv_bi_type);
            tvSummary = (TextView) view.findViewById(R.id.tv_bi_summary);
            tvTotal = (TextView) view.findViewById(R.id.tv_bi_total);
            tvTime = (TextView) view.findViewById(R.id.tv_bi_time);
            tv7SecurityCode = (TextView) view.findViewById(R.id.tv_bi_7securitycode);
            llInvoice = (LinearLayout) view.findViewById(R.id.ll_bi_invoice);
            ivInvoiceL = (ImageView) view.findViewById(R.id.iv_bi_invoice_l);
            ivInvoiceR = (ImageView) view.findViewById(R.id.iv_bi_invoice_r);
            mItemClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClick(view, getPosition());
            }
        }

        public interface ItemClickListener{
            void onItemClick(View view, int position);
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe_bill_item;
    }
}