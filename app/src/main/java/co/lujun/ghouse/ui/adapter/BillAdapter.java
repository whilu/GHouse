package co.lujun.ghouse.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;

import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;

/**
 * Created by lujun on 2015/8/4.
 */
public class BillAdapter extends RecyclerSwipeAdapter<BillAdapter.BillItemViewHolder> {

    private BillItemViewHolder.ItemClickListener mItemClickListener;
    protected SwipeItemRecyclerMangerImpl mItemManger;

    private List<String> mList;

    public BillAdapter(List<String> list){
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
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(viewHolder.mSwipeLayout);
                mList.remove(i);
                notifyItemRemoved(i);
//                notifyItemRangeChanged(i, mList.size());
                mItemManger.closeAllItems();
                Toast.makeText(GlApplication.getContext(), "Deleted:" + viewHolder.tvType.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tvType.setText(mList.get(i) + "");
        mItemManger.bindView(viewHolder.itemView, i);
    }

    public static class BillItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        SwipeLayout mSwipeLayout;
        ImageButton btnConfirm, btnModify, btnDelete;
        TextView tvType;
        ItemClickListener mItemClickListener;

        public BillItemViewHolder(View view, ItemClickListener listener){
            super(view);
            mSwipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_bill_item);
            btnConfirm = (ImageButton) view.findViewById(R.id.btn_bi_confirm);
            btnModify = (ImageButton) view.findViewById(R.id.btn_bi_modify);
            btnDelete = (ImageButton) view.findViewById(R.id.btn_bi_delete);
            tvType = (TextView) view.findViewById(R.id.tv_bi_type);
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