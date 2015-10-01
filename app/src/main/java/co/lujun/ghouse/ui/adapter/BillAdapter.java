package co.lujun.ghouse.ui.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.bean.Bill;
import co.lujun.ghouse.bean.Image;
import co.lujun.ghouse.util.AppHelper;
import co.lujun.ghouse.util.SystemUtil;

/**
 * Created by lujun on 2015/8/4.
 */
public class BillAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private final static int TYPE_0 = 0;
    private final static int TYPE_1 = TYPE_0 + 1;

    private BillItemViewHolder.ItemClickListener mItemClickListener;
    private OnBillOperationListener mBillOperationListener;
    protected SwipeItemRecyclerMangerImpl mItemManger;

    private List<Bill> mList;

    private RvFooterViewHolder rvFooterViewHolder;

    public BillAdapter(List<Bill> list){
        mList = list;
        mItemManger = new SwipeItemRecyclerMangerImpl(this);
    }

    public void setItemClickListener(BillItemViewHolder.ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override public int getItemCount() {
        if (mList.size() == 0){
            return 0;
        }
        return mList.size() + 1;
    }

    @Override public int getItemViewType(int position) {
        if (position == mList.size()){
            return TYPE_1;
        }else {
            return TYPE_0;
        }
    }

    @Override public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == TYPE_0){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_bill_item, viewGroup, false);
            view.setTag(mList.get(i));
            return new BillItemViewHolder(view, mItemClickListener);
        }else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_rv_footer, viewGroup, false);
            return (rvFooterViewHolder = new RvFooterViewHolder(view));
        }
    }

    @Override public void onBindViewHolder(
            final RecyclerView.ViewHolder recyclerViewHolder, final int i) {
        if (getItemViewType(i) == TYPE_0){
            final BillItemViewHolder viewHolder = (BillItemViewHolder) recyclerViewHolder;
            viewHolder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            viewHolder.mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {

                @Override public void onOpen(SwipeLayout layout) {
                    super.onOpen(layout);
//                Toast.makeText(GlApplication.getContext(), mList.get(i) + "--Open", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.btnConfirm.setOnClickListener(v -> {
                if (mBillOperationListener != null){
                    mItemManger.closeAllItems();
                    mBillOperationListener.onConfirmBill(i);
                }
            });
            viewHolder.btnModify.setOnClickListener(v -> {
                if (mBillOperationListener != null){
                    mItemManger.closeAllItems();
                    mBillOperationListener.onEditBill(i);
                }
            });
            viewHolder.btnDelete.setOnClickListener(v -> {
                /*Snackbar snackbar = Snackbar.make(v, "Delete:" + viewHolder.tvType.getText(), Snackbar.LENGTH_LONG)
                    .setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .setActionTextColor(GlApplication.getContext().getResources().getColor(R.color.accent_material_dark));
                snackbar.getView().setBackgroundColor(GlApplication.getContext().getResources().getColor(R.color.background_floating_material_light));
                snackbar.show();*/
                if (mBillOperationListener != null){
                    if (!AppHelper.onCheckPermission(GlApplication.getContext())){
                        SystemUtil.showToast(R.string.msg_have_no_permission);
                        mItemManger.closeAllItems();
                        return;
                    }
                    mBillOperationListener.onDeleteBill(i);
                    mItemManger.removeShownLayouts(viewHolder.mSwipeLayout);
                    mList.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, mList.size());
                    mItemManger.closeAllItems();
                }
            });
            String[] types = GlApplication.getContext().getResources().getStringArray(R.array.bill_type);
            String[] bgDrawableTypes = GlApplication.getContext().getResources().getStringArray(R.array.bill_drawable_type);
            String type, bgDrawableType;
            int tmpCostType = (int)(Math.log((double)mList.get(i).getType_id()) / Math.log(2d));
            if (tmpCostType >= 0 && tmpCostType < types.length){
                type = types[tmpCostType];
                bgDrawableType = bgDrawableTypes[tmpCostType];
            }else {
                type = types[5];
                bgDrawableType = bgDrawableTypes[5];
            }
            try {
                Field field = R.drawable.class.getField(bgDrawableType);
                int j = field.getInt(new R.drawable());
                viewHolder.tvType.setBackgroundResource(j);
            }catch (Exception e){
                e.printStackTrace();
            }

            viewHolder.tvType.setText(type);
            viewHolder.tvSummary.setText(mList.get(i).getTitle());
            String[] moneyFlags =
                    GlApplication.getContext().getResources().getStringArray(R.array.money_flag);
            String moneyFlag;
            if (mList.get(i).getMoney_flag() >= 0
                    && mList.get(i).getMoney_flag() < moneyFlags.length){
                moneyFlag = moneyFlags[mList.get(i).getMoney_flag()];
            }else {
                moneyFlag = moneyFlags[0];
            }
            viewHolder.tvTotal.setText(
                    GlApplication.getContext().getResources().getString(R.string.tv_total)
                            + moneyFlag + mList.get(i).getTotal());
            viewHolder.tvTime.setText(
                    GlApplication.getSimpleDateFormat().format(mList.get(i).getCreate_time() * 1000));
            if (mList.get(i).getSecurity_code() != null
                    && mList.get(i).getSecurity_code().length() > 7){
                viewHolder.tv7SecurityCode.setText(mList.get(i).getSecurity_code().substring(0, 7));
            }
            int voliceLength = mList.get(i).getPhotos().size();
            Iterator<Image> iterator = mList.get(i).getPhotos().iterator();
            List<String> invoiceImgList = new ArrayList<String>();
            while (iterator.hasNext()){
                invoiceImgList.add(iterator.next().getLarge());
            }
            if (voliceLength >= 2){
                voliceLength = 2;
            }
            if (voliceLength > 0){
                viewHolder.llInvoice.setVisibility(View.VISIBLE);
                switch (voliceLength){
                    case 2:
                        viewHolder.ivInvoiceR.setVisibility(View.VISIBLE);
                        viewHolder.ivInvoiceL.setVisibility(View.VISIBLE);
                        Picasso.with(GlApplication.getContext())
                                .load(invoiceImgList.get(0) == null ? "" : invoiceImgList.get(0))
                                .placeholder(R.drawable.ic_image_grey600_48dp)
                                .into(viewHolder.ivInvoiceL);
                        Picasso.with(GlApplication.getContext())
                                .load(invoiceImgList.get(1) == null ? "" : invoiceImgList.get(1))
                                .placeholder(R.drawable.ic_image_grey600_48dp)
                                .into(viewHolder.ivInvoiceR);
                        break;

                    case 1:
                        viewHolder.ivInvoiceL.setVisibility(View.VISIBLE);
                        viewHolder.ivInvoiceR.setVisibility(View.GONE);
                        Picasso.with(GlApplication.getContext())
                                .load(invoiceImgList.get(0) == null ? "" : invoiceImgList.get(0))
                                .placeholder(R.drawable.ic_image_grey600_48dp)
                                .into(viewHolder.ivInvoiceL);
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
    }

    public static class BillItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        LinearLayout llBillItem;
        SwipeLayout mSwipeLayout;
        ImageButton btnConfirm, btnModify, btnDelete;
        TextView tvType, tvSummary, tvTotal, tvTime, tv7SecurityCode;
        LinearLayout llInvoice;
        ImageView ivInvoiceL, ivInvoiceR;
        ItemClickListener mItemClickListener;

        public BillItemViewHolder(View view, ItemClickListener listener){
            super(view);
            llBillItem = (LinearLayout) view.findViewById(R.id.ll_bi);
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
            llBillItem.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClick(view, getPosition());
            }
        }

        public interface ItemClickListener{
            void onItemClick(View view, int position);
        }
    }

    static class RvFooterViewHolder extends RecyclerView.ViewHolder{

        ProgressBar pbFooter;

        public RvFooterViewHolder(View view){
            super(view);
            pbFooter = (ProgressBar) view.findViewById(R.id.pb_footer);
        }

        /**
         * hide progressbar
         */
        public void hideProgressBar(){
            this.pbFooter.setVisibility(View.GONE);
        }

        /**
         * show progressbar
         */
        public void showProgressBar(){
            this.pbFooter.setVisibility(View.VISIBLE);
        }
    }

    @Override public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe_bill_item;
    }

    public void setBillOperationListener(OnBillOperationListener listener){
        this.mBillOperationListener = listener;
    }

    /**
     * hide the footer's progressBar
     */
    public void hideFooter(){
        rvFooterViewHolder.hideProgressBar();
    }

    /**
     * show the footer's progressBar
     */
    public void showFooter(){
        rvFooterViewHolder.showProgressBar();
    }

    public interface OnBillOperationListener{
        void onConfirmBill(int position);
        void onEditBill(int positin);
        void onDeleteBill(int position);
    }
}