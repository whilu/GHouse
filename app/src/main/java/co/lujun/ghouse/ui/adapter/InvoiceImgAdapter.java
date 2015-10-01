package co.lujun.ghouse.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.listener.ViewClickListener;

/**
 * Created by lujun on 2015/8/4.
 */
public class InvoiceImgAdapter extends RecyclerView.Adapter<InvoiceImgAdapter.MemberViewHolder> {

    private List<String> mList;
    private ViewClickListener mListener;
    public InvoiceImgAdapter(List<String> list){
        mList = list;
    }

    @Override public int getItemCount() {
        return mList.size();
    }

    @Override public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override public InvoiceImgAdapter.MemberViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_invoice, viewGroup, false);
        return new MemberViewHolder(view, mListener);
    }

    @Override public void onBindViewHolder(
            InvoiceImgAdapter.MemberViewHolder recyclerViewHolder, int i) {
        Picasso.with(GlApplication.getContext())
                .load(mList.get(i) == null ? "" : mList.get(i))
                .placeholder(R.drawable.ic_image_grey600_48dp)
                .into(recyclerViewHolder.ivImage);
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView ivImage;
        private ViewClickListener mImageClicklistener;

        public MemberViewHolder(View view, ViewClickListener listener){
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.iv_bd_voice);
            mImageClicklistener = listener;
            view.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            if (mImageClicklistener != null){
                mImageClicklistener.onClick(getPosition());
            }
        }
    }

    public void setImageClickListener(ViewClickListener listener){
        this.mListener = listener;
    }
}