package co.lujun.ghouse.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import co.lujun.ghouse.R;

/**
 * Created by lujun on 2015/8/4.
 */
public class InvoiceImgAdapter extends RecyclerView.Adapter<InvoiceImgAdapter.MemberViewHolder> {

    private List<String> mList;
    private ImageClickListener mListener;

    public InvoiceImgAdapter(List<String> list){
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public InvoiceImgAdapter.MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_invoice, viewGroup, false);
        return new MemberViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(InvoiceImgAdapter.MemberViewHolder recyclerViewHolder, int i) {

    }

    static class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView ivAvatar;
        private ImageClickListener mImageClicklistener;

        public MemberViewHolder(View view, ImageClickListener listener){
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_bd_voice);
            mImageClicklistener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mImageClicklistener != null){
                mImageClicklistener.onClick(getPosition());
            }
        }
    }

    public void setImageClickListener(ImageClickListener listener){
        this.mListener = listener;
    }

    public interface ImageClickListener{
        void onClick(int position);
    }
}