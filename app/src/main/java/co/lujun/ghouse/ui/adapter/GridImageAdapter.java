package co.lujun.ghouse.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import co.lujun.ghouse.R;

/**
 * Created by lujun on 2015/9/30.
 */
public class GridImageAdapter extends BaseAdapter {


    private static int TYPE_0 = 0;
    private static int TYPE_1 = TYPE_0 + 1;
    private static int TYPE_COUNT = 2;
    private int mMaxSize = 6;

    private List<Uri> mList;
    private LayoutInflater mInflater;
    private OnOperateListener mListener;

    private static final String TAG = "GridImageAdapter";

    public GridImageAdapter(Context context, List<Uri> list){
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override public int getItemViewType(int position) {
        if (position < mList.size()){
            return TYPE_0;
        }
        return TYPE_1;
    }

    @Override public int getViewTypeCount() {
        if (mList.size() < mMaxSize){
            return TYPE_COUNT;
        }else {
            return TYPE_COUNT - 1;
        }
    }

    @Override public int getCount() {
        if (mList.size() < mMaxSize){
            return mList.size() + 1;
        }
        if (mList.size() > mMaxSize){
            for (int i = mList.size() - 1; i >= mMaxSize; i--){
                mList.remove(i);
            }
        }
        return mMaxSize;
    }

    @Override public Object getItem(int i) {
        return mList.get(i);
    }

    @Override public long getItemId(int i) {
        return 0;
    }

    @Override public View getView(final int i, View view, ViewGroup viewGroup) {
//        Log.d(TAG, getItemViewType(i) + "");
        if (getItemViewType(i) == TYPE_0){
            ImageViewHolder holder;
            if (view == null){
                holder = new ImageViewHolder();
                view = mInflater.inflate(R.layout.view_grid_imageview_item, null, false);
                holder.imageView = (ImageView) view.findViewById(R.id.iv_photos_item);
                holder.btnDelete = (ImageButton) view.findViewById(R.id.btn_img_delete);
                view.setTag(holder);
            }else {
                holder = (ImageViewHolder) view.getTag();
            }
            holder.imageView.setImageURI(mList.get(i));
            holder.btnDelete.setOnClickListener(v -> {
                    if (mListener != null){
                        mListener.onDeleteImage(i);
                        notifyDataSetChanged();
                    }
            });
        }else {
            ImageViewAddHolder holder;
            if (view == null){
                holder = new ImageViewAddHolder();
                view = mInflater.inflate(R.layout.view_grid_imageview_add_item, null, false);
                holder.btnAdd = (ImageButton) view.findViewById(R.id.btn_img_add);
                view.setTag(holder);
            }else {
                holder = (ImageViewAddHolder) view.getTag();
            }
            holder.btnAdd.setOnClickListener(v -> {
                    if (mList.size() >= mMaxSize){
                        Log.i(TAG, "Adapter has reached the max size!");
                        return;
                    }
                    if (mListener != null){
                        mListener.onAddImage();
//                        notifyDataSetChanged();
                    }
            });
        }
        return view;
    }

    class ImageViewHolder{
        ImageView imageView;
        ImageButton btnDelete;
    }

    class ImageViewAddHolder{
        ImageButton btnAdd;
    }

    public interface OnOperateListener{
        void onAddImage();
        void onDeleteImage(int position);
    }

    /**
     * set the adapter max size
     * @param size
     */
    public void setMaxSize(int size){
        this.mMaxSize = size;
    }

    public void setOnOperateListener(OnOperateListener listener){
        this.mListener = listener;
    }
}