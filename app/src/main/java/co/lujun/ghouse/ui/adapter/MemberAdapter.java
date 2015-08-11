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
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<String> mList;

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
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.MemberViewHolder recyclerViewHolder, int i) {

    }

    static class MemberViewHolder extends RecyclerView.ViewHolder{

        final ImageView ivAvatar;

        public MemberViewHolder(View view){
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_house_member_avatar);
        }
    }
}