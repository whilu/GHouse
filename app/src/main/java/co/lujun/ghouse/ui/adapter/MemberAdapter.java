package co.lujun.ghouse.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.lujun.ghouse.GlApplication;
import co.lujun.ghouse.R;
import co.lujun.ghouse.ui.listener.ViewClickListener;
import co.lujun.ghouse.ui.widget.roundedimageview.RoundedImageView;

/**
 * Created by lujun on 2015/8/4.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<String> mList;
    private ViewClickListener mListener;
    public MemberAdapter(List<String> list){
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
    public MemberAdapter.MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_house_member, viewGroup, false);
        return new MemberViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.MemberViewHolder recyclerViewHolder, int i) {
        Picasso.with(GlApplication.getContext())
            .load(mList.get(i) == null ? "" : mList.get(i))
            .placeholder(R.drawable.ic_timer_auto_grey600_48dp)
            .into(recyclerViewHolder.ivAvatar);
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final RoundedImageView ivAvatar;
        private ViewClickListener mViewClickListener;

        public MemberViewHolder(View view, ViewClickListener listener){
            super(view);
            this.mViewClickListener = listener;
            ivAvatar = (RoundedImageView) view.findViewById(R.id.iv_house_member_avatar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mViewClickListener != null){
                mViewClickListener.onClick(getPosition());
            }
        }
    }

    /**
     * set on click listener
     * @param listener
     */
    public void setViewOnClickListener(ViewClickListener listener){
        this.mListener = listener;
    }
}